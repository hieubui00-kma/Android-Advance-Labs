package com.hieubui.session_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable {
    private TextView tvProgress;

    private ProgressBar progressBar;

    private Handler handler;

    private WorkerThread thread;

    private AsyncTask<Void, Integer, Void> asyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvProgress = findViewById(R.id.tv_progress);
        progressBar = findViewById(R.id.progress);
        Button btnStart = findViewById(R.id.btn_start);
        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                Bundle bundle = message.getData();
                int progress = bundle.getInt("PROGRESS", 0);

                updateProgress(progress);
            }
        };

        btnStart.setOnClickListener(view -> startAsyncProgress());
    }

    @SuppressLint("SetTextI18n")
    private void updateProgress(int progress) {
        tvProgress.setText(progress + "%");
        progressBar.setProgress(progress);
    }

    private void startProgressThread() {
        if (thread != null && thread.isRunning()) {
            return;
        }

        thread = new WorkerThread(this);
        thread.start();
    }

    @Override
    public void run() {
        Message message;
        Bundle bundle;

        thread.setRunning(true);
        for (int i = 0; i <= 100; i++) {
            message = handler.obtainMessage();
            bundle = new Bundle();

            bundle.putInt("PROGRESS", i);
            message.setData(bundle);
            handler.sendMessage(message);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread.setRunning(false);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private void startAsyncProgress() {
        if (asyncTask != null) asyncTask.cancel(true);
        asyncTask = new AsyncTask<Void, Integer, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i <= 100; i++) {
                    publishProgress(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                updateProgress(values[0]);
            }
        };
        asyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        if (thread != null) thread.interrupt();
        if (asyncTask != null) asyncTask.cancel(true);
        super.onDestroy();
    }
}