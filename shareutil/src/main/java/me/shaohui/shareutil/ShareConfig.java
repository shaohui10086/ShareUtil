package me.shaohui.shareutil;

/**
 * Created by shaohui on 2016/12/7.
 */

public class ShareConfig {

    private String wxId;

    private String wxSecret;

    private String qqId;

    private String weiboId;

    private String weiboRedirectUrl = "https://api.weibo.com/oauth2/default.html";

    private String weiboScope = "email";

    private boolean debug;

    public static ShareConfig instance() {
        return new ShareConfig();
    }

    public ShareConfig wxId(String id) {
        wxId = id;
        return this;
    }

    public ShareConfig wxSecret(String id) {
        wxSecret = id;
        return this;
    }

    public ShareConfig qqId(String id) {
        qqId = id;
        return this;
    }

    public ShareConfig weiboId(String id) {
        weiboId = id;
        return this;
    }

    public ShareConfig weiboRedirectUrl(String url) {
        weiboRedirectUrl = url;
        return this;
    }

    public ShareConfig weiboScope(String scope) {
        weiboScope = scope;
        return this;
    }

    public ShareConfig debug(boolean isDebug) {
        debug = isDebug;
        return this;
    }

    public String getWxId() {
        return wxId;
    }

    public String getWxSecret() {
        return wxSecret;
    }

    public String getQqId() {
        return qqId;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public String getWeiboRedirectUrl() {
        return weiboRedirectUrl;
    }

    public String getWeiboScope() {
        return weiboScope;
    }

    public boolean isDebug() {
        return debug;
    }
}
