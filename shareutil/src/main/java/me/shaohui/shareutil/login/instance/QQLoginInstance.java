package me.shaohui.shareutil.login.instance;

import android.app.Activity;
import android.content.Intent;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.io.IOException;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.QQToken;
import me.shaohui.shareutil.login.result.QQUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shaohui on 2016/12/1.
 */

public class QQLoginInstance extends LoginInstance {

    public static final String SCOPE = "get_simple_userinfo";

    private static final String URL = "https://graph.qq.com/user/get_user_info";

    private Tencent mTencent;

    private IUiListener mIUiListener;

    private LoginListener mLoginListener;

    public QQLoginInstance(Activity activity, final LoginListener listener,
            final boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        mTencent = Tencent.createInstance(ShareManager.QQ_ID, activity.getApplicationContext());
        mLoginListener = listener;
        mIUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                QQToken token = QQToken.parse((JSONObject) o);
                if (token == null) {
                    mLoginListener.doLoginFailure(new Exception("login_failure"));
                    return;
                }

                if (fetchUserInfo) {
                    listener.beforeFetchUserInfo(token);
                    fetchUserInfo(token);
                } else {
                    listener.doLoginSuccess(new LoginResult(LoginPlatform.QQ, token));
                }
            }

            @Override
            public void onError(UiError uiError) {
                listener.loginFailure(
                        new Exception("QQError: " + uiError.errorCode + uiError.errorDetail));
            }

            @Override
            public void onCancel() {
                listener.loginCancel();
            }
        };
    }

    @Override
    public void doLogin(Activity activity, final LoginListener listener, boolean fetchUserInfo) {
        mTencent = Tencent.createInstance(ShareManager.QQ_ID, activity);

        mTencent.login(activity, SCOPE, mIUiListener);
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {
        Observable.fromEmitter(new Action1<Emitter<QQUser>>() {
            @Override
            public void call(Emitter<QQUser> qqUserEmitter) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(buildUserInfoUrl(token, URL)).build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    QQUser user = QQUser.parse(token.getOpenid(), jsonObject);
                    qqUserEmitter.onNext(user);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    qqUserEmitter.onError(e);
                }
            }
        }, Emitter.BackpressureMode.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QQUser>() {
                    @Override
                    public void call(QQUser qqUser) {
                        mLoginListener.doLoginSuccess(
                                new LoginResult(LoginPlatform.QQ, token, qqUser));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginListener.doLoginFailure(new Exception(throwable));
                    }
                });
    }

    private String buildUserInfoUrl(BaseToken token, String base) {
        return base
                + "?access_token="
                + token.getAccessToken()
                + "&oauth_consumer_key="
                + ShareManager.QQ_ID
                + "&openid="
                + token.getOpenid();
    }

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        Tencent.handleResultData(data, mIUiListener);
    }

    @Override
    public void recycle() {
        mTencent.releaseResource();
        mIUiListener = null;
        mLoginListener = null;
        mTencent = null;
    }
}
