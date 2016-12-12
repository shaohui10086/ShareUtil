package me.shaohui.shareutil;

import android.util.Log;

/**
 * Created by shaohui on 2016/12/8.
 */

public class ShareLog {

    public static void i(String info) {
        if (ShareManager.CONFIG.isDebug()) {
            Log.i("share_util_log", info);
        }
    }

    public static void e(String error) {
        if (ShareManager.CONFIG.isDebug()) {
            Log.e("share_util_log", error);
        }
    }

}
