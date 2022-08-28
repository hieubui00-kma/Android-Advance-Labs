package com.hieubui.session;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AuthorDetailsActivity extends AppCompatActivity {
    private BookAdapter bookAdapter;

    private AlertDialog inputBookDialog;

    private AlertDialog inputAuthorDialog;

    private LabsRepository labsRepository;

    private Author author;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_details);
        ActionBar actionBar = getSupportActionBar();
        RecyclerView recyclerBooks = findViewById(R.id.recycler_books);
        bookAdapter = new BookAdapter();
        inputBookDialog = createInputDialog(
            R.layout.dialog_input_book,
            "Book",
            (dialog, which) -> createBook(),
            (dialog, which) -> cancelCreateBook()
        );
        inputAuthorDialog = createInputDialog(
            R.layout.dialog_input_author,
            "Author",
            (dialog, which) -> updateAuthor(),
            (dialog, which) -> cancelUpdateAuthor()
        );
        LabsDatabase database = new LabsDatabaseImpl(this);
        labsRepository = new LabsRepositoryImpl(database);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        recyclerBooks.setHasFixedSize(true);
        recyclerBooks.setAdapter(bookAdapter);

        bookAdapter.setOnItemClickListener(this::navigateToBookDetails);

        parseData();
    }

    @NonNull
    private AlertDialog createInputDialog(
        @LayoutRes int resId,
        @Nullable String title,
        @Nullable DialogInterface.OnClickListener onPositiveListener,
        @Nullable DialogInterface.OnClickListener onNegativeListener
    ) {
        View root = findViewById(android.R.id.content).getRootView();
        View inputDialog = getLayoutInflater().inflate(resId, (ViewGroup) root, false);

        return new AlertDialog.Builder(this)
            .setTitle(title)
            .setView(inputDialog)
            .setPositiveButton(android.R.string.ok, onPositiveListener)
            .setNegativeButton(android.R.string.cancel, onNegativeListener)
            .create();
    }

    private void createBook() {
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
            labsRepository.createBook(title, introduce, summary, author);
        }
        inputBookDialog.dismiss();
        if (isValid) {
            getBooks();
        }
    }

    private void cancelCreateBook() {
        EditText inputName = inputBookDialog.findViewById(R.id.edt_title);
        EditText inputIntroduce = inputBookDialog.findViewById(R.id.edt_introduce);
        EditText inputSummary = inputBookDialog.findViewById(R.id.edt_summary);

        if (inputName != null) inputName.clearFocus();
        if (inputIntroduce != null) inputIntroduce.clearFocus();
        if (inputSummary != null) inputSummary.clearFocus();
        inputBookDialog.cancel();
    }

    private void updateAuthor() {
        EditText input = inputAuthorDialog.findViewById(R.id.edt_input);
        String authorName = input != null ? input.getText().toString() : "";
        ActionBar actionBar = getSupportActionBar();

        if (input != null) input.clearFocus();
        author.setName(authorName);
        labsRepository.updateAuthor(author);
        if (input != null) input.setText(null);
        inputAuthorDialog.dismiss();
        if (actionBar != null) actionBar.setTitle(author.getName());
        setResult(RESULT_OK);
    }

    private void cancelUpdateAuthor() {
        EditText input = inputAuthorDialog.findViewById(R.id.edt_input);

        if (input != null) {
            input.clearFocus();
            input.setText(null);
        }
        inputAuthorDialog.cancel();
    }

    @SuppressWarnings("deprecation")
    private void navigateToBookDetails(Book book) {
        Intent intent = new Intent(this, BookDetailsActivity.class);

        intent.putExtra("BOOK_ID", book.getID());
        startActivityForResult(intent, "UPDATE_BOOK".length());
    }

    private void parseData() {
        int authorId = getIntent().getIntExtra("AUTHOR_ID", -1);
        author = labsRepository.getAuthor(authorId);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) actionBar.setTitle(author.getName());
        getBooks();
    }

    private void getBooks() {
        List<Book> books = labsRepository.getBooks(author.getID());

        bookAdapter.setBooks(books);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_author_details, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_book:
                showInputBookDialog();
                return true;

            case R.id.action_update:
                showInputAuthorDialog();
                return true;

            case R.id.action_delete:
                deleteAuthor();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInputBookDialog() {
        EditText inputTitle = inputBookDialog.findViewById(R.id.edt_title);
        EditText inputIntroduce = inputBookDialog.findViewById(R.id.edt_introduce);
        EditText inputSummary = inputBookDialog.findViewById(R.id.edt_summary);

        if (inputTitle != null) inputTitle.setText(null);
        if (inputIntroduce != null) inputIntroduce.setText(null);
        if (inputSummary != null) inputSummary.setText(null);
        inputBookDialog.show();
    }

    private void showInputAuthorDialog() {
        EditText input;

        inputAuthorDialog.show();
        input = inputAuthorDialog.findViewById(R.id.edt_input);
        if (input != null) input.setText(author.getName());
    }

    private void deleteAuthor() {
        labsRepository.deleteAuthor(author.getID());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getBooks();
        }
    }

    @Override
    protected void onDestroy() {
        if (inputBookDialog != null) inputBookDialog.dismiss();
        if (inputAuthorDialog != null) inputAuthorDialog.dismiss();
        super.onDestroy();
    }
}
