package com.synconset;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import org.apache.cordova.LOG;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageHelper {

    private static final String LOG_TAG = "ImageHelper";

    static public void handleBitmap(OutputStream outStream, Bitmap bitmap, int maxWidthOrHeight, int compressQuality,
            int maxImageByteSize, int minNeedcompressByteSize, boolean autoCrop) throws IOException {
        // Double-check the bitmap.
        if (bitmap == null) {
            LOG.d(LOG_TAG, "I either have a null image path or bitmap");
            return;
        }

        bitmap = resize(bitmap, maxWidthOrHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，
        int sourceLength = baos.toByteArray().length;
        // if (sourceLength > this.maxImageSize) {
        // compressImage(bitmap, this.compressQuality, this.maxImageSize,
        // CompressFormat.JPEG, baos);
        // } else if (sourceLength > this.minNeedcompressSize) {
        if (sourceLength > minNeedcompressByteSize) {
            compress(bitmap, compressQuality, sourceLength, CompressFormat.JPEG, baos);
        }
        outStream.write(baos.toByteArray());
        LOG.d(LOG_TAG, "sourceLength:" + sourceLength / 1024 + "kb  compressLength:" + baos.toByteArray().length / 1024
                + "kb");
        baos.close();

        bitmap.recycle();
        bitmap = null;
        System.gc();
    }

    private static void compress(Bitmap image, int quality, int reqSize, CompressFormat compressFormat,
            ByteArrayOutputStream baos) {
        do {
            baos.reset();// 清空baos
            image.compress(compressFormat, quality, baos);// 这里压缩options%，把压缩后的数据放到baos中
            LOG.d(LOG_TAG, "compressImage  quality:" + quality + " length:" + baos.toByteArray().length / 1024 + "kb");
            quality -= 5;
        } while (quality > 0 && baos.toByteArray().length > reqSize);
    }

    private static Bitmap resize(Bitmap bm, int maxSize) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scale = 0;

        if (width >= height && width > maxSize) {
            scale = ((float) maxSize) / width;
        } else if (height > width && height > maxSize) {
            scale = ((float) maxSize) / height;
        } else {
            return bm;
        }
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}