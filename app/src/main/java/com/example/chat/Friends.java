package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private ArrayList<User> users ;
    private ProgressBar progressBar ;
    private UsersAdapter usersAdapter;
    private SwipeRefreshLayout swipeRefreshLayout ;
    UsersAdapter.OnUserClickListener onUserClickListener ;
    String myImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler);
        users = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getusers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

       onUserClickListener = new UsersAdapter.OnUserClickListener() {
           @Override
           public void onUserClicked(int position) {
               startActivity(new Intent(Friends.this,MessageActivity.class)
                       .putExtra("username_of_roommate",users.get(position).getUsername())
                       .putExtra("email_of_roommate",users.get(position).getEmail())
                       .putExtra("img_of_roomate",users.get(position).getProfilePictures())
                       .putExtra("my_img",myImageUrl)


               );




           }
       };
       getusers();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_item_profile){
            startActivity(new Intent(Friends.this,Profile.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getusers(){
        users.clear();


        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users.add(dataSnapshot.getValue(User.class));
                }
                usersAdapter = new UsersAdapter(users,Friends.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(Friends.this));
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                for(User user : users){
                    if(user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    myImageUrl = user.getProfilePictures();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}