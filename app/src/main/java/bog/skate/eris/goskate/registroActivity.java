package bog.skate.eris.goskate;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registroActivity extends AppCompatActivity
{

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();


        UserEmail = (EditText) findViewById(R.id.email);
        UserPassword = (EditText) findViewById(R.id.password1);
        UserConfirmPassword = (EditText) findViewById(R.id.passwordp);
        CreateAccountButton = (Button) findViewById(R.id.registrar_user);


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              CreateNewAcount();
            }
        });
    }

    private void CreateNewAcount()
    {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Ingresa email...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Ingresa contraseña...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this,"Comfirmar contraseña...",Toast.LENGTH_SHORT).show();
        }
        else if (password != confirmPassword)
        {
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                          if (task.isSuccessful()){
                              Toast.makeText(registroActivity.this, "Autenticacion Exitosa...",Toast.LENGTH_SHORT).show();
                          }
                          else
                              {
                                  String message = task.getException().getMessage();
                                  Toast.makeText(registroActivity.this,"Error: " +message, Toast.LENGTH_SHORT).show();
                              }
                        }
                    });
        }
    }
}
