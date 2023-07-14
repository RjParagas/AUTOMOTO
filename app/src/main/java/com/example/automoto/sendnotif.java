package com.example.automoto;

import static com.example.automoto.Constant.TOPIC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.automoto.Model.NotificationData;
import com.example.automoto.Model.PushNotification;
import com.example.automoto.Utility.ApiUtilities;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sendnotif extends AppCompatActivity {
    EditText mes, tit;
    Button sended;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnotif);

        mes = findViewById(R.id.messages);
        tit = findViewById(R.id.title);
        sended = findViewById(R.id.send);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
        sended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titletxt = tit.getText().toString();
                String msgTxt = mes.getText().toString();

                if (!titletxt.isEmpty() && !msgTxt.isEmpty()){
                    PushNotification notification = new PushNotification(new NotificationData(titletxt, msgTxt), TOPIC);
                    sendNotification(notification);
                }
            }
        });
    }

    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(sendnotif.this, "Success", Toast.LENGTH_SHORT).show();
                else
                Toast.makeText(sendnotif.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(sendnotif.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}