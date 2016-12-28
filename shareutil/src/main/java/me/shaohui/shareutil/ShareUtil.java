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
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.util.List;
import java.util.Locale;

import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.share.ShareImageObject;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
import me.shaohui.shareutil.share.instance.DefaultShareInstance;
import me.shaohui.shareutil.share.instance.QQShareInstance;
import me.shaohui.shareutil.share.instance.ShareInstance;
import me.shaohui.shareutil.share.instance.WeiboShareInstance;
import me.shaohui.shareutil.share.instance.WxShareInstance;
import static me.shaohui.shareutil.ShareLogger.INFO;

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

    public static final int TYPE = 798;

    public static ShareListener mShareListener;

    private static ShareInstance mShareInstance;

    private final static int TYPE_IMAGE = 1;
    private final static int TYPE_TEXT = 2;
    private final static int TYPE_MEDIA = 3;

    private static int mType;
    private static int mPlatform;
    private static String mText;
    private static ShareImageObject mShareImageObject;
    private static String mTitle;
    private static String mSummary;
    private static String mTargetUrl;

    static void action(Activity activity) {
        mShareInstance = getShareInstance(mPlatform, activity);

        // 防止之后调用 NullPointException
        if (mShareListener == null) {
            activity.finish();
            return;
        }

        if (!mShareInstance.isInstall(activity)) {
            mShareListener.shareFailure(new Exception(INFO.NOT_INSTALL));
            activity.finish();
            return;
        }

        switch (mType) {
            case TYPE_TEXT:
                mShareInstance.shareText(mPlatform, mText, activity, mShareListener);
                break;
            case TYPE_IMAGE:
                mShareInstance.shareImage(mPlatform, mShareImageObject, activity, mShareListener);
                break;
            case TYPE_MEDIA:
                mShareInstance.shareMedia(mPlatform, mTitle, mTargetUrl, mSummary,
                        mShareImageObject, activity, mShareListener);
                break;
        }
    }

    public static void shareText(Context context, @SharePlatform.Platform int platform, String text,
            ShareListener listener) {
        mType = TYPE_TEXT;
        mText = text;
        mPlatform = platform;
        mShareListener = buildProxyListener(listener);

        context.startActivity(_ShareActivity.newInstance(context, TYPE));
    }

    public static void shareImage(Context context, @SharePlatform.Platform final int platform,
            final String urlOrPath, ShareListener listener) {
        mType = TYPE_IMAGE;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(urlOrPath);
        mShareListener = buildProxyListener(listener);

        context.startActivity(_ShareActivity.newInstance(context, TYPE));
    }

    public static void shareImage(Context context, @SharePlatform.Platform final int platform,
            final Bitmap bitmap, ShareListener listener) {
        mType = TYPE_IMAGE;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(bitmap);
        mShareListener = buildProxyListener(listener);

        context.startActivity(_ShareActivity.newInstance(context, TYPE));
    }

    public static void shareMedia(Context context, @SharePlatform.Platform int platform,
            String title, String summary, String targetUrl, Bitmap thumb, ShareListener listener) {
        mType = TYPE_MEDIA;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(thumb);
        mSummary = summary;
        mTargetUrl = targetUrl;
        mTitle = title;
        mShareListener = buildProxyListener(listener);

        context.startActivity(_ShareActivity.newInstance(context, TYPE));
    }

    public static void shareMedia(Context context, @SharePlatform.Platform int platform,
            String title, String summary, String targetUrl, String thumbUrlOrPath,
            ShareListener listener) {
        mType = TYPE_MEDIA;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(thumbUrlOrPath);
        mSummary = summary;
        mTargetUrl = targetUrl;
        mTitle = title;
        mShareListener = buildProxyListener(listener);

        context.startActivity(_ShareActivity.newInstance(context, TYPE));
    }

    private static ShareListener buildProxyListener(ShareListener listener) {
        return new ShareListenerProxy(listener);
    }

    public static void handleResult(Intent data) {
        // 微博分享会同时回调onActivityResult和onNewIntent， 而且前者返回的intent为null
        if (mShareInstance != null && data != null) {
            mShareInstance.handleResult(data);
        } else if (data == null) {
            if (mPlatform != SharePlatform.WEIBO) {
                ShareLogger.e(INFO.HANDLE_DATA_NULL);
            }
        } else {
            ShareLogger.e(INFO.UNKNOWN_ERROR);
        }
    }

    private static ShareInstance getShareInstance(@SharePlatform.Platform int platform,
            Context context) {
        switch (platform) {
            case SharePlatform.WX:
            case SharePlatform.WX_TIMELINE:
                return new WxShareInstance(context, ShareManager.CONFIG.getWxId());
            case SharePlatform.QQ:
            case SharePlatform.QZONE:
                return new QQShareInstance(context, ShareManager.CONFIG.getQqId());
            case SharePlatform.WEIBO:
                return new WeiboShareInstance(context, ShareManager.CONFIG.getWeiboId());
            case SharePlatform.DEFAULT:
            default:
                return new DefaultShareInstance();
        }
    }

    public static void recycle() {
        mTitle = null;
        mSummary = null;
        mShareListener = null;

        // bitmap recycle
        if (mShareImageObject != null
                && mShareImageObject.getBitmap() != null
                && !mShareImageObject.getBitmap().isRecycled()) {
            mShareImageObject.getBitmap().recycle();
        }
        mShareImageObject = null;

        if (mShareInstance != null) {
            mShareInstance.recycle();
        }
        mShareInstance = null;
    }

    /**
     * 检查客户端是否安装
     */

    public static boolean isInstalled(@SharePlatform.Platform int platform, Context context) {
        switch (platform) {
            case SharePlatform.QQ:
            case SharePlatform.QZONE:
                return isQQInstalled(context);
            case SharePlatform.WEIBO:
                return isWeiBoInstalled(context);
            case SharePlatform.WX:
            case SharePlatform.WX_TIMELINE:
                return isWeiXinInstalled(context);
            case SharePlatform.DEFAULT:
                return true;
            default:
                return false;
        }
    }

    @Deprecated
    public static boolean isQQInstalled(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(Locale.getDefault()),
                    "com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static boolean isWeiBoInstalled(@NonNull Context context) {
        IWeiboShareAPI shareAPI =
                WeiboShareSDK.createWeiboAPI(context, ShareManager.CONFIG.getWeiboId());
        return shareAPI.isWeiboAppInstalled();
    }

    @Deprecated
    public static boolean isWeiXinInstalled(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, ShareManager.CONFIG.getWxId(), true);
        return api.isWXAppInstalled();
    }
    
    private static class ShareListenerProxy extends ShareListener {

        private final ShareListener mShareListener;

        ShareListenerProxy(ShareListener listener) {
            mShareListener = listener;
        }

        @Override
        public void shareSuccess() {
            ShareLogger.i(INFO.SHARE_SUCCESS);
            ShareUtil.recycle();
            mShareListener.shareSuccess();
        }

        @Override
        public void shareFailure(Exception e) {
            ShareLogger.i(INFO.SHARE_FAILURE);
            ShareUtil.recycle();
            mShareListener.shareFailure(e);
        }

        @Override
        public void shareCancel() {
            ShareLogger.i(INFO.SHARE_CANCEL);
            ShareUtil.recycle();
            mShareListener.shareCancel();
        }

        @Override
        public void shareRequest() {
            ShareLogger.i(INFO.SHARE_REQUEST);
            mShareListener.shareRequest();
        }
    }
}
