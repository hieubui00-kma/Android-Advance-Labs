package com.hieubui.session;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private RecyclerView recyclerUsers;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_loading);
        recyclerUsers = findViewById(R.id.recycler_users);
        userAdapter = new UserAdapter();

        recyclerUsers.setHasFixedSize(true);
        recyclerUsers.setAdapter(userAdapter);

        getUsers();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private void getUsers() {
        String API = "https://api.github.com/users";
        AsyncTask<String, Void, List<User>> asyncTask = new AsyncTask<String, Void, List<User>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                recyclerUsers.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<User> doInBackground(String... paths) {
                try {
                    final URL url = new URL(paths[0]);
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int responseCode;

                    connection.setRequestMethod("GET");
                    connection.connect();
                    responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        return null;
                    }

                    final InputStream inputStream = connection.getInputStream();
                    final String data = getData(inputStream);

                    inputStream.close();
                    return parseData(data);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                progressBar.setVisibility(View.GONE);
                recyclerUsers.setVisibility(View.VISIBLE);
                userAdapter.setUsers(users);
            }
        };

        asyncTask.execute(API);
    }

    @NonNull
    private String getData(@NonNull InputStream inputStream) throws IOException {
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8 * 1024);
        final StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        inputStreamReader.close();
        return stringBuilder.toString();
    }

    @NonNull
    private List<User> parseData(String data) throws JSONException {
        List<User> users = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);
        int length = jsonArray.length();
        JSONObject jsonObject;
        User user;

        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            user = new User();
            user.setID(jsonObject.getInt("id"));
            user.setName(jsonObject.getString("login"));
            user.setAvatar(getAvatar(jsonObject.getString("avatar_url")));
            users.add(user);
        }
        return users;
    }

    @Nullable
    private Bitmap getAvatar(String path) {
        try {
            final URL url = new URL(path);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode;

            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            final InputStream inputStream = connection.getInputStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}