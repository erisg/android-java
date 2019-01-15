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
import android.util.EventLogTags;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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

    private ImageView SelectPostImage;
    private Button UpdatePostButton;
    EditText Descrption, Descrption1, Descrption2;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;


    private StorageReference storageReference;
    private DatabaseReference userRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        PostsRef = FirebaseDatabase.getInstance().getReference();

        SelectPostImage = (ImageView) findViewById(R.id.select_post);
        UpdatePostButton = (Button) findViewById(R.id.enviar_post);
        Descrption = (EditText) findViewById(R.id.description);
        Descrption1 = (EditText) findViewById(R.id.description1);
        Descrption2 = (EditText) findViewById(R.id.description2);
        loadingBar = new ProgressDialog(this);


        postadd = (Toolbar) findViewById(R.id.add_post);
        setSupportActionBar(postadd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("New Publicacion");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // getSupportActionBar().setHomeButtonEnabled(true);

        }
        else
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getActionBar().setHomeButtonEnabled(true);
        }

        getSupportActionBar().setTitle("Update Post");
        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void validatePost() {
        String Description = Descrption.getText().toString();
        String Description1 = Descrption1.getText().toString();
        String Description2 = Descrption2.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(getApplicationContext(), "select post image", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(getApplicationContext(), "Ubicacion del parche", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(Description1)) {
            Toast.makeText(getApplicationContext(), "Nombre del Rider", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(Description2)) {
            Toast.makeText(getApplicationContext(), "Truco", Toast.LENGTH_SHORT);
        }
        else {
            loadingBar.setTitle("new post");
            loadingBar.setMessage("Publicando");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();
        }

    }

    private void StoringImageToFirebaseStorage() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = storageReference.child("post").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");
        filePath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
                    SendUserMainActivity();
                    Uri downUri = task.getResult();
                    Toast.makeText(PostActivity.this, "save successfully", Toast.LENGTH_SHORT).show();


                    downloadUrl = downUri.toString();
                    SavingPostInformationToDatabase();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        private void SavingPostInformationToDatabase ()
        {
            userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String useremail = dataSnapshot.child("useremail").getValue().toString();

                        HashMap postsMap = new HashMap();
                        postsMap.put("uid", current_user_id);
                        postsMap.put("date", saveCurrentDate);
                        postsMap.put("time", saveCurrentTime);
                        postsMap.put("postImage", downloadUrl);
                        postsMap.put("description", Descrption);
                        postsMap.put("description1", Descrption1);
                        postsMap.put("description2", Descrption2);

                        PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful())
                                        {
                                            SendUserMainActivity();

                                            Toast.makeText(PostActivity.this, "compartida exitosamente", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();


                                        }
                                        else {
                                            Toast.makeText(PostActivity.this, "Error Ocurrred", Toast.LENGTH_SHORT).show();
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


        private void OpenGalery ()
        {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Gallery_Pick);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data)
        {

            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
                ImageUri = data.getData();
                SelectPostImage.setImageURI(ImageUri);
            }
        }


        @Override
        public boolean onOptionsItemSelected (MenuItem item)
        {
            int id = item.getItemId();

            if (id == android.R.id.home) {
                SendUserMainActivity();
            }

            return super.onOptionsItemSelected(item);
        }

        private void SendUserMainActivity () {

            Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
            startActivity(mainIntent);

        }
    }

