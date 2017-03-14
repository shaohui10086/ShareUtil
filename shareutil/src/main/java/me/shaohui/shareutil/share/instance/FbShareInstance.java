package me.shaohui.shareutil.share.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.List;
import java.util.Locale;

import me.shaohui.shareutil.share.ImageDecoder;
import me.shaohui.shareutil.share.ShareImageObject;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
import me.shaohui.shareutil.share.facebook.FbCallBack;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by pitt on 2017/3/13.
 */

public class FbShareInstance implements ShareInstance, FbCallBack {

    private final CallbackManager mCallbackManager;
    private final FacebookCallback<Sharer.Result> mFacebookCallback;

    public FbShareInstance(final ShareListener listener) {
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookCallback = new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                listener.shareSuccess();
            }

            @Override
            public void onCancel() {
                listener.shareCancel();
            }

            @Override
            public void onError(FacebookException error) {
                listener.shareFailure(error);
            }
        };
    }

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {

    }

    @Override
    public void shareMedia(int platform, String title, String targetUrl,
                           String summary, ShareImageObject shareImageObject, Activity activity, ShareListener listener) {
        final ShareLinkContent faceContent = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentDescription(summary)
                .setContentUrl(Uri.parse(targetUrl))
                .setImageUrl(Uri.parse(shareImageObject.getPathOrUrl()))
                .build();
        doShare(faceContent, activity, listener);
    }

    /**
     * @param shareImageObject 优先使用Bitmap，如果没有Bitmap，则使用PathOrUrl
     */
    @Override
    public void shareImage(int platform, @Nullable final ShareImageObject shareImageObject,
                           final Activity activity, final ShareListener listener) {

        if (shareImageObject.getBitmap() == null
                && shareImageObject.getPathOrUrl() == null) {
            activity.finish();
            listener.shareFailure(new IllegalArgumentException("ShareImageObject must have content."));
            return;
        }
        if (shareImageObject.getBitmap() != null) {
            final SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(shareImageObject.getBitmap())
                    .build();
            final SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();
            doShare(sharePhotoContent, activity, listener);
            return;
        }

        Observable.fromEmitter(new Action1<Emitter<String>>() {
            @Override
            public void call(Emitter<String> emitter) {
                try {
                    emitter.onNext(ImageDecoder.decode(activity, shareImageObject));
                    emitter.onCompleted();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, Emitter.BackpressureMode.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.shareRequest();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String localPath) {
                        final SharePhoto.Builder photoBuilder = new SharePhoto.Builder();
                        photoBuilder.setImageUrl(Uri.parse(String.format(Locale.ENGLISH, "%s%s", "file://", localPath)));

                        final SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photoBuilder.build())
                                .build();
                        doShare(content, activity, listener);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        activity.finish();
                        listener.shareFailure(new Exception(throwable));
                    }
                });
    }

    @Override
    public void handleResult(Intent data) {
        //do notiong
    }


    private void doShare(final ShareContent shareContent, final Activity activity, final ShareListener listener) {
        final ShareDialog dialog = new ShareDialog(activity);
        if (isInstall(activity.getApplicationContext())
                && dialog.canShow(shareContent, ShareDialog.Mode.NATIVE)) {
            dialog.registerCallback(mCallbackManager, mFacebookCallback);
            dialog.show(shareContent, ShareDialog.Mode.NATIVE);
        } else {
            listener.shareFailure(new Exception("you must install facebook first."));
            activity.finish();
        }
    }

    @Override
    public boolean isInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.facebook.katana")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {

    }

    @Override
    public boolean handleResult(int requestCode, int resultCode, Intent data) {
        return mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
