package me.shaohui.shareutil.login.result;

import com.facebook.AccessToken;

/**
 * Created by pitt on 2017/3/7.
 */

public class FbToken extends BaseToken {
    private String user_id;

    public static FbToken parse(AccessToken token) {
        final FbToken fbToken = new FbToken();
        fbToken.setAccessToken(token.getToken());
        fbToken.setUserId(token.getUserId());
        return fbToken;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}
