package me.shaohui.shareutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.login.LoginUtil;

/**
 * Created by shaohui on 2016/11/19.
 */

public class WXEntryParent extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp && baseResp.getType() == 1) {
            LoginUtil.handleWxResult((SendAuth.Resp) baseResp);
        } else if (ShareUtil.mShareListener != null) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    ShareUtil.mShareListener.shareFailure();
                    break;
                case BaseResp.ErrCode.ERR_OK:
                    ShareUtil.mShareListener.shareSuccess();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ShareUtil.mShareListener.shareCancel();
            }
        }
        finish();
    }
}
