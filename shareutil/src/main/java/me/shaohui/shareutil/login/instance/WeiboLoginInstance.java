package me.shaohui.shareutil.login.instance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import java.io.IOException;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.LoginUtil;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.WeiboToken;
import me.shaohui.shareutil.login.result.WeiboUser;
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

public class WeiboLoginInstance extends LoginInstance {

    public static final String DEFAULT_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    public static final String DEFAULT_SCOPE = "email";

    private static final String USER_INFO = "https://api.weibo.com/2/users/show.json";

    private SsoHandler mSsoHandler;

    private LoginListener mLoginListener;

    public WeiboLoginInstance(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        AuthInfo authInfo =
                new AuthInfo(activity, ShareManager.WEIBO_ID, ShareManager.WEIBO_REDIRECT_URL,
                        ShareManager.WEIBO_SCOPE);
        mSsoHandler = new SsoHandler(activity, authInfo);
        mLoginListener = listener;
    }

    @Override
    public void doLogin(Activity activity, final LoginListener listener,
            final boolean fetchUserInfo) {
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                WeiboToken weiboToken = WeiboToken.parse(accessToken);
                if (fetchUserInfo) {
                    listener.beforeFetchUserInfo(weiboToken);
                    fetchUserInfo(weiboToken);
                } else {
                    listener.doLoginSuccess(new LoginResult(LoginPlatform.WEIBO, weiboToken));
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                listener.loginFailure(e);
            }

            @Override
            public void onCancel() {
                listener.loginCancel();
            }
        });
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {
        Observable.fromEmitter(new Action1<Emitter<WeiboUser>>() {
            @Override
            public void call(Emitter<WeiboUser> weiboUserEmitter) {
                OkHttpClient client = new OkHttpClient();
                Request request =
                        new Request.Builder().url(buildUserInfoUrl(token, USER_INFO)).build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    WeiboUser user = WeiboUser.parse(jsonObject);
                    weiboUserEmitter.onNext(user);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    weiboUserEmitter.onError(e);
                }
            }
        }, Emitter.BackpressureMode.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WeiboUser>() {
                    @Override
                    public void call(WeiboUser weiboUser) {
                        mLoginListener.doLoginSuccess(
                                new LoginResult(LoginPlatform.WEIBO, token, weiboUser));
                        LoginUtil.recycle();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginListener.doLoginFailure(new Exception(throwable));
                    }
                });
    }

    private String buildUserInfoUrl(BaseToken token, String baseUrl) {
        return baseUrl + "?access_token=" + token.getAccessToken() + "&uid=" + token.getOpenid();
    }

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    @Override
    public void recycle() {
        mSsoHandler = null;
        mLoginListener = null;
    }
}
