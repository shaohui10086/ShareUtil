package me.shaohui.shareutil.login.result;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pitt on 2017/3/7.
 */

public class FbUser extends BaseUser {
    public static FbUser parse(final JSONObject jsonObject) throws JSONException {
        final FbUser fbUser = new FbUser();
        fbUser.setSex(TextUtils.equals("male", jsonObject.getString("gender")) ? 1 : 2);
        fbUser.setHeadImageUrl(jsonObject.getString("profile"));
        return fbUser;
    }
}
