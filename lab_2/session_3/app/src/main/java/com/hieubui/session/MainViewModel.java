package com.hieubui.session;

import static android.util.Patterns.WEB_URL;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final String baseUrl = "https://www.google.com";

    private final MutableLiveData<String> url = new MutableLiveData<>(baseUrl);

    public void search(String query) {
        String url = WEB_URL.matcher(query).matches() ? query : baseUrl + "/search?q=" + query;

        this.url.postValue(url);
    }

    public LiveData<String> getURL() {
        return url;
    }
}
