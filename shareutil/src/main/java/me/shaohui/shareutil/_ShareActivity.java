package me.shaohui.shareutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shaohui on 2016/11/19.
 */

public class _ShareActivity extends Activity {

    private int mType;

    private boolean isNew;

    private static final String TYPE = "share_activity_type";

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, _ShareActivity.class);
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareLog.i("ShareActivity onCreate");
        isNew = true;

        // init data
        mType = getIntent().getIntExtra(TYPE, 0);
        if (mType == ShareUtil.TYPE) {
            // 分享
            ShareUtil.action(this);
        } else if (mType == LoginUtil.TYPE) {
            // 登录
            LoginUtil.action(this);
        } else {
            // handle 微信回调
            LoginUtil.handleResult(-1, -1, getIntent());
            ShareUtil.handleResult(getIntent());
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShareLog.i("ShareActivity onResume");
        if (isNew) {
            isNew = false;
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShareLog.i("ShareActivity onNewIntent");
        // 处理回调
        if (mType == LoginUtil.TYPE) {
            LoginUtil.handleResult(0, 0, intent);
        } else if (mType == ShareUtil.TYPE) {
            ShareUtil.handleResult(intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareLog.i("ShareActivity onActivityResult");
        // 处理回调
        if (mType == LoginUtil.TYPE) {
            LoginUtil.handleResult(requestCode, resultCode, data);
        } else if (mType == ShareUtil.TYPE) {
            ShareUtil.handleResult(data);
        }
        finish();
    }
}
