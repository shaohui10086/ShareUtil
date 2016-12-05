package me.shaohui.shareutil.login.result;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaohui on 2016/12/3.
 */

public class QQToken extends BaseToken {

    public static QQToken parse(JSONObject jsonObject) {
        QQToken token = new QQToken();
        try {
            token.setAccessToken(jsonObject.getString("access_token"));
            token.setOpenid(jsonObject.getString("openid"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }

}
