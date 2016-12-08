package me.shaohui.shareutil.login;

import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.result.BaseToken;

/**
 * Created by shaohui on 2016/12/2.
 */

public abstract class LoginListener {

    public void doLoginSuccess(LoginResult result) {
        loginSuccess(result);
        recycle();
    }

    public void doLoginFailure(Exception e) {
        loginFailure(e);
        recycle();
    }

    public void doLoginCancel() {
        loginCancel();
        recycle();
    }

    public abstract void loginSuccess(LoginResult result);

    public void beforeFetchUserInfo(BaseToken token) {}

    public abstract void loginFailure(Exception e);

    public abstract void loginCancel();

    // do something recycle
    private void recycle() {
        LoginUtil.recycle();
    }
}
