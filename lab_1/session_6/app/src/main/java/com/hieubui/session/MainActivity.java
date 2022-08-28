package com.hieubui.session;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerData;

    private ContactAdapter contactAdapter;

    private SMSMessageAdapter smsMessageAdapter;

    private MessageService messageService;

    private ServiceConnection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerData = findViewById(R.id.recycler_data);
        contactAdapter = new ContactAdapter();
        smsMessageAdapter = new SMSMessageAdapter();
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MessageService.MessageBinder binder = (MessageService.MessageBinder) iBinder;

                messageService = binder.getService();
                messageService.getRandomMessage().observe(MainActivity.this, message -> showMessage(message));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                messageService = null;
            }
        };
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        recyclerData.addItemDecoration(dividerItemDecoration);
        recyclerData.setHasFixedSize(true);

        setEvents();

        startMessageService();
    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setEvents() {
        Button btnReadContacts = findViewById(R.id.btn_read_contacts);
        Button btnReadSMS = findViewById(R.id.btn_read_sms);

        btnReadContacts.setOnClickListener(view -> getContacts());

        btnReadSMS.setOnClickListener(view -> getSMSMessages());

        contactAdapter.setOnItemLongClickListener(this::removeContact);

        smsMessageAdapter.setOnItemLongClickListener(this::removeSMSMessage);
    }

    private void getContacts() {
        if (!hasPermission(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, READ_CONTACTS.length());
            return;
        }

        if (messageService != null) {
            List<Contact> contacts = messageService.getContacts();

            recyclerData.setAdapter(contactAdapter);
            contactAdapter.setContacts(contacts);
        }
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED;
    }

    private void getSMSMessages() {
        if (!hasPermission(READ_SMS)) {
            requestPermissions(new String[]{READ_SMS}, READ_SMS.length());
            return;
        }

        if (messageService != null) {
            List<SMSMessage> messages = messageService.getSMSMessages();

            recyclerData.setAdapter(smsMessageAdapter);
            smsMessageAdapter.setSMSMessages(messages);
        }
    }

    private void removeContact(Contact contact) {
        if (!hasPermission(WRITE_CONTACTS)) {
            requestPermissions(new String[]{WRITE_CONTACTS}, WRITE_CONTACTS.length());
            return;
        }

        if (messageService != null) {
            messageService.deleteContact(contact.getID());
            getContacts();
        }
    }

    private void removeSMSMessage(SMSMessage smsMessage) {
        if (messageService != null) {
            messageService.deleteSMSMessage(smsMessage.getID());
            getSMSMessages();
        }
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission = grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED;
        if (requestCode == READ_CONTACTS.length() && hasPermission) {
            getContacts();
            return;
        }

        if (requestCode == READ_SMS.length() && hasPermission) {
            getSMSMessages();
        }
    }

    private void startMessageService() {
        Intent intent = new Intent(this, MessageService.class);

        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindMessageService();
    }

    private void bindMessageService() {
        Intent intent = new Intent(this, MessageService.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    @Override
    protected void onDestroy() {
        stopMessageService();
        super.onDestroy();
    }

    private void stopMessageService() {
        Intent intent = new Intent(this, MessageService.class);

        stopService(intent);
    }
}