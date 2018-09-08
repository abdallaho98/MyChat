package com.dz.abdallah.mychat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity  {



    private DatabaseReference Base;
    private static final String TAG = "Chat";
    private FirebaseAuth mAuth;
    private ListView listView;
    private MessageListAdapter Adapter;
    private List<Message> messageList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        Base = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.listmessage);
        final Button button = findViewById(R.id.Sent);
        final EditText txt = findViewById(R.id.TextSent);
        messageList = new ArrayList<>();
        final Intent intent = new Intent(this,ChatActivity.class);
        Adapter = new MessageListAdapter(getApplicationContext(),messageList,mAuth.getCurrentUser().getDisplayName());
        listView.setAdapter(Adapter);

        registerForContextMenu(listView);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                Message message = new Message(mAuth.getCurrentUser().getDisplayName().toString(), txt.getText().toString().trim());
                if ( message.getMsg().length() != 0) {
                    Base.push();
                    /*Base.child(message.getTime().toString()).setValue(message.getMsg());*/
                    Base.child(message.getTime().toString()).setValue(message);

                 //   Adapter.Add(message);
                   // Adapter.notifyDataSetChanged();
                    txt.setText("");

                }
            }
        });




         Base.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Message message = new Message(dataSnapshot.child("user").getValue().toString(),dataSnapshot.child("msg").getValue().toString());
                message.setTime(dataSnapshot.child("time").getValue(Long.class));
                Adapter.Add(message);
                Adapter.notifyDataSetChanged();

                if(!message.getUser().equals(mAuth.getCurrentUser().getDisplayName())) {
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(ChatActivity.this);
                        PendingIntent pendingIntent = PendingIntent.getActivity(ChatActivity.this, 0, intent, 0);

                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setAutoCancel(true);
                        mBuilder.setSmallIcon(R.drawable.messagenotif);
                        mBuilder.setContentTitle(message.getUser());
                        mBuilder.setContentText(message.getMsg());

                        NotificationManager mNotificationManager =

                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        mNotificationManager.notify(001, mBuilder.build());
                }
            }


             @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                 Message message = new Message(dataSnapshot.child("user").getValue().toString(), dataSnapshot.child("msg").getValue().toString());
                 message.setTime(dataSnapshot.child("time").getValue(Long.class));
                 Adapter.Add(message);
                 Adapter.notifyDataSetChanged();

             }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (item.getTitle() == "Delete")
        {
            Message message = (Message) Adapter.getItem(index);
            Base.child(message.getTime().toString()).removeValue();
            Adapter.Delete(index);
            Adapter.notifyDataSetChanged();

        }
         return true;
    }
}