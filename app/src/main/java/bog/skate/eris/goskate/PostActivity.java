package bog.skate.eris.goskate;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private Toolbar postadd;
    private ProgressDialog loadingBar;

    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText truco, parche, rider;

    private static final int Gallery_Pick = 1 ;
    private Uri ImageUri;
    private String Descrption, Description1, Descrption2;

    private StorageReference PostsImageRefrence;
    private DatabaseReference userRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth= FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostsImageRefrence = FirebaseStorage.getInstance().getReference();
        userRef =FirebaseDatabase.getInstance().getReference().child("usuarios");
        PostsRef =FirebaseDatabase.getInstance().getReference().child("posts");

        SelectPostImage = (ImageButton) findViewById(R.id.select_post);
        UpdatePostButton = (Button) findViewById(R.id.enviar_post);
        truco = (EditText) findViewById(R.id.truco);
        parche = (EditText) findViewById(R.id.parche_post);
        rider = (EditText) findViewById(R.id.Rider);
        loadingBar = new ProgressDialog(this);


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
            loadingBar.setTitle("Add new post");
            loadingBar.setMessage("Subiendo Post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


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
                  downloadUrl = task.getResult().toString();
                  Toast.makeText(PostActivity.this, "Publicacion Exitosa...",Toast.LENGTH_SHORT).show();

                  SavingPostInformationToDatabase();

              }
              else
              {
                  String message = task.getException().getMessage();
                  Toast.makeText(PostActivity.this,"Error: "+ message,  Toast.LENGTH_SHORT).show();
              }
            }
        });
    }



    private void SavingPostInformationToDatabase()
    {
       userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
           {
              if (dataSnapshot.exists())
              {
                  String usercorreo = dataSnapshot.child("correo").getValue().toString();

                  HashMap postsMap = new HashMap();
                  postsMap.put("uid", current_user_id);
                  postsMap.put("date", saveCurrentDate);
                  postsMap.put("time", saveCurrentTime);
                  postsMap.put("postimage", downloadUrl);
                  postsMap.put("description", Descrption);
                  postsMap.put("description1", Description1);
                  postsMap.put("descrption2", Descrption2);

                  PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                          .addOnCompleteListener(new OnCompleteListener() {
                              @Override
                              public void onComplete(@NonNull Task task)
                              {
                                  if (task.isSuccessful())
                                  {
                                     SendMainActivity();
                                      Toast.makeText(PostActivity.this, "compartida exitosamente", Toast.LENGTH_SHORT).show();
                                      loadingBar.dismiss();
                                  }
                                  else
                                  {
                                      Toast.makeText(PostActivity.this, "Error Ocurrred",Toast.LENGTH_SHORT).show();
                                      loadingBar.dismiss();
                                  }
                              }
                          });

              }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

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
