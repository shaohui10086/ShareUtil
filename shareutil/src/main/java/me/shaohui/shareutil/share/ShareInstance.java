package me.shaohui.shareutil.share;

import android.app.Activity;

/**
 * Created by shaohui on 2016/11/18.
 */

public interface ShareInstance {

    //void init(Context context, String appId);

    void shareText(int platform, String text, Activity activity, ShareListener listener);

    void shareMedia(int platform, String title, String targetUrl, String summary,
            ShareImageObject shareImageObject, Activity activity, ShareListener listener);

    void shareImage(int platform, ShareImageObject shareImageObject, Activity activity,
            ShareListener listener);
}
