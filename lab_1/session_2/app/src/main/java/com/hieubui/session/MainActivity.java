package com.hieubui.session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements Runnable {
    private EditText edtInput;

    private final NumberAdapter adapterRandomNumbers = new NumberAdapter();

    private final NumberAdapter adapterPrimeNumbers = new NumberAdapter();

    private Handler handler;

    private WorkerThread thread;

    private AsyncTask<List<Integer>, Void, List<Integer>> asyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStart = findViewById(R.id.btn_start);
        edtInput = findViewById(R.id.edt_input);
        RecyclerView recyclerRandomNumbers = findViewById(R.id.recycler_random_numbers);
        RecyclerView recyclerPrimeNumbers = findViewById(R.id.recycler_prime_numbers);
        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                final Bundle bundle = message.getData();
                final ArrayList<Integer> randomNumbers = bundle.getIntegerArrayList("RANDOM_NUMBERS");

                adapterRandomNumbers.setNumbers(randomNumbers);
                startAsyncProgress(randomNumbers);
            }
        };

        recyclerRandomNumbers.setHasFixedSize(true);
        recyclerRandomNumbers.setAdapter(adapterRandomNumbers);
        recyclerPrimeNumbers.setHasFixedSize(true);
        recyclerPrimeNumbers.setAdapter(adapterPrimeNumbers);
        btnStart.setOnClickListener(view -> startProgressThread());
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
        final String inputData = edtInput.getText().toString();
        final int input = inputData.isEmpty() ? 0 : Integer.parseInt(inputData);
        final ArrayList<Integer> randomNumbers = new ArrayList<>();
        final Message message = handler.obtainMessage();
        final Bundle bundle = new Bundle();
        final Random random = new Random();

        thread.setRunning(true);
        for (int i = 0; i < input; i++) {
            randomNumbers.add(random.nextInt(10000));
        }
        bundle.putIntegerArrayList("RANDOM_NUMBERS", randomNumbers);
        message.setData(bundle);
        handler.sendMessage(message);
        thread.setRunning(false);
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @SuppressLint("StaticFieldLeak")
    private void startAsyncProgress(List<Integer> randomNumbers) {
        if (asyncTask != null) asyncTask.cancel(true);
        asyncTask = new AsyncTask<List<Integer>, Void, List<Integer>>() {

            @Override
            protected List<Integer> doInBackground(List<Integer>... lists) {
                return lists[0].stream().filter(number -> isPrime(number)).collect(Collectors.toList());
            }

            @Override
            protected void onPostExecute(List<Integer> numbers) {
                super.onPostExecute(numbers);
                adapterPrimeNumbers.setNumbers(numbers);
            }
        };
        asyncTask.execute(randomNumbers);
    }

    private boolean isPrime(int n) {
        // Corner case
        if (n <= 1)
            return false;

        // Check from 2 to n-1
        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;

        return true;
    }

    @Override
    protected void onDestroy() {
        if (thread != null) thread.interrupt();
        if (asyncTask != null) asyncTask.cancel(true);
        super.onDestroy();
    }
}