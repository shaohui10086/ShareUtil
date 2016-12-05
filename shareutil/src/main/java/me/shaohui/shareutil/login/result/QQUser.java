package me.shaohui.shareutil.login.result;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaohui on 2016/12/1.
 */

public class QQUser extends BaseUser {

    private String qZoneHeadImage;

    private String qZoneHeadImageLarge;

    public static QQUser parse(String openId, JSONObject jsonObject) throws JSONException {
        QQUser user = new QQUser();
        user.setNickname(jsonObject.getString("nickname"));
        user.setOpenId(openId);
        user.setSex(TextUtils.equals("男", jsonObject.getString("gender")) ? 1 : 2);
        user.setHeadImageUrl(jsonObject.getString("figureurl_qq_1"));
        user.setHeadImageUrlLarge(jsonObject.getString("figureurl_qq_2"));
        user.setqZoneHeadImage(jsonObject.getString("figureurl_1"));
        user.setqZoneHeadImageLarge(jsonObject.getString("figureurl_2"));

        return user;
    }

    public String getqZoneHeadImage() {
        return qZoneHeadImage;
    }

    public void setqZoneHeadImage(String qZoneHeadImage) {
        this.qZoneHeadImage = qZoneHeadImage;
    }

    public String getqZoneHeadImageLarge() {
        return qZoneHeadImageLarge;
    }

    public void setqZoneHeadImageLarge(String qZoneHeadImageLarge) {
        this.qZoneHeadImageLarge = qZoneHeadImageLarge;
    }
}
