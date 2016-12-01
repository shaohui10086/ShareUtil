package me.shaohui.shareutil.share.share_instance;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;
import me.shaohui.shareutil.R;
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

public class DefaultShareInstance implements ShareInstance {

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent,
                activity.getResources().getString(R.string.vista_share_title)));
    }

    @Override
    public void shareMedia(int platform, String title, String targetUrl, String summary,
            ShareImageObject shareImageObject, Activity activity, ShareListener listener) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s", title, targetUrl));
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent,
                activity.getResources().getString(R.string.vista_share_title)));
    }

    @Override
    public void shareImage(int platform, final ShareImageObject shareImageObject,
            final Activity activity, final ShareListener listener) {
        Observable.fromEmitter(new Action1<Emitter<Uri>>() {
            @Override
            public void call(Emitter<Uri> emitter) {
                if (!TextUtils.isEmpty(shareImageObject.getPathOrUrl())) {
                    String path = ImageDecoder.decode(activity, shareImageObject.getPathOrUrl());
                    emitter.onNext(Uri.fromFile(new File(path)));
                    emitter.onCompleted();
                } else if (shareImageObject.getBitmap() != null) {
                    String path = ImageDecoder.decode(activity, shareImageObject.getBitmap());
                    emitter.onNext(Uri.fromFile(new File(path)));
                    emitter.onCompleted();
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
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("image/jpeg");
                        activity.startActivity(Intent.createChooser(shareIntent,
                                activity.getResources().getText(R.string.vista_share_title)));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        listener.shareFailure();
                    }
                });
    }
}
