package me.shaohui.shareutil.share.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Pair;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboResponse;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ImageDecoder;
import me.shaohui.shareutil.share.ShareImageObject;
import me.shaohui.shareutil.share.ShareListener;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shaohui on 2016/11/18.
 */

public class WeiboShareInstance implements ShareInstance {
    /**
     * 微博分享限制thumb image必须小于2097152，否则点击分享会没有反应
     */

    IWeiboShareAPI mWeiboShareAPI;

    private static final int TARGET_SIZE = 800;

    public WeiboShareInstance(Context context, String appId) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, appId);
        mWeiboShareAPI.registerApp();
    }

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {
        TextObject textObject = new TextObject();
        textObject.text = text;

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = textObject;

        sendRequest(activity, message);
    }

    @Override
    public void shareMedia(int platform, final String title, final String targetUrl, String summary,
            ShareImageObject shareImageObject, final Activity activity,
            final ShareListener listener) {
        String content = String.format("%s %s", title, targetUrl);
        shareTextOrImage(shareImageObject, content, activity, listener);
    }

    @Override
    public void shareImage(int platform, ShareImageObject shareImageObject, Activity activity,
            ShareListener listener) {
        shareTextOrImage(shareImageObject, null, activity, listener);
    }

    @Override
    public void handleResult(Intent intent) {
        SendMessageToWeiboResponse baseResponse =
                new SendMessageToWeiboResponse(intent.getExtras());

        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                ShareUtil.mShareListener.shareSuccess();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                ShareUtil.mShareListener.shareFailure(new Exception(baseResponse.errMsg));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                ShareUtil.mShareListener.shareCancel();
                break;
            default:
                ShareUtil.mShareListener.shareFailure(new Exception(baseResponse.errMsg));
        }
    }

    @Override
    public boolean isInstall(Context context) {
        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    @Override
    public void recycle() {
        mWeiboShareAPI = null;
    }

    private void shareTextOrImage(final ShareImageObject shareImageObject, final String text,
            final Activity activity, final ShareListener listener) {

        Observable.fromEmitter(new Action1<Emitter<Pair<String, byte[]>>>() {
            @Override
            public void call(Emitter<Pair<String, byte[]>> emitter) {
                if (!TextUtils.isEmpty(shareImageObject.getPathOrUrl())) {
                    String path = ImageDecoder.decode(activity, shareImageObject.getPathOrUrl());
                    Bitmap bitmap = ImageDecoder.compress(path, TARGET_SIZE, TARGET_SIZE);

                    emitter.onNext(Pair.create(path, ImageDecoder.bmp2ByteArray(bitmap)));
                    bitmap.recycle();
                    emitter.onCompleted();
                } else if (shareImageObject.getBitmap() != null) {
                    String path = ImageDecoder.decode(activity, shareImageObject.getBitmap());
                    if (!TextUtils.isEmpty(path)) {
                        Bitmap bitmap = ImageDecoder.compress(path, TARGET_SIZE, TARGET_SIZE);

                        emitter.onNext(Pair.create(path, ImageDecoder.bmp2ByteArray(bitmap)));
                        bitmap.recycle();
                        emitter.onCompleted();
                    } else {
                        emitter.onError(new IllegalArgumentException());
                    }
                } else {
                    emitter.onError(new IllegalArgumentException());
                }
            }
        }, Emitter.BackpressureMode.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.shareRequest();
                    }
                })
                .subscribe(new Action1<Pair<String, byte[]>>() {
                    @Override
                    public void call(Pair<String, byte[]> pair) {
                        ImageObject imageObject = new ImageObject();
                        imageObject.imageData = pair.second;
                        imageObject.imagePath = pair.first;

                        WeiboMultiMessage message = new WeiboMultiMessage();
                        message.imageObject = imageObject;
                        if (!TextUtils.isEmpty(text)) {
                            TextObject textObject = new TextObject();
                            textObject.text = text;

                            message.textObject = textObject;
                        }

                        sendRequest(activity, message);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startFailed(activity, listener, new Exception(throwable));
                    }
                });
    }

    private void sendRequest(Activity activity, WeiboMultiMessage message) {
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = message;
        mWeiboShareAPI.sendRequest(activity, request);
    }

    private void startFailed(Activity activity, ShareListener listener, Exception e) {
        activity.finish();
        listener.shareFailure(e);
    }
}
