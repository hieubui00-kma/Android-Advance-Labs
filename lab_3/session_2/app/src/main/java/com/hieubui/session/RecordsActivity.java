package com.hieubui.session;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;

public class RecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ImageButton btnClose = findViewById(R.id.btn_close);

        setupRecordsPager();
        btnClose.setOnClickListener(view -> onBackPressed());
    }

    private void setupRecordsPager() {
        ViewPager2 pagerRecords = findViewById(R.id.pager_records);
        File[] records = getRecords();
        RecordAdapter recordAdapter = new RecordAdapter(this, records);

        pagerRecords.setAdapter(recordAdapter);
        pagerRecords.setPageTransformer(new ZoomOutPageTransformer());
        pagerRecords.setCurrentItem(records.length - 1, false);
    }

    private File[] getRecords() {
        final String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Session";
        final File storage = new File(path);

        return storage.listFiles();
    }
}
