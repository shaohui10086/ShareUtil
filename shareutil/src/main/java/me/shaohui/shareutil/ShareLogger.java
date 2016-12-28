package me.shaohui.shareutil;

import android.util.Log;

/**
 * Created by shaohui on 2016/12/8.
 */

public class ShareLogger {

    private static final String TAG = "share_util_log";

    public static void i(String info) {
        if (ShareManager.CONFIG.isDebug()) {
            Log.i(TAG, info);
        }
    }

    public static void e(String error) {
        if (ShareManager.CONFIG.isDebug()) {
            Log.e(TAG, error);
        }
    }

    public static class INFO {
        public static final String SHARE_SUCCESS = "call share success";
        public static final String SHARE_FAILURE = "call share failure";
        public static final String SHARE_CANCEL = "call share cancel";
        public static final String SHARE_REQUEST = "call share request";

        // for share
        public static final String HANDLE_DATA_NULL = "Handle the result, but the data is null, please check you app id";
        public static final String UNKNOWN_ERROR = "Unknown error";
        public static final String NOT_INSTALL = "The application is not install";
        public static final String DEFAULT_QQ_SHARE_ERROR = "QQ share failed";
        public static final String QQ_NOT_SUPPORT_SHARE_TXT = "QQ not support share text";
        public static final String IMAGE_FETCH_ERROR = "Image fetch error";
        public static final String SD_CARD_NOT_AVAILABLE = "The sd card is not available";

        // for login
        public static final String LOGIN_SUCCESS = "call login success";
        public static final String LOGIN_FAIl = "call login failed";
        public static final String LOGIN_CANCEL = "call login cancel";
        public static final String LOGIN_AUTH_SUCCESS = "call before fetch user info";
        public static final String ILLEGAL_TOKEN = "Illegal token, please check your config";
        public static final String QQ_LOGIN_ERROR = "QQ login error";
        public static final String QQ_AUTH_SUCCESS = "QQ auth success";
        public static final String WEIBO_AUTH_ERROR = "weibo auth error";
        public static final String UNKNOW_PLATFORM = "unknown platform";

        public static final String WX_ERR_SENT_FAILED = "Wx sent failed";
        public static final String WX_ERR_UNSUPPORT = "Wx UnSupport";
        public static final String WX_ERR_AUTH_DENIED = "Wx auth denied";
        public static final String WX_ERR_AUTH_ERROR = "Wx auth error";

        public static final String AUTH_CANCEL = "auth cancel";
        public static final String FETCH_USER_INOF_ERROR = "Fetch user info error";

        // for shareActivity
        public static final String ACTIVITY_CREATE = "ShareActivity onCreate";
        public static final String ACTIVITY_RESUME = "ShareActivity onResume";
        public static final String ACTIVITY_RESULT = "ShareActivity onActivityResult";
        public static final String ACTIVITY_NEW_INTENT = "ShareActivity onNewIntent";

    }

}
