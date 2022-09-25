package com.hieubui.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.io.File;
import java.util.Objects;

public class RecordAdapter extends FragmentStateAdapter {
    private final File[] records;

    public RecordAdapter(@NonNull FragmentActivity fragmentActivity, File[] records) {
        super(fragmentActivity);
        this.records = records;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new RecordDetailsFragment();
        Bundle arguments = new Bundle();

        arguments.putSerializable("RECORD", records[position]);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return records.length;
    }

    public static class RecordDetailsFragment extends Fragment {
        private VideoView videoRecord;

        @Nullable
        @Override
        public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
        ) {
            return inflater.inflate(R.layout.fragment_record_details, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            videoRecord = view.findViewById(R.id.video_record);
            MediaController mediaController = new MediaController(getContext());
            File record = (File) (getArguments() != null ? getArguments().getSerializable("RECORD") : null);

            mediaController.setAnchorView(videoRecord);
            videoRecord.setMediaController(mediaController);
            videoRecord.setOnPreparedListener(mediaPlayer -> {
                videoRecord.seekTo(1);
            });
            videoRecord.setVideoPath(Objects.requireNonNull(record).getPath());
        }
    }
}
