package com.example.automoto.Services;

import androidx.annotation.NonNull;

import com.example.automoto.Model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private Users users = new Users();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        updateToken(token);
        super.onNewToken(token);
    }

    private void updateToken(String Token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Riders").child(users.getPid());

        Map<String, Object> map = new HashMap<>();
        map.put("token", Token);
        databaseReference.updateChildren(map);

    }
}
