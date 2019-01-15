package bog.skate.eris.goskate;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private RecyclerView postList;

    // botones

    private ImageButton post_button;
    private ImageButton salir;


    // autenticacion firebase

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        postList = (RecyclerView) findViewById(R.id.reycler_view);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);



        // Post Button

        post_button = (ImageButton) findViewById(R.id.post_button);


        post_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                SendUserToPostActivity();
            }
        });

        DisplayAllUserPosts();

    }

    private void DisplayAllUserPosts()
    {

    }

    private void SendUserToPostActivity()
    {
        Intent postIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(postIntent);
    }


    @Override
   protected void onStart()
  {
      super.onStart();

      FirebaseUser currentUser = mAuth.getCurrentUser();
      if(currentUser == null)
      {
          SendUserToLoginActivity();
      }


    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        UserMenuSelector(item);
        return false;
    }

    private void UserMenuSelector(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.navigation_home:
                break;

            case R.id.navigation_parches:
                Intent a = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(a);
                break;

            case R.id.navigation_skateshop:
                Intent b = new Intent(MainActivity.this,shopActivity.class);
                startActivity(b);
                break;
        }
    }
}
