package me.shaohui.shareutil.login.result;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaohui on 2016/12/3.
 */

public class WxToken extends BaseToken {

    private String refresh_token;

    public static WxToken parse(JSONObject jsonObject) throws JSONException {
        WxToken wxToken = new WxToken();
        wxToken.setOpenid(jsonObject.getString("openid"));
        wxToken.setAccessToken(jsonObject.getString("access_token"));
        wxToken.setRefreshToken(jsonObject.getString("refresh_token"));
        return wxToken;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
