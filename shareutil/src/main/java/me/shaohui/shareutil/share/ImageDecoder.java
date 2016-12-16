package me.shaohui.shareutil.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by shaohui on 2016/11/19.
 */

public class ImageDecoder {

    private static final String FILE_NAME = "share_image.jpg";

    public static String decode(Context context, ShareImageObject imageObject) throws Exception {
        File resultFile = cacheFile(context);

        if (!TextUtils.isEmpty(imageObject.getPathOrUrl())) {
            return decode(context, imageObject.getPathOrUrl());
        } else if (imageObject.getBitmap() != null) {
            // save bitmap to file
            FileOutputStream outputStream = new FileOutputStream(resultFile);
            imageObject.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return resultFile.getAbsolutePath();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Bitmap decodeBitmap(Context context, ShareImageObject imageObject)
            throws Exception {
        if (imageObject.getBitmap() != null) {
            return imageObject.getBitmap();
        } else if (!TextUtils.isEmpty(imageObject.getPathOrUrl())) {
            String path = decode(context, imageObject.getPathOrUrl());
            return BitmapFactory.decodeFile(path);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] compress(Bitmap origin, String filePath, int targetSize) {
        Bitmap thumb;
        if (origin != null && !origin.isRecycled()) {
            thumb = compress(origin, targetSize);
        } else {
            thumb = compress(filePath, targetSize, targetSize);
        }
        byte[] data = bmp2ByteArray(thumb);
        thumb.recycle();
        return data;
    }

    public static byte[] compress(Context context, ShareImageObject imageObject, int targetSize)
            throws Exception {
        Bitmap bitmap = decodeBitmap(context, imageObject);
        Bitmap thumb = compress(bitmap, targetSize);
        byte[] data = bmp2ByteArray(thumb);

        bitmap.recycle();
        thumb.recycle();
        return data;
    }

    private static String decode(Context context, String pathOrUrl) throws Exception {
        File resultFile = cacheFile(context);

        if (new File(pathOrUrl).exists()) {
            // copy file
            return decodeFile(new File(pathOrUrl), resultFile);
        } else if (HttpUrl.parse(pathOrUrl) != null) {
            // download image
            return downloadImageToUri(pathOrUrl, resultFile);
        } else {
            throw new IllegalArgumentException("Please input a file path or http url");
        }
    }

    public static Bitmap compress(Bitmap origin, int targetSize) {
        float scale = Math.max(origin.getHeight() / (float) targetSize,
                origin.getWidth() / (float) targetSize);
        if (scale > 1) {
            return Bitmap.createScaledBitmap(origin, (int) (origin.getWidth() / scale),
                    (int) (origin.getHeight() / scale), false);
        } else {
            return origin;
        }
    }

    private static String downloadImageToUri(String url, File resultFile) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        BufferedSink sink = Okio.buffer(Okio.sink(resultFile));
        sink.writeAll(response.body().source());

        sink.close();
        response.close();

        return resultFile.getAbsolutePath();
    }

    private static File cacheFile(Context context) {
        return new File(context.getExternalFilesDir(null), FILE_NAME);
    }

    private static void copyFile(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        byte[] buffer = new byte[4096];
        while (-1 != inputStream.read(buffer)) {
            outputStream.write(buffer);
        }

        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    private static String decodeFile(File origin, File result) throws IOException {
        copyFile(new FileInputStream(origin), new FileOutputStream(result, false));
        return result.getAbsolutePath();
    }

    /**
     * get the thumbnail
     */
    private static Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static byte[] bmp2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
