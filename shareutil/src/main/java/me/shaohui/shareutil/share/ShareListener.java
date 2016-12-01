package me.shaohui.shareutil.share;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by shaohui on 2016/11/18.
 */

public abstract class ShareListener implements IUiListener, IWeiboHandler.Response {
    @Override
    public void onComplete(Object o) {
        shareSuccess();
    }

    @Override
    public void onError(UiError uiError) {
        shareFailure();
    }

    @Override
    public void onCancel() {
        shareCancel();
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                shareSuccess();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                shareFailure();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                shareCancel();
                break;
        }
    }

    public abstract void shareSuccess();

    public abstract void shareFailure();

    public abstract void shareCancel();

    // 用于缓解用户焦虑
    public void shareRequest() {

    }
}
