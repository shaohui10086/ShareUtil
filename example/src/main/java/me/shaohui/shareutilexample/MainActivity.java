package me.shaohui.shareutilexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 定义ID以及Listener
        ShareUtil.setQQId("QQ_xxxx");
        ShareUtil.setWXId("WX_xxxx");
        ShareUtil.setWeiboId("WB_xxxx");
        ShareUtil.setShareListener(new ShareListener() {
            @Override
            public void shareSuccess() {Log.i("share", "share_success");
            }

            @Override
            public void shareFailure() {Log.i("share", "share_failure");
            }

            @Override
            public void shareCancel() {Log.i("share", "share_cancel");
            }
        });

    }

    private void shareAction() {
        ShareUtil shareUtil = ShareUtil.newInstance(this);

        shareUtil.shareText(SharePlatform.WX, "分享文字");

        shareUtil.shareImage(SharePlatform.QQ, "分享图片链接");

        shareUtil.shareMedia(SharePlatform.QZONE, "分享title", "分享简洁", "分享的链接", "分享缩略图链接");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShareUtil.handleWeiboResponse(this, intent);   // 用于处理微博分享的回调
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.handleQQResult(data);    // 用于处理QQ分享的回调
    }
}
