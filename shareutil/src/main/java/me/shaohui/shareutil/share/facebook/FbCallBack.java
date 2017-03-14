package me.shaohui.shareutil.share.facebook;

import android.content.Intent;

/**
 * Created by pitt on 2017/3/13.
 */

public interface FbCallBack {
    /**
     * The method that should be called from the Activity's or Fragment's onActivityResult method.
     *
     * @param requestCode The request code that's received by the Activity or Fragment.
     * @param resultCode  The result code that's received by the Activity or Fragment.
     * @param data        The result data that's received by the Activity or Fragment.
     * @return true If the result could be handled.
     */
    boolean handleResult(int requestCode, int resultCode, Intent data);
}
