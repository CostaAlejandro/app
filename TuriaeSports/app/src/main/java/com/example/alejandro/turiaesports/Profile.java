package com.example.alejandro.turiaesports;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alejandro.turiaesports.objetos.FirebaseReferences;
import com.example.alejandro.turiaesports.objetos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;

    EditText usuario, dorsal, posicion;
    Button guardar;
    ImageView imageView;
    Uri uriProfileImage, newUriProfileImage;
    ProgressBar cargando;
    String profileImageUrl;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();


        usuario = findViewById(R.id.tUser);
        dorsal = findViewById(R.id.tDorsal);
        posicion = findViewById(R.id.tPosicion);

        guardar = findViewById(R.id.guardar);

        imageView = findViewById(R.id.tFoto);
        cargando = findViewById(R.id.cargando);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profile.this, "Tus muertos", Toast.LENGTH_SHORT).show();
                saveUserInfo();
            }

        });

        loadUserData();

    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        usuario.setText(user.getDisplayName());

        if(dorsal == null) {
            dorsal.setText(" ");
        }

        DatabaseReference dbDorsal =
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(user.getDisplayName())
                        .child("Dorsal");

        dbDorsal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newDorsal = (String) dataSnapshot.getValue();
                dorsal.setText(newDorsal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(posicion == null) {
            posicion.setText(" ");
        }

        DatabaseReference dbPosicion =
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(user.getDisplayName())
                        .child("Posicion");

        dbPosicion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newPosicion = (String) dataSnapshot.getValue();
                posicion.setText(newPosicion);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInfo() {
        String displayName = usuario.getText().toString();
        final String displayDorsal = dorsal.getText().toString();
        final String displayPosicion = posicion.getText().toString();

        if (displayName.isEmpty()) {
            usuario.setError("El nombre de usuario es requerido");
            usuario.requestFocus();
            return;
        }

        if (displayDorsal.isEmpty()) {
            dorsal.setError("El dorsal es requerido");
            dorsal.requestFocus();
            return;
        }

        if (displayPosicion.isEmpty()) {
            posicion.setError("La posicion es requerida");
            posicion.requestFocus();
            return;
        }


        final FirebaseUser user = mAuth.getCurrentUser();

        if (user!= null && profileImageUrl != null && displayDorsal !=null && displayPosicion !=null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageUrl)).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                     //   writeNewUser(user.getDisplayName(), displayDorsal, displayPosicion);
                        Toast.makeText(Profile.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }


    /* private void writeNewUser(String userId, String Dorsal, String Posicion) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(FirebaseReferences.USERS_REFERENCE);

        myRef.child(userId).child("Dorsal").setValue(Dorsal);
        myRef.child(userId).child("Posicion").setValue(Posicion);
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!= null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        FirebaseUser user = mAuth.getCurrentUser();
        StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profilepics/"+ user.getDisplayName() + ".jpg");

        if (uriProfileImage != null) {
            cargando.setVisibility(View.VISIBLE);
            profileImageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    cargando.setVisibility(View.GONE);
                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cargando.setVisibility(View.GONE);
                    Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen de perfil"), CHOOSE_IMAGE);
    }
}
