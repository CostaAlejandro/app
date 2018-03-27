package com.example.alejandro.turiaesports;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alejandro.turiaesports.objetos.FirebaseReferences;
import com.example.alejandro.turiaesports.objetos.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Seleccion extends AppCompatActivity {

    private FirebaseAuth auth;
    TextView usuario, editar, cerrar, email, dorsal, posicion;
    ImageView perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);

        auth = FirebaseAuth.getInstance();
        usuario = findViewById(R.id.tUsuario);
        email = findViewById(R.id.tEmail);
        dorsal = findViewById(R.id.tDorsal);
        posicion = findViewById(R.id.tPosicion);
        perfil = findViewById(R.id.perfil);
        editar = findViewById(R.id.tEditar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Seleccion.this, Profile.class);
                startActivity(intent);
            }
        });

        cerrar = findViewById(R.id.tCerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(Seleccion.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        loadUserInformation();
    }

    private void loadUserInformation() {
        FirebaseUser user = auth.getCurrentUser();

       if (user.getDisplayName() == null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(" ").build();
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


        if (user != null) {
            if(user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(perfil);
            }
            if(user.getDisplayName() !=null) {
                usuario.setText(user.getDisplayName());
            }
            if(user.getEmail() != null) {
                email.setText(user.getEmail());
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
