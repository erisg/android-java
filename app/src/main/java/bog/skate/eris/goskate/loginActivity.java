package bog.skate.eris.goskate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class loginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NeedNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        NeedNewAccount = (TextView) findViewById(R.id.user_nocount);
        UserEmail = (EditText) findViewById(R.id.email_login);
        UserPassword = (EditText) findViewById(R.id.password_login);
        LoginButton = (Button) findViewById(R.id.ingresar_login);

        NeedNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });
    }

    private void SendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(loginActivity.this, registroActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
