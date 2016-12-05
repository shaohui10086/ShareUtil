package me.shaohui.shareutil.login.result;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaohui on 2016/12/1.
 */

public class WxUser extends BaseUser {

    private String city;

    private String country;

    private String province;

    private String unionid;

    public static WxUser parse(JSONObject jsonObject) throws JSONException {
        WxUser user = new WxUser();
        user.setOpenId(jsonObject.getString("openid"));
        user.setNickname(jsonObject.getString("nickname"));
        user.setSex(jsonObject.getInt("sex"));
        user.setHeadImageUrl(jsonObject.getString("headimgurl"));
        user.setHeadImageUrlLarge(jsonObject.getString("headimgurl"));       // 重复
        user.setProvince(jsonObject.getString("province"));
        user.setCity(jsonObject.getString("city"));
        user.setCountry(jsonObject.getString("country"));
        user.setUnionid(jsonObject.getString("unionid"));

        return user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
