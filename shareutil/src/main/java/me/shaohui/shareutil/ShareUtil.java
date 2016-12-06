package me.shaohui.shareutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import java.lang.ref.WeakReference;
import java.util.List;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.instance.WeiboLoginInstance;
import me.shaohui.shareutil.share.ShareImageObject;
import me.shaohui.shareutil.share.share_instance.ShareInstance;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
import me.shaohui.shareutil.share.share_instance.DefaultShareInstance;
import me.shaohui.shareutil.share.share_instance.QQShareInstance;
import me.shaohui.shareutil.share.share_instance.WeiboShareInstance;
import me.shaohui.shareutil.share.share_instance.WxShareInstance;

/**
 * Created by shaohui on 2016/11/18.
 */

public class ShareUtil {
    /**
     * 测试case
     *
     * 1. 本地图片 vs 网络图片
     * 2. 图片大小限制
     * 3. 文字长度限制
     */

    private WeakReference<Activity> mActivityWeakReference;

    public static ShareListener mShareListener;

    /**
     * VistaShareUtil 初始化
     */

    public static void setShareListener(ShareListener listener) {
        mShareListener = listener;
    }

    public static ShareListener getShareListener() {
        return mShareListener;
    }

    private ShareUtil(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
    }

    public static ShareUtil newInstance(Activity activity) {
        return new ShareUtil(activity);
    }

    public void shareText(@SharePlatform.Platform int platform, String text) {
        if (mActivityWeakReference.get() == null) {
            return;
        }

        ShareInstance instance = getShareInstance(platform, mActivityWeakReference.get());
        instance.shareText(platform, text, mActivityWeakReference.get(), mShareListener);
    }

    public void shareImage(@SharePlatform.Platform final int platform, final String urlOrPath) {
        if (mActivityWeakReference.get() == null) {
            return;
        }

        ShareInstance instance = getShareInstance(platform, mActivityWeakReference.get());

        ShareImageObject imageObject = new ShareImageObject(urlOrPath);
        instance.shareImage(platform, imageObject, mActivityWeakReference.get(), mShareListener);
    }

    public void shareImage(@SharePlatform.Platform final int platform, final Bitmap bitmap) {
        if (mActivityWeakReference.get() == null) {
            return;
        }

        ShareInstance instance = getShareInstance(platform, mActivityWeakReference.get());

        ShareImageObject imageObject = new ShareImageObject(bitmap);
        instance.shareImage(platform, imageObject, mActivityWeakReference.get(), mShareListener);
    }

    public void shareMedia(@SharePlatform.Platform int platform, String title, String summary,
            String targetUrl, Bitmap thumb) {
        ShareInstance instance = getShareInstance(platform, mActivityWeakReference.get());
        ShareImageObject imageObject = new ShareImageObject(thumb);
        instance.shareMedia(platform, title, targetUrl, summary, imageObject,
                mActivityWeakReference.get(), mShareListener);
    }

    public void shareMedia(@SharePlatform.Platform int platform, String title, String summary,
            String targetUrl, String thumbUrlOrPath) {
        ShareInstance instance = getShareInstance(platform, mActivityWeakReference.get());
        ShareImageObject imageObject = new ShareImageObject(thumbUrlOrPath);
        instance.shareMedia(platform, title, targetUrl, summary, imageObject,
                mActivityWeakReference.get(), mShareListener);
    }

    public static void handleWeiboResponse(Context context, Intent intent) {
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, ShareManager.WEIBO_ID);
        mWeiboShareAPI.registerApp();
        mWeiboShareAPI.handleWeiboResponse(intent, mShareListener);
    }

    public static void handleQQResult(Intent data) {
        Tencent.handleResultData(data, mShareListener);
    }

    private ShareInstance getShareInstance(@SharePlatform.Platform int platform, Context context) {
        switch (platform) {
            case SharePlatform.WX:
            case SharePlatform.WX_TIMELINE:
                return new WxShareInstance(context, ShareManager.WX_ID);
            case SharePlatform.QQ:
            case SharePlatform.QZONE:
                return new QQShareInstance(context, ShareManager.QQ_ID);
            case SharePlatform.WEIBO:
                return new WeiboShareInstance(context, ShareManager.WEIBO_ID);
            case SharePlatform.DEFAULT:
            default:
                return new DefaultShareInstance();
        }
    }

    /**
     * 检查客户端是否安装
     */

    public static boolean isQQInstalled(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeiBoInstalled(@NonNull Context context) {
        IWeiboShareAPI shareAPI = WeiboShareSDK.createWeiboAPI(context, ShareManager.WEIBO_ID);
        return shareAPI.isWeiboAppInstalled();
    }

    public static boolean isWeiXinInstalled(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, ShareManager.WX_ID, true);
        return api.isWXAppInstalled();
    }
}
