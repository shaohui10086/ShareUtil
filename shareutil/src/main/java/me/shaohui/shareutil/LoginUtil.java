package me.shaohui.shareutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.instance.LoginInstance;
import me.shaohui.shareutil.login.instance.QQLoginInstance;
import me.shaohui.shareutil.login.instance.WeiboLoginInstance;
import me.shaohui.shareutil.login.instance.WxLoginInstance;
import me.shaohui.shareutil.login.result.BaseToken;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/12/3.
 */

public class LoginUtil {

    private static LoginInstance mLoginInstance;

    private static LoginListener mLoginListener;

    private static int mPlatform;

    private static boolean isFetchUserInfo;

    static final int TYPE = 799;

    public static void login(Context context, @LoginPlatform.Platform int platform,
            LoginListener listener) {
        login(context, platform, listener, true);
    }

    public static void login(Context context, @LoginPlatform.Platform int platform,
            LoginListener listener, boolean fetchUserInfo) {
        mPlatform = platform;
        mLoginListener = new LoginListenerProxy(listener);
        isFetchUserInfo = fetchUserInfo;
        context.startActivity(_ShareActivity.newInstance(context, TYPE));
    }

    static void action(Activity activity) {
        switch (mPlatform) {
            case LoginPlatform.QQ:
                mLoginInstance = new QQLoginInstance(activity, mLoginListener, isFetchUserInfo);
                break;
            case LoginPlatform.WEIBO:
                mLoginInstance = new WeiboLoginInstance(activity, mLoginListener, isFetchUserInfo);
                break;
            case LoginPlatform.WX:
                mLoginInstance = new WxLoginInstance(activity, mLoginListener, isFetchUserInfo);
                break;
            default:
                mLoginListener.loginFailure(new Exception(INFO.UNKNOW_PLATFORM));
                activity.finish();
        }
        mLoginInstance.doLogin(activity, mLoginListener, isFetchUserInfo);
    }

    static void handleResult(int requestCode, int resultCode, Intent data) {
        if (mLoginInstance != null) {
            mLoginInstance.handleResult(requestCode, resultCode, data);
        }
    }

    public static void recycle() {
        if (mLoginInstance != null) {
            mLoginInstance.recycle();
        }
        mLoginInstance = null;
        mLoginListener = null;
        mPlatform = 0;
        isFetchUserInfo = false;
    }

    private static class LoginListenerProxy extends LoginListener {

        private LoginListener mListener;

        LoginListenerProxy(LoginListener listener) {
            mListener = listener;
        }

        @Override
        public void loginSuccess(LoginResult result) {
            ShareLogger.i(INFO.LOGIN_SUCCESS);
            mListener.loginSuccess(result);
            recycle();
        }

        @Override
        public void loginFailure(Exception e) {
            ShareLogger.i(INFO.LOGIN_FAIl);
            mListener.loginFailure(e);
            recycle();
        }

        @Override
        public void loginCancel() {
            ShareLogger.i(INFO.LOGIN_CANCEL);
            mListener.loginCancel();
            recycle();
        }

        @Override
        public void beforeFetchUserInfo(BaseToken token) {
            ShareLogger.i(INFO.LOGIN_AUTH_SUCCESS);
            mListener.beforeFetchUserInfo(token);
        }
    }
}
