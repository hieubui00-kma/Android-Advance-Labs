package com.hieubui.session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AuthorAdapter authorAdapter;

    private AlertDialog inputAuthorDialog;

    private LabsRepository labsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        RecyclerView recyclerAuthors = findViewById(R.id.recycler_authors);
        authorAdapter = new AuthorAdapter();
        inputAuthorDialog = createInputAuthorDialog();
        LabsDatabase database = new LabsDatabaseImpl(this);
        labsRepository = new LabsRepositoryImpl(database);

        if (actionBar != null) actionBar.setTitle("Authors");
        recyclerAuthors.setHasFixedSize(true);
        recyclerAuthors.setAdapter(authorAdapter);

        authorAdapter.setOnItemClickListener(this::navigateToAuthorDetails);

        getAuthors();
    }

    @NonNull
    private AlertDialog createInputAuthorDialog() {
        View root = findViewById(android.R.id.content).getRootView();
        View inputDialog = getLayoutInflater().inflate(R.layout.dialog_input_author, (ViewGroup) root, false);

        return new AlertDialog.Builder(this)
            .setTitle("Author")
            .setView(inputDialog)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                EditText input = inputDialog.findViewById(R.id.edt_input);
                String authorName = input.getText().toString();

                input.clearFocus();
                if (!authorName.isEmpty()) labsRepository.createAuthor(authorName);
                input.setText(null);
                dialog.dismiss();
                if (!authorName.isEmpty()) getAuthors();
            })
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                EditText input = inputDialog.findViewById(R.id.edt_input);

                input.clearFocus();
                input.setText(null);
                dialog.cancel();
            })
            .create();
    }

    private void getAuthors() {
        List<Author> authors = labsRepository.getAuthors();

        authorAdapter.setAuthors(authors);
    }

    @SuppressWarnings("deprecation")
    private void navigateToAuthorDetails(Author author) {
        Intent intent = new Intent(this, AuthorDetailsActivity.class);

        intent.putExtra("AUTHOR_ID", author.getID());
        startActivityForResult(intent, "UPDATE_AUTHOR".length());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_authors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            inputAuthorDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getAuthors();
        }
    }

    @Override
    protected void onDestroy() {
        if (inputAuthorDialog != null) inputAuthorDialog.dismiss();
        super.onDestroy();
    }
}