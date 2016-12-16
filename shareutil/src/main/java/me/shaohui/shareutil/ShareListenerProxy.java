package me.shaohui.shareutil;

import me.shaohui.shareutil.share.ShareListener;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/12/15.
 */

class ShareListenerProxy extends ShareListener {

    private final ShareListener mShareListener;

    public ShareListenerProxy(ShareListener listener) {
        mShareListener = listener;
    }

    @Override
    public void shareSuccess() {
        ShareLogger.i(INFO.SHARE_SUCCESS);
        ShareUtil.recycle();
        mShareListener.shareSuccess();
    }

    @Override
    public void shareFailure(Exception e) {
        ShareLogger.i(INFO.SHARE_FAILURE);
        ShareUtil.recycle();
        mShareListener.shareFailure(e);
    }

    @Override
    public void shareCancel() {
        ShareLogger.i(INFO.SHARE_CANCEL);
        ShareUtil.recycle();
        mShareListener.shareCancel();
    }

    @Override
    public void shareRequest() {
        ShareLogger.i(INFO.SHARE_REQUEST);
        mShareListener.shareRequest();
    }
}
