package com.younglive.livestreaming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        findViewById(R.id.action_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareBottomDialog dialog = new ShareBottomDialog();
                dialog.show(getSupportFragmentManager());
            }
        });

        findViewById(R.id.action_share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareImage(ShareActivity.this, SharePlatform.QQ,
                        "http://shaohui.me/images/avatar.gif", new ShareListener() {
                            @Override
                            public void shareSuccess() {
                                Log.i("TAG", "分享成功");
                            }

                            @Override
                            public void shareFailure(Exception e) {
                                Log.i("TAG", "分享失败");
                            }

                            @Override
                            public void shareCancel() {
                                Log.i("TAG", "分享取消");
                            }
                        });
            }
        });

        // 初始化shareUtil
        ShareConfig config = ShareConfig.instance()
                .qqId("XXXXXX")
                .weiboId("XXXXXX")
                .wxId("xXXXXX");
        ShareManager.init(config);
    }
}
