package bog.skate.eris.goskate;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.style.UpdateAppearance;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private Toolbar postadd;

    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText truco, parche, rider;

    private static final int Gallery_Pick = 1 ;
    private Uri ImageUri;
    private String Descrption, Description1, Descrption2;

    private StorageReference PostsImageRefrence;

    private String saveCurrentDate, saveCurrentTime, postRandomName;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        PostsImageRefrence = FirebaseStorage.getInstance().getReference();

        SelectPostImage = (ImageButton) findViewById(R.id.select_post);
        UpdatePostButton = (Button) findViewById(R.id.enviar_post);
        truco = (EditText) findViewById(R.id.truco);
        parche = (EditText) findViewById(R.id.parche_post);
        rider = (EditText) findViewById(R.id.Rider);


        postadd = (Toolbar) findViewById(R.id.add_post);
        setSupportActionBar(postadd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("New Post");


        SelectPostImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              OpenGalery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePost();
            }
        });

    }

    private void validatePost()
    {
        String Description = parche.getText().toString();
        String Description1 = rider.getText().toString();
        String Description2 = truco.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "select post image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Ubicacion del parche", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description1))
        {
            Toast.makeText(this, "Nombre del Rider", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description2))
        {
            Toast.makeText(this, "Truco", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoringImageToFirebaseStorage();
        }

    }

    private void StoringImageToFirebaseStorage()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;


        StorageReference filePath = PostsImageRefrence.child("post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
              if (task.isSuccessful())
              {
                  Toast.makeText(PostActivity.this, "Publicacion Exitosa...",Toast.LENGTH_SHORT).show();

              }
              else
              {
                  String message = task.getException().getMessage();
                  Toast.makeText(PostActivity.this,"Error: "+ message,  Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    private void OpenGalery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            SendMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void SendMainActivity() {

        Intent mainIntent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(mainIntent);
    }
}
