package com.example.testing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static byte[] convertDrawableToByteArray(Drawable drawable) {
        // Mendapatkan Bitmap dari Drawable
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return convertBitmapToByteArray(bitmap);
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Kompres bitmap ke format JPEG dengan kualitas 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        Log.d("ImageUtils", "Size before compression: " + byteArray.length + " bytes");

        // Jika ukuran lebih dari 2MB (2 * 1024 * 1024 bytes), lakukan kompresi
        if (byteArray.length > 2 * 1024 * 1024) {
            stream.reset(); // Reset stream untuk mengompres ulang
            int quality = 90; // Mulai kompresi dengan kualitas 90%

            // Turunkan kualitas secara bertahap sampai ukuran kurang dari 2MB
            while (byteArray.length > 2 * 1024 * 1024 && quality > 0) {
                stream.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                byteArray = stream.toByteArray();
                quality -= 10;
                Log.d("ImageUtils", "Size after compression at quality " + quality + ": " + byteArray.length + " bytes");
            }
        }

        return byteArray;
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage, Context context) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }
}
