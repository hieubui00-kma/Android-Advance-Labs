package com.hieubui.session;

import android.graphics.Matrix;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Bitmap {

    public static android.graphics.Bitmap rotate(@NonNull android.graphics.Bitmap source, float angle) {
        final int width = source.getWidth();
        final int height = source.getHeight();
        final Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        return android.graphics.Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
    }

    public static void save(@NonNull android.graphics.Bitmap bitmap, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);

        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
    }
}
