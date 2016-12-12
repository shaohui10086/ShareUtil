package me.shaohui.shareutil.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private static final int MAX_SIZE = 800;

    public static String decode(Context context, String pathOrUrl) {
        File resultFile = cacheFile(context);

        if (HttpUrl.parse(pathOrUrl) != null) {
            return downloadImageToUri(pathOrUrl, resultFile);
        } else {
            if (!new File(pathOrUrl).exists()) {
                return null;
            } else {
                return decodeFile(new File(pathOrUrl), resultFile);
            }
        }
    }

    public static String decode(Context context, Bitmap bitmap) {
        File resultFile = cacheFile(context);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(resultFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            bitmap.recycle();
            return resultFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap compress(Context context, String pathOrUrl) {
        File resultFile = cacheFile(context);
        if (HttpUrl.parse(pathOrUrl) != null) {
            String path = downloadImageToUri(pathOrUrl, resultFile);
            return compress(path, MAX_SIZE, MAX_SIZE);
        } else {
            if (new File(pathOrUrl).exists()) {
                return compress(pathOrUrl, MAX_SIZE, MAX_SIZE);
            } else {
                return null;
            }
        }
    }

    public static Bitmap compress(Bitmap origin, int targetSize) {
        float scale = Math.max(origin.getHeight() / (float) targetSize,
                origin.getWidth() / (float) targetSize);
        if (scale > 1) {
            return Bitmap.createScaledBitmap(origin, (int) (origin.getWidth()/scale),
                    (int) (origin.getHeight()/scale), false);
        } else {
            return origin;
        }
    }

    private static String downloadImageToUri(String url, File resultFile) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            BufferedSink sink = Okio.buffer(Okio.sink(resultFile));
            sink.writeAll(response.body().source());

            sink.close();
            response.close();

            return resultFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File cacheFile(Context context) {
        return new File(context.getExternalFilesDir(null), "share_image.jpg");
    }

    private static void copyFile(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[4096];
            while (-1 != inputStream.read(buffer)) {
                outputStream.write(buffer);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String decodeFile(File origin, File result) {
        try {
            copyFile(new FileInputStream(origin), new FileOutputStream(result, false));

            return result.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get the thumbnail
     */
    public static Bitmap compress(String imagePath, int width, int height) {
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
