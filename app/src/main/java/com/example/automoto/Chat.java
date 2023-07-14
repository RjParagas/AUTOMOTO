package com.example.automoto;

import static com.example.automoto.Constant.TOPIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.automoto.Interface.AllConstants;
import com.example.automoto.Model.Message;
import com.example.automoto.Model.NotificationData;
import com.example.automoto.Model.PushNotification;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Utility.ApiUtilities;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {


    private CircleImageView profimg;
    private TextView shopname, ridername;
    private ImageButton sendBtn;
    private Button backBtn;
    private EditText messagechat;
    private String ShopID, Auth, Chat, date, time, shopName, riderName, profusr,sid;
    private RecyclerView recyclerView;
    private DatabaseReference chats, Riders;
    private MessageAdapter messageAdapter;
    private List<Message> mChat;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        ShopID = getIntent().getStringExtra("Aid");

        Auth = FirebaseAuth.getInstance().getCurrentUser().getUid();

        chats = FirebaseDatabase.getInstance().getReference("ChatWorld");



        if (getIntent().hasExtra("ShopID")){
            shopName = getIntent().getStringExtra("sender");
            sid = getIntent().getStringExtra("ShopID");
        }else{
            shopName = getIntent().getStringExtra("sender");
           sid = getIntent().getStringExtra("ShopID");
        }

        recyclerView = findViewById(R.id.chat_data_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profimg = (CircleImageView) findViewById(R.id.profileusr);
        shopname = (TextView) findViewById(R.id.shop_names);
        ridername = (TextView) findViewById(R.id.rider_names);
        backBtn = (Button) findViewById(R.id.backBtn);
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        messagechat = (EditText) findViewById(R.id.chat_msg);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AcceptedBooking.class);
                startActivity(intent);
                overridePendingTransition(1, 1);
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg =  messagechat.getText().toString();
                String date_created = getTimeStamp();
                String date_modified = getTimeStamp();
                if (!msg.equals("")){
                    sendMessageNow(shopName, riderName, msg, date_created, date_modified);
                } else {
                    Toast.makeText(Chat.this, "Empty Message cannot be sent!", Toast.LENGTH_SHORT).show();
                    getToken(msg, shopName, riderName);
                }




                messagechat.setText("");
            }
        });
        showAllUserData();
        readMessages();
    }


    private void showAllUserData() {

        Intent intent = getIntent();
        riderName = intent.getStringExtra("RiderName");
        shopName = intent.getStringExtra("ShopName");
        profusr = intent.getStringExtra("ShopImage");

        shopname.setText(shopName);
        ridername.setText(riderName);
        Picasso.get().load(profusr).into(profimg);

    }

    private void sendMessageNow(String shopName, String riderName, String msg, String date_created, String date_modified) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", riderName);
        hashMap.put("receiver", shopName);
        hashMap.put("message", msg);
        hashMap.put("date_created", date_created);
        hashMap.put("date_created", date_modified);

        reference.child("ChatWorld").push().setValue(hashMap);
    }

    private String getTimeStamp(){
        return new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.getDefault()).format(new Date());
    }

    private void readMessages(){
        mChat = new ArrayList<>();

        chats = FirebaseDatabase.getInstance().getReference("ChatWorld");
        chats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getReceiver().equals(riderName) && (message.getSender()).equals(shopName)
                            || (message.getReceiver()).equals(shopName) && (message.getSender()).equals(riderName)){

                        mChat.add(message);
                    }

                    messageAdapter = new MessageAdapter(Chat.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class MessageAdapter extends RecyclerView.Adapter<com.example.automoto.Adapter.MessageAdapter.ViewHolder> {
        public static final int MSG_TYPE_LEFT = 0;
        public static final int MSG_TYPE_RIGHT = 1;

        private Context mContext;
        private List<Message> mChat;

        public MessageAdapter(Context mContext, List<Message> mChat) {
            this.mChat = mChat;
            this.mContext = mContext;
        }

        DatabaseReference Riders;

        @NonNull
        @Override
        public com.example.automoto.Adapter.MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == MSG_TYPE_RIGHT) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
                return new com.example.automoto.Adapter.MessageAdapter.ViewHolder(view);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
                return new com.example.automoto.Adapter.MessageAdapter.ViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull com.example.automoto.Adapter.MessageAdapter.ViewHolder holder, int position) {

            Message message = mChat.get(position);
            holder.show_message.setText(message.getMessage());

        }

        @Override
        public int getItemCount() {
            return mChat.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView show_message;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                show_message = itemView.findViewById(R.id.show_message);

            }
        }

        @Override
        public int getItemViewType(int position) {
            Riders = FirebaseDatabase.getInstance().getReference("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            if (mChat.get(position).getSender().equals(riderName)) {
                return MSG_TYPE_LEFT;
            } else {
                return MSG_TYPE_RIGHT;
            }
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void getToken(String message, String sender, String receiver){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("ShopOwners").child(ShopID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();
                String name = snapshot.child("name").getValue().toString();

                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();
                try{
                    data.put("title", name);
                    data.put("message", message);
                    data.put("sender", sender);
                    data.put("receiver", receiver);

                    data.put("to", token);
                    data.put("data", data);

                    sendNotifcation(to);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotifcation(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATIO_URL,to,response -> {

            Log.d("notification", "sendNotification:" + response);
        },error-> {
            Log.d("notificaiton", "sendNotification:" + error);
        }){
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError{
               Map<String , String> map = new HashMap<>();
               map.put("Authorization", "key+" + AllConstants.SERVER_KEY);
               map.put("Constant-Type", "application/json");
               return map;
           }
           @Override
           public String getBodyContentType(){
               return "application/json";
           }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
    }


}