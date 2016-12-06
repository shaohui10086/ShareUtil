package me.shaohui.shareutil.login;

import android.app.Activity;
import android.content.Intent;
import me.shaohui.shareutil.login.instance.LoginInstance;
import me.shaohui.shareutil.login.instance.QQLoginInstance;
import me.shaohui.shareutil.login.instance.WeiboLoginInstance;
import me.shaohui.shareutil.login.instance.WxLoginInstance;

/**
 * Created by shaohui on 2016/12/3.
 */

public class LoginUtil {

    private static LoginInstance mLoginInstance;

    /**
     * 调用第三方登录
     *
     * @param activity 接收回调的activity
     * @param platform 登录的平台QQ, WEIBO, WX
     */
    public static void login(Activity activity, @LoginPlatform.Platform int platform,
            LoginListener listener) {
        login(activity, platform, listener, true);
    }

    /**
     * @param fetchUserInfo 是否登录成功以后获取用户信息
     */
    public static void login(Activity activity, @LoginPlatform.Platform int platform,
            LoginListener listener, boolean fetchUserInfo) {
        switch (platform) {
            case LoginPlatform.QQ:
                mLoginInstance = new QQLoginInstance(activity, listener, fetchUserInfo);
                break;
            case LoginPlatform.WEIBO:
                mLoginInstance = new WeiboLoginInstance(activity, listener, fetchUserInfo);
                break;
            case LoginPlatform.WX:
                mLoginInstance = new WxLoginInstance(activity, listener, fetchUserInfo);
                break;
        }
        mLoginInstance.doLogin(activity, listener, fetchUserInfo);
    }

    public static void handleResult(int requestCode, int resultCode, Intent data) {
        if (mLoginInstance != null) {
            mLoginInstance.handleResult(requestCode, resultCode, data);
        }
    }

    public static void recycle() {
        if (mLoginInstance != null) {
            mLoginInstance.recycle();
        }
        mLoginInstance = null;
    }
}
