package me.shaohui.shareutil.login.result;

import com.facebook.AccessToken;

/**
 * Created by pitt on 2017/3/7.
 */

public class FbToken extends BaseToken {

    public static FbToken parse(AccessToken token) {
        final FbToken fbToken = new FbToken();
        fbToken.setAccessToken(token.getToken());
        //TODO add OpenId
        fbToken.setOpenid(token.getUserId());
        return fbToken;
    }
}
