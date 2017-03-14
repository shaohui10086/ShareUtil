package me.shaohui.shareutil.login.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.FbToken;
import me.shaohui.shareutil.login.result.FbUser;

import static me.shaohui.shareutil.login.LoginPlatform.FB;

/**
 * Created by pitt on 2017/3/6.
 */

public class FbLoginInstance extends LoginInstance {

    private CallbackManager mCallbackManager;
    private LoginManager mManager;
    private FacebookCallback mFacebookCallback;
    private LoginListener mLoginListener;

    public FbLoginInstance(Activity activity, final LoginListener listener, final boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        mManager = LoginManager.getInstance();
        mManager.setDefaultAudience(DefaultAudience.FRIENDS);
        mManager.setLoginBehavior(LoginBehavior.NATIVE_ONLY);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginListener = listener;
        mFacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult result) {
                final FbToken fbToken = FbToken.parse(result.getAccessToken());
                listener.loginSuccess(new me.shaohui.shareutil.login.LoginResult(FB, fbToken));
                if (fetchUserInfo) {
                    listener.beforeFetchUserInfo(fbToken);
                    fetchUserInfo(fbToken);
                }
            }

            @Override
            public void onCancel() {
                listener.loginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                listener.loginFailure(error);
            }
        };
    }

    @Override
    public void doLogin(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        mManager.registerCallback(mCallbackManager, mFacebookCallback);
        mManager.logInWithReadPermissions(activity, Arrays.asList("public_profile"));
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {
        final GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                final FacebookRequestError facebookRequestError = response.getError();
                if (null == facebookRequestError) {
                    try {
                        final FbUser fbUser = FbUser.parse(object);
                        mLoginListener.loginSuccess(new me.shaohui.shareutil.login.LoginResult(FB, token, fbUser));
                    } catch (JSONException e) {
                        mLoginListener.loginFailure(e);
                    }
                } else {
                    mLoginListener.loginFailure(facebookRequestError.getException());
                }
            }
        });
        request.executeAsync();
    }

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isInstall(Context context) {
        final PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        final List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.facebook.katana")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {
        mCallbackManager = null;
        mManager = null;
        mFacebookCallback = null;
    }
}
