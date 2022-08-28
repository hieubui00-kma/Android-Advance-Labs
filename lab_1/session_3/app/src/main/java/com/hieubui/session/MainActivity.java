package com.hieubui.session;

import static com.hieubui.session.MediaPlaybackService.KEY_SONG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MediaPlaybackService mediaPlaybackService;

    private ServiceConnection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MediaPlaybackService.MediaPlaybackBinder binder = (MediaPlaybackService.MediaPlaybackBinder) iBinder;

                mediaPlaybackService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mediaPlaybackService = null;
            }
        };

        setEvents();
    }

    private void setEvents() {
        Button btnStart = findViewById(R.id.btn_start);
        Button btnResume = findViewById(R.id.btn_resume);
        Button btnPause = findViewById(R.id.btn_pause);
        Button btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(view -> {
            if (mediaPlaybackService != null) startMediaPlaybackService();
        });

        btnResume.setOnClickListener(view -> {
            if (mediaPlaybackService != null) mediaPlaybackService.resume();
        });

        btnPause.setOnClickListener(view -> {
            if (mediaPlaybackService != null) mediaPlaybackService.pause();
        });

        btnStop.setOnClickListener(view -> {
            if (mediaPlaybackService != null) stopMediaPlaybackService();
        });
    }

    private void startMediaPlaybackService() {
        Intent intent = new Intent(this, MediaPlaybackService.class);
        Song song = new Song("Bên trên tầng lầu", "Tăng Duy Tân");

        song.setFileName("BenTrenTangLau-TangDuyTan.mp3");
        intent.putExtra(KEY_SONG, song);
        startService(intent);
    }

    private void stopMediaPlaybackService() {
        Intent intent = new Intent(this, MediaPlaybackService.class);

        mediaPlaybackService.stop();
        stopService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MediaPlaybackService.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }
}