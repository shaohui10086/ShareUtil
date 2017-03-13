package me.shaohui.shareutil;

import android.content.Context;
import com.facebook.FacebookSdk;
/**
 * Created by shaohui on 2016/12/5.
 */

public class ShareManager {

    private static boolean isInit = false;

    public static ShareConfig CONFIG;

    public static void init(final Context context, ShareConfig config) {
        isInit = true;
        CONFIG = config;
        FacebookSdk.sdkInitialize(context.getApplicationContext());
    }
}
