package com.hieubui.consumer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtMovieId;

    private EditText edtName;

    private EditText edtDescription;

    private TextView tvLogs;

    private LabsRepository labsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtMovieId = findViewById(R.id.edt_movie_id);
        edtName = findViewById(R.id.edt_movie_name);
        edtDescription = findViewById(R.id.edt_movie_description);
        tvLogs = findViewById(R.id.tv_logs);
        labsRepository = new LabsRepositoryImpl(new WeakReference<>(getContentResolver()));

        setEvents();
    }

    @SuppressLint("SetTextI18n")
    private void setEvents() {
        Button btnCreateMovie = findViewById(R.id.btn_create_movie);
        Button btnGetMovies = findViewById(R.id.btn_get_movies);
        Button btnUpdateMovie = findViewById(R.id.btn_update_movie);
        Button btnDeleteMovies = findViewById(R.id.btn_delete_movies);

        btnCreateMovie.setOnClickListener(view -> createMovie());

        btnGetMovies.setOnClickListener(view -> {
            List<Movie> movies = labsRepository.getMovies();

            tvLogs.setText("Movies: " + movies);
        });

        btnUpdateMovie.setOnClickListener(view -> updateMovie());

        btnDeleteMovies.setOnClickListener(view -> labsRepository.deleteMovies());
    }

    private void createMovie() {
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        Movie movie = new Movie();

        movie.setName(name);
        movie.setDescription(description);
        labsRepository.createMovie(movie);
    }

    private void updateMovie() {
        String movieId = edtMovieId.getText().toString();
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        Movie movie = new Movie(Integer.parseInt(movieId), name, description);

        labsRepository.updateMovie(movie);
    }
}