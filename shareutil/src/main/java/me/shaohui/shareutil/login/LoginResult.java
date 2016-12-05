package me.shaohui.shareutil.login;

import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;

/**
 * Created by shaohui on 2016/12/3.
 */

public class LoginResult {

    private BaseToken mToken;

    private BaseUser mUserInfo;

    public LoginResult(BaseToken token) {
        mToken = token;
    }

    public LoginResult(BaseToken token, BaseUser userInfo) {
        mToken = token;
        mUserInfo = userInfo;
    }

    public BaseToken getToken() {
        return mToken;
    }

    public void setToken(BaseToken token) {
        mToken = token;
    }

    public BaseUser getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(BaseUser userInfo) {
        mUserInfo = userInfo;
    }
}
