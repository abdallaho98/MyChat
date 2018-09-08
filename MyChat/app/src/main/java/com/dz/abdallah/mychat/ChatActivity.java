package com.dz.abdallah.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    public static int i;
    private ListView listView;
    private MessageListAdapter Adapter;
    private List<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Base = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.listmessage);
        final Button button = findViewById(R.id.Sent);
        final EditText txt = findViewById(R.id.TextSent);
        messageList = new ArrayList<>();
        Adapter = new MessageListAdapter(getApplicationContext(),messageList);
        listView.setAdapter(Adapter);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                Message message = new Message(mAuth.getCurrentUser().getDisplayName().toString(), txt.getText().toString());
                if ( message.getMsg().length() != 0) {
                    Base.push();
                    Base.child(message.getTime().toString()).setValue(message.getMsg());
                    i++;
                 //   Adapter.Add(message);
                   // Adapter.notifyDataSetChanged();
                    txt.setText("");

                }
            }
        });



         Base.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                String newPost = dataSnapshot.getValue(String.class);
                Adapter.Add(new Message(mAuth.getCurrentUser().getDisplayName(),newPost));
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                String newPost = dataSnapshot.getValue(String.class);
                Adapter.Add(new Message(mAuth.getCurrentUser().getDisplayName(),newPost));
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



}