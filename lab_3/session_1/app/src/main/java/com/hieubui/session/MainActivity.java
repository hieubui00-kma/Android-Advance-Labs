package com.hieubui.session;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.hieubui.session.Orientation.getOrientation;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private CameraView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera = findViewById(R.id.camera);
        final SeekBar seekBarZoom = findViewById(R.id.seek_bar_zoom);
        final ImageButton btnCapture = findViewById(R.id.btn_capture);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        seekBarZoom.setOnSeekBarChangeListener(this);
        btnCapture.setOnClickListener(view -> takePhoto());

        requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, 66);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
        camera.setZoom((float) progress / 100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void takePhoto() {
        camera.takePhoto((bytes, camera) -> {
            final Bitmap original = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            final int orientation = getOrientation(this);
            final Bitmap photo = com.hieubui.session.Bitmap.rotate(original, orientation);
            final File file = createPhotoFile();

            try {
                com.hieubui.session.Bitmap.save(photo, file);
                WallpaperManager.getInstance(getApplicationContext()).setBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @NonNull
    private File createPhotoFile() {
        final String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Session";
        final String pattern = "dd.MM.yyyy_HH.mm.ss";
        final DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        final String name = "JPEG_" + dateFormat.format(new Date()) + ".jpeg";

        new File(path).mkdir();
        return new File(path, name);
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 66 && Arrays.stream(grantResults).allMatch(result -> result == PERMISSION_GRANTED)) {
            camera.setup();
        }
    }
}