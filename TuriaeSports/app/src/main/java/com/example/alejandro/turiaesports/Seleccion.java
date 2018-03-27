package com.example.alejandro.turiaesports;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                Intent intent = new Intent(Seleccion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loadUserInformation();

    }

    private void loadUserInformation() {
        FirebaseUser user = auth.getCurrentUser();

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
