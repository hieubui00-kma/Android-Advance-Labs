package com.hieubui.session;

import static com.hieubui.session.SessionApplication.CHANNEL_MEDIA_PLAYBACK;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.io.FileDescriptor;
import java.io.IOException;

public class MediaPlaybackService extends Service {
    private static final int NOTIFICATION_ID = 55555;

    public static final String KEY_SONG = "SONG";

    private IBinder binder;

    private MediaPlayer mediaPlayer;

    private AssetFileDescriptor assetFileDescriptor;

    private final AudioAttributes audioAttributes = new AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build();

    private Song song;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MediaPlaybackBinder();
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForeground(true);
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.song = (Song) intent.getSerializableExtra(KEY_SONG);
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    private void start() throws IOException {
        assetFileDescriptor = getAssets().openFd(song.getFileName());
        FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
        long offset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getStartOffset();

        close();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(audioAttributes);
        mediaPlayer.setDataSource(fileDescriptor, offset, length);
        mediaPlayer.prepare();
        mediaPlayer.start();
        closeAssetFile();
    }

    private void closeAssetFile() {
        if (assetFileDescriptor != null) {
            try {
                assetFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (song != null && mediaPlayer != null && mediaPlayer.isPlaying()) {
            Notification notification = createNotification(song.getTitle(), song.getArtistNames());

            startForeground(NOTIFICATION_ID, notification);
        }
        return true;
    }

    @NonNull
    private Notification createNotification(String title, String content) {
        return new NotificationCompat.Builder(this, CHANNEL_MEDIA_PLAYBACK)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentTitle(title)
            .setContentText(content)
            .build();
    }

    @Override
    public void onDestroy() {
        close();
        stopForeground(true);
        super.onDestroy();
    }

    private void close() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public class MediaPlaybackBinder extends Binder {

        public MediaPlaybackService getService() {
            return MediaPlaybackService.this;
        }
    }
}
