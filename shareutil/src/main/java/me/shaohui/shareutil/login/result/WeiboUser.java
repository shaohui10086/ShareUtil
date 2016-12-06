package me.shaohui.shareutil.login.result;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaohui on 2016/12/1.
 */

public class WeiboUser extends BaseUser {

    private String language;

    private String city;

    private String province;

    private String headimgurl_hd;

    /**
     * 示例
     * {
     *     "id": 1404376560,
     *     "screen_name": "zaku",
     *     "name": "zaku",
     *     "province": "11",
     *     "city": "5",
     *     "location": "北京 朝阳区",
     *     "description": "人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。",
     *     "url": "http://blog.sina.com.cn/zaku",
     *     "profile_image_url": "http://tp1.sinaimg.cn/1404376560/50/0/1",
     *     "domain": "zaku",
     *     "gender": "m",
     *     "followers_count": 1204,
     *     "friends_count": 447,
     *     "statuses_count": 2908,
     *     "favourites_count": 0,
     *     "created_at": "Fri Aug 28 00:00:00 +0800 2009",
     *     "following": false,
     *     "allow_all_act_msg": false,
     *     "geo_enabled": true,
     *     "verified": false,
     *     "status": {
     *         "created_at": "Tue May 24 18:04:53 +0800 2011",
     *         "id": 11142488790,
     *         "text": "我的相机到了。",
     *         "favorited": false,
     *         "truncated": false,
     *         "in_reply_to_status_id": "",
     *         "in_reply_to_user_id": "",
     *         "in_reply_to_screen_name": "",
     *         "geo": null,
     *         "mid": "5610221544300749636",
     *         "annotations": [],
     *         "reposts_count": 5,
     *         "comments_count": 8
     *     },
     *     "allow_all_comment": true,
     *     "avatar_large": "http://tp1.sinaimg.cn/1404376560/180/0/1",
     *     "verified_reason": "",
     *     "follow_me": false,
     *     "online_status": 0,
     *     "bi_followers_count": 215
     * }
     */

    public static WeiboUser parse(JSONObject jsonObject) throws JSONException {
        WeiboUser user = new WeiboUser();
        user.setOpenId(String.valueOf(jsonObject.getInt("id")));
        user.setNickname(jsonObject.getString("screen_name"));

        // 性别
        String gender = jsonObject.getString("gender");
        if (TextUtils.equals(gender, "m")) {
            user.setSex(1);
        } else if (TextUtils.equals(gender, "f")) {
            user.setSex(2);
        } else {
            user.setSex(0);
        }

        user.setHeadImageUrl(jsonObject.getString("profile_image_url"));
        user.setHeadImageUrlLarge(jsonObject.getString("avatar_large"));
        user.setHeadimgurl_hd(jsonObject.getString("avatar_hd"));
        user.setCity(jsonObject.getString("city"));
        user.setProvince(jsonObject.getString("province"));

        return user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHeadimgurl_hd() {
        return headimgurl_hd;
    }

    public void setHeadimgurl_hd(String headimgurl_hd) {
        this.headimgurl_hd = headimgurl_hd;
    }
}
