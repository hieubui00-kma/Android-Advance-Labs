package com.hieubui.session;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CameraView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera = findViewById(R.id.camera);
        SeekBar seekBarZoom = findViewById(R.id.seek_bar_zoom);
        ImageButton btnCapture = findViewById(R.id.btn_capture);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });
        btnCapture.setOnClickListener(view -> takePhoto());
    }

    private void takePhoto() {
        camera.takePhoto((bytes, camera) -> {
            File file = createPhotoFile();

            try (OutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @NonNull
    private File createPhotoFile() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/Pictures";
        String pattern = "dd.MM.yyyy_HH.mm.ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        String name = "JPEG_" + dateFormat.format(new Date()) + ".jpeg";

        return new File(path, name);
    }
}