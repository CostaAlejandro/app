package com.example.alejandro.turiaesports;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Seleccion extends Activity {

    private FirebaseAuth auth;
    TextView usuario, editar, cerrar, email, dorsal, posicion, turia;
    ImageView perfil, marca;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);

        progressBar2 = findViewById(R.id.cargando2);
        auth = FirebaseAuth.getInstance();
        usuario = findViewById(R.id.tUsuario);
        email = findViewById(R.id.tEmail);
        dorsal = findViewById(R.id.tDorsal);
        posicion = findViewById(R.id.tPosicion);
        perfil = findViewById(R.id.perfil);
        editar = findViewById(R.id.tEditar);
        marca = findViewById(R.id.marcaB);
        turia = findViewById(R.id.turia);

        //Va a la ventana de iniciar sesión
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Seleccion.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });

        cerrar = findViewById(R.id.tCerrar);
        //Cierra sesión y vuelve a la pantalla de Log In
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(Seleccion.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        //Carga las imagenes y los datos
        loadImages();
        loadUserInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Mcalendario:
                Intent iCalendario = new Intent(Seleccion.this, Calendario.class);
                startActivity(iCalendario);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadImages() {
        //Cargar la imagen de MarcaApuestas guardada en la base de datos
        DatabaseReference dbImageMarca =
                FirebaseDatabase.getInstance().getReference()
                        .child("images")
                        .child("marcaapuestas");
        dbImageMarca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newMarca = (String) dataSnapshot.getValue();
                Glide.with(Seleccion.this).load(newMarca).into(marca);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInformation() {
        FirebaseUser user = auth.getCurrentUser();

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
                Glide.with(Seleccion.this).load(user.getPhotoUrl().toString()).into(perfil);
            }

            if(user.getDisplayName() !=null) {
                usuario.setText(user.getDisplayName());
            }
            if(user.getEmail() != null) {
                email.setText(user.getEmail());
            }
        }
        progressBar2.setVisibility(View.GONE);


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
