package bog.skate.eris.goskate;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


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
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        post_button = (ImageButton) findViewById(R.id.post_button);
        post_button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view)
            {

            }
        });

    }

    private void UserMenuSelector(MenuItem item)
    {
        Activity activity = null;
        switch (item.getItemId())
        {

            case R.id.navigation_home:
            break;

            case R.id.navigation_parches:
                break;

            case R.id.navigation_skateshop:
                break;

            case R.id.navigation_salir:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }

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



    private void SendUserToSetupActivity()
    {
        Intent  setupIntent = new Intent(MainActivity.this, MainActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
