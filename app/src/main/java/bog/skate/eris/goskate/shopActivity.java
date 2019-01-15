package bog.skate.eris.goskate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class shopActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        UserMenuSelector(item);
        return false;
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.navigation_home:
                Intent a = new Intent(shopActivity.this,MainActivity.class);
                startActivity(a);
                break;

            case R.id.navigation_parches:
                Intent b = new Intent(shopActivity.this,MapsActivity.class);
                startActivity(b);
                break;

            case R.id.navigation_skateshop:
                break;
        }
    }
}
