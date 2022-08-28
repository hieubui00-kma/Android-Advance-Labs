package com.hieubui.session;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageService extends Service {
    private static final long INTERVAL_TIME_SEND_MESSAGE = 2000;

    private static final String KEY_RANDOM_MESSAGE = "RANDOM_MESSAGE";

    private ScheduledExecutorService executorService;

    private Handler handler;

    private MutableLiveData<String> randomMessage;

    private IBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadScheduledExecutor();
        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                Bundle bundle = message.getData();

                randomMessage.postValue(bundle.getString(KEY_RANDOM_MESSAGE));
            }
        };
        randomMessage = new MutableLiveData<>();
        binder = new MessageBinder();
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getMessages();
        return START_NOT_STICKY;
    }

    private void getMessages() {
        executorService.scheduleWithFixedDelay(() -> {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();

            bundle.putString(KEY_RANDOM_MESSAGE, UUID.randomUUID().toString());
            message.setData(bundle);
            handler.sendMessage(message);
        }, 0, INTERVAL_TIME_SEND_MESSAGE, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        executorService.shutdown();
        super.onDestroy();
    }

    @NonNull
    public List<Contact> getContacts() {
        return Phonebook.getContacts(this);
    }

    public void deleteContact(long contactId) {
        Phonebook.deleteContact(this, contactId);
    }

    @NonNull
    public List<SMSMessage> getSMSMessages() {
        return Phonebook.getSMSMessages(this);
    }

    public void deleteSMSMessage(long messageId) {
        Phonebook.deleteSMSMessage(this, messageId);
    }

    public LiveData<String> getRandomMessage() {
        return randomMessage;
    }

    public class MessageBinder extends Binder {

        public MessageService getService() {
            return MessageService.this;
        }
    }
}
