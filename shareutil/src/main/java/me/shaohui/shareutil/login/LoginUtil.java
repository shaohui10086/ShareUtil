package me.shaohui.shareutil.login;

import android.app.Activity;
import android.content.Intent;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import me.shaohui.shareutil.login.instance.LoginInstance;
import me.shaohui.shareutil.login.instance.QQLoginInstance;
import me.shaohui.shareutil.login.instance.WeiboLoginInstance;
import me.shaohui.shareutil.login.instance.WxLoginInstance;

/**
 * Created by shaohui on 2016/12/3.
 */

public class LoginUtil {

    private static LoginInstance mLoginInstance;

    public static void login(Activity activity, int platform, LoginListener listener) {
        login(activity, platform, listener, true);
    }

    public static void login(Activity activity, int platform, LoginListener listener, boolean fetchUserInfo) {
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
        mLoginInstance.handleResult(requestCode, resultCode, data);
    }

    public static void handleWxResult(SendAuth.Resp resp) {
        if (mLoginInstance instanceof WxLoginInstance) {
            mLoginInstance.handleWxResult(resp);
        }
    }


}
