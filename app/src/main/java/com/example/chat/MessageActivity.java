package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private EditText edtMessegeInput;
    private TextView txtChattingWIth;
    private ProgressBar progressBar;
    private ArrayList<Message> messages ;
    private MessageAdapter messageAdapter;
    private ImageView imgToolbar ,imgSend;
    String usernameOfTheRoomate ,emailofroomate , chatroomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        usernameOfTheRoomate = getIntent().getStringExtra("username_of_roommate");
        emailofroomate = getIntent().getStringExtra("email_of_roommate");



        recyclerView= (findViewById(R.id.recyclerMesseges));
        edtMessegeInput= (findViewById(R.id.edttext));
        txtChattingWIth= (findViewById(R.id.txtChattingWith));
        progressBar= (findViewById(R.id.progressMesseges));
       imgToolbar= (findViewById(R.id.img_toolbar));

        imgSend= (findViewById(R.id.imgSendmassege));



        txtChattingWIth.setText(usernameOfTheRoomate);
        messages = new ArrayList<>();

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatroomId).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailofroomate,edtMessegeInput.getText().toString()));
                edtMessegeInput.setText("");
            }
        });
        messageAdapter= new MessageAdapter(messages ,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roomate"),MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roomate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgToolbar);
        setUpchatRoom();
    }
    private void setUpchatRoom(){
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUsername();
                if(usernameOfTheRoomate.compareTo(myUsername)>0){
                    chatroomId = myUsername + usernameOfTheRoomate ;
                }
                else if (usernameOfTheRoomate.compareTo(myUsername)==0){
                    chatroomId = myUsername + usernameOfTheRoomate ;
                }
                else {
                    chatroomId = usernameOfTheRoomate + myUsername   ;
                }
                attachMessegeListener(chatroomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void attachMessegeListener(String chatroomId){
        FirebaseDatabase.getInstance().getReference("messages/"+chatroomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}