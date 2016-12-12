package me.shaohui.shareutil.share;

import android.util.Log;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import me.shaohui.shareutil.ShareLog;
import me.shaohui.shareutil.ShareUtil;

/**
 * Created by shaohui on 2016/11/18.
 */

public abstract class ShareListener implements IUiListener, IWeiboHandler.Response {
    @Override
    public void onComplete(Object o) {
        doShareSuccess();
    }

    @Override
    public void onError(UiError uiError) {
        doShareFailure(new Exception(uiError == null ? "QQ share failed" : uiError.errorDetail));
    }

    @Override
    public void onCancel() {
        doShareCancel();
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                doShareSuccess();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                doShareFailure(new Exception(baseResponse.errMsg));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                doShareCancel();
                break;
            default:
                doShareFailure(new Exception(baseResponse.errMsg));
        }
    }

    public void doShareSuccess() {
        ShareLog.i("share success");
        ShareUtil.recycle();
        shareSuccess();
    }

    public void doShareFailure(Exception e) {
        ShareLog.e("share failed");
        ShareUtil.recycle();
        shareFailure(e);
    }

    public void doShareCancel() {
        ShareLog.i("share cancel");
        ShareUtil.recycle();
        shareCancel();
    }

    public abstract void shareSuccess();

    public abstract void shareFailure(Exception e);

    public abstract void shareCancel();

    // 用于缓解用户焦虑
    public void shareRequest() {

    }
}
