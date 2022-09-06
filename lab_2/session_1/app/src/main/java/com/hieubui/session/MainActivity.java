package com.hieubui.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ImageView ivImage;

    private TextView tvJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = findViewById(R.id.iv_image);
        tvJSON = findViewById(R.id.tv_json);
        Button btnDownloadImage = findViewById(R.id.btn_download_image);
        Button btnDownloadJSON = findViewById(R.id.btn_download_json);

        btnDownloadImage.setOnClickListener(view -> getImage("https://res.cloudinary.com/dk-find-out/image/upload/q_80,w_1920,f_auto/MonolophosaurusHiRes_usl6ti.jpg"));
        btnDownloadJSON.setOnClickListener(view -> getJSON("https://dummyjson.com/products/1"));
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private void getImage(@NonNull String url) {
        if (checkInternetConnected()) {
            new AsyncTask<String, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(String... strings) {
                    try {
                        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        int responseCode;

                        connection.setRequestMethod("GET");
                        connection.connect();
                        responseCode = connection.getResponseCode();
                        if (responseCode != HttpURLConnection.HTTP_OK) {
                            return null;
                        }

                        final InputStream inputStream = connection.getInputStream();
                        final File file = saveData(inputStream, "Monolophosaurus.png");

                        inputStream.close();
                        return BitmapFactory.decodeFile(file.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    ivImage.setImageBitmap(bitmap);
                }
            }.execute(url);
        }
    }

    private boolean checkInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        boolean isConnected = Arrays.stream(networkInfos)
            .anyMatch(networkInfo ->
                networkInfo.getState() == NetworkInfo.State.CONNECTED
                    || networkInfo.getState() == NetworkInfo.State.CONNECTING
            );

        if (!isConnected)
            Toast.makeText(this, "Internet is disconnected", Toast.LENGTH_SHORT).show();
        return isConnected;
    }

    @NonNull
    private File saveData(@NonNull InputStream inputStream, @NonNull String name) {
        File storage = getFilesDir();
        File file = new File(storage, name);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[8 * 1024];
            int count;

            while ((count = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private void getJSON(String url) {
        if (checkInternetConnected()) {
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... strings) {
                    try {
                        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        int responseCode;

                        connection.setRequestMethod("GET");
                        connection.connect();
                        responseCode = connection.getResponseCode();
                        if (responseCode != HttpURLConnection.HTTP_OK) {
                            return null;
                        }

                        final InputStream inputStream = connection.getInputStream();
                        final File file = saveData(inputStream, "dummyjson_products.json");

                        inputStream.close();
                        return getContent(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String str) {
                    super.onPostExecute(str);
                    tvJSON.setText(str);
                }
            }.execute(url);
        }
    }

    @NonNull
    @SuppressWarnings("deprecation")
    private String getContent(@NonNull File file) throws IOException {
        final InputStreamReader inputStreamReader = new InputStreamReader(file.toURL().openStream());
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        final StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        inputStreamReader.close();
        return stringBuilder.toString();
    }
}