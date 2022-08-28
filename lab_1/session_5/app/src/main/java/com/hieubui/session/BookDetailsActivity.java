package com.hieubui.session;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {
    private TextView tvIntroduce;

    private TextView tvSummary;

    private AlertDialog inputBookDialog;

    private LabsRepository labsRepository;

    private Book book;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ActionBar actionBar = getSupportActionBar();
        tvIntroduce = findViewById(R.id.tv_introduce);
        tvSummary = findViewById(R.id.tv_summary);
        inputBookDialog = createInputBookDialog();
        LabsDatabase database = new LabsDatabaseImpl(this);
        labsRepository = new LabsRepositoryImpl(database);
        int bookId = getIntent().getIntExtra("BOOK_ID", -1);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        getBook(bookId);
    }

    @NonNull
    private AlertDialog createInputBookDialog() {
        View root = findViewById(android.R.id.content).getRootView();
        View inputDialog = getLayoutInflater().inflate(R.layout.dialog_input_book, (ViewGroup) root, false);

        return new AlertDialog.Builder(this)
            .setTitle("Book")
            .setView(inputDialog)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> updateBook())
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> cancelUpdateBook())
            .create();
    }

    private void updateBook() {
        EditText inputName = inputBookDialog.findViewById(R.id.edt_title);
        EditText inputIntroduce = inputBookDialog.findViewById(R.id.edt_introduce);
        EditText inputSummary = inputBookDialog.findViewById(R.id.edt_summary);
        String title = inputName != null ? inputName.getText().toString() : "";
        String introduce = inputIntroduce != null ? inputIntroduce.getText().toString() : "";
        String summary = inputSummary != null ? inputSummary.getText().toString() : "";
        boolean isValid = !title.isEmpty() && !introduce.isEmpty();

        if (inputName != null) inputName.clearFocus();
        if (inputIntroduce != null) inputIntroduce.clearFocus();
        if (inputSummary != null) inputSummary.clearFocus();
        if (isValid) {
            book.setTitle(title);
            book.setIntroduce(introduce);
            book.setSummary(summary);
            labsRepository.updateBook(book);
        }
        inputBookDialog.dismiss();
        if (isValid) {
            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) actionBar.setTitle(book.getTitle());
            tvIntroduce.setText(book.getIntroduce());
            tvSummary.setText(book.getSummary());
        }
    }

    private void cancelUpdateBook() {
        EditText input = inputBookDialog.findViewById(R.id.edt_input);

        if (input != null) input.clearFocus();
        inputBookDialog.cancel();
    }

    private void getBook(int bookId) {
        book = labsRepository.getBook(bookId);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) actionBar.setTitle(book.getTitle());
        tvIntroduce.setText(book.getIntroduce());
        tvSummary.setText(book.getSummary());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                showInputBookDialog();
                return true;

            case R.id.action_delete:
                deleteBook();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInputBookDialog() {
        EditText inputTitle;
        EditText inputIntroduce;
        EditText inputSummary;

        inputBookDialog.show();
        inputTitle = inputBookDialog.findViewById(R.id.edt_title);
        inputIntroduce = inputBookDialog.findViewById(R.id.edt_introduce);
        inputSummary = inputBookDialog.findViewById(R.id.edt_summary);
        if (inputTitle != null) inputTitle.setText(book.getTitle());
        if (inputIntroduce != null) inputIntroduce.setText(book.getIntroduce());
        if (inputSummary != null) inputSummary.setText(book.getSummary());
    }

    private void deleteBook() {
        labsRepository.deleteBook(book.getID());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (inputBookDialog != null) inputBookDialog.dismiss();
        super.onDestroy();
    }
}
