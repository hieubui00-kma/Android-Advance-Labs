package com.hieubui.session;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.hieubui.session.Orientation.getOrientation;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surface;

    private ImageView ivThumbnail;

    private Camera camera;

    private MediaRecorder mediaRecorder;

    private File storage;

    private File recordFile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surface = findViewById(R.id.surface);
        ivThumbnail = findViewById(R.id.iv_thumbnails);
        final CheckBox chkbRecord = findViewById(R.id.btn_record);
        mediaRecorder = new MediaRecorder();
        final String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Session";
        storage = new File(path);

        storage.mkdir();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        surface.getHolder().addCallback(this);

        ivThumbnail.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordsActivity.class);

            startActivity(intent);
        });
        chkbRecord.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                startRecord();
                return;
            }

            stopRecord();
        });

        getLastRecordThumbnail();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (camera != null) camera.stopPreview();
    }

    private void startRecord() {
        recordFile = createRecordFile();

        setupMediaRecorder(recordFile);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.i("MainActivity", "Start Recording");
        } catch (IOException e) {
            e.printStackTrace();
            mediaRecorder.reset();
        }
    }

    @NonNull
    private File createRecordFile() {
        final String path = storage.getPath();
        final String pattern = "dd.MM.yyyy_HH.mm.ss";
        final DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        final String name = "MPEG_" + dateFormat.format(new Date()) + ".mp4";

        return new File(path, name);
    }

    private void setupMediaRecorder(File outputFile) {
        int orientation = getOrientation(this);

        camera.unlock();
        mediaRecorder.setOrientationHint(orientation);
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setVideoFrameRate(30);    // 30 FPS
        mediaRecorder.setPreviewDisplay(surface.getHolder().getSurface());
    }

    private void stopRecord() {
        Log.i("MainActivity", "Stop Recording");
        mediaRecorder.stop();
        mediaRecorder.reset();
        Glide.with(this).load(recordFile).into(ivThumbnail);
        ivThumbnail.setEnabled(true);
    }

    private void getLastRecordThumbnail() {
        final File[] records = Objects.requireNonNull(storage.listFiles());
        final boolean isEmpty = records.length == 0;

        ivThumbnail.setEnabled(!isEmpty);
        if (isEmpty) {
            return;
        }

        final String pattern = "dd.MM.yyyy_HH.mm.ss";
        final DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        File record = (File) Arrays.stream(records)
            .sorted((fileFirst, firstSecond) -> {
                try {
                    Date dateFirst = dateFormat.parse(fileFirst.getName()
                        .replace("MPEG_", "")
                        .replace(".mp4", "")
                    );
                    Date dateSecond = dateFormat.parse(firstSecond.getName()
                        .replace("MPEG_", "")
                        .replace(".mp4", "")
                    );

                    assert dateFirst != null;
                    assert dateSecond != null;
                    return dateFirst.compareTo(dateSecond);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            })
            .toArray()[records.length - 1];

        Glide.with(this).load(record).into(ivThumbnail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions(new String[]{CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, 97);
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 97 && Arrays.stream(grantResults).allMatch(result -> result == PERMISSION_GRANTED)) {
            setupCamera();
        }
    }

    private void setupCamera() {
        camera = Camera.open();
        int orientation = getOrientation(this);

        try {
            camera.setDisplayOrientation(orientation);
            camera.setPreviewDisplay(surface.getHolder());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        if (camera != null) camera.stopPreview();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mediaRecorder.release();
        if (camera != null) camera.release();
        super.onDestroy();
    }
}