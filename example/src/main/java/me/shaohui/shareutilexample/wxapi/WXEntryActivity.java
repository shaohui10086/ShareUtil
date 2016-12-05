package me.shaohui.shareutilexample.wxapi;

import com.tencent.mm.sdk.modelbase.BaseResp;
import me.shaohui.shareutil.WXEntryParent;

/**
 * Created by shaohui on 2016/12/1.
 */

public class WXEntryActivity extends WXEntryParent {

    @Override
    public void onResp(BaseResp baseResp) {

        // do anything you want

        super.onResp(baseResp);         // 处理微信分享回调
    }
}
