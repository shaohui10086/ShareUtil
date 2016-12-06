package me.shaohui.shareutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import me.shaohui.shareutil.login.LoginUtil;

/**
 * Created by shaohui on 2016/11/19.
 */

public class WXEntryParent extends Activity {

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIWXAPI = WXAPIFactory.createWXAPI(this, ShareManager.WX_ID);

        mIWXAPI.handleIntent(getIntent(), new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {
                if (baseResp instanceof SendAuth.Resp && baseResp.getType() == 1) {
                    LoginUtil.handleResult(0, 0, getIntent());
                } else if (ShareUtil.mShareListener != null) {
                    switch (baseResp.errCode) {
                        case BaseResp.ErrCode.ERR_SENT_FAILED:
                            ShareUtil.mShareListener.shareFailure(
                                    new Exception("weixin share error"));
                            break;
                        case BaseResp.ErrCode.ERR_OK:
                            ShareUtil.mShareListener.shareSuccess();
                            break;
                        case BaseResp.ErrCode.ERR_USER_CANCEL:
                            ShareUtil.mShareListener.shareCancel();
                    }
                }
                finish();
                mIWXAPI.detach();
            }
        });
    }
}
