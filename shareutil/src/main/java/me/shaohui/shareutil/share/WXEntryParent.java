package me.shaohui.shareutil.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import me.shaohui.shareutil.VistaShareUtil;

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
        if (VistaShareUtil.mShareListener != null) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    VistaShareUtil.mShareListener.shareFailure();
                    break;
                case BaseResp.ErrCode.ERR_OK:
                    VistaShareUtil.mShareListener.shareSuccess();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    VistaShareUtil.mShareListener.shareCancel();
            }
        }
        finish();
    }
}
