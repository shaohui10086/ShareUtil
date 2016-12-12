package com.younglive.livestreaming;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.result.BaseToken;

public class MainActivity extends AppCompatActivity {

    String APP_ID = "XXXXXX";
    String APP_SECRET = "XXXXXXX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShareConfig config = ShareConfig.instance().wxId(APP_ID).wxSecret(APP_SECRET);
        ShareManager.init(config);

        findViewById(R.id.action_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUtil.login(MainActivity.this, LoginPlatform.WX, new LoginListener() {
                    @Override
                    public void loginSuccess(LoginResult result) {
                        Log.i("TAG", result.getUserInfo().getNickname());
                        Log.i("TAG", "登录成功");
                    }

                    @Override
                    public void beforeFetchUserInfo(BaseToken token) {
                        Log.i("TAG", "获取用户信息");
                    }

                    @Override
                    public void loginFailure(Exception e) {
                        e.printStackTrace();
                        Log.i("TAG", "登录失败");
                    }

                    @Override
                    public void loginCancel() {
                        Log.i("TAG", "登录取消");
                    }
                });
            }
        });
    }
}
