package com.example.alejandro.turiaesports;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Calendario extends AppCompatActivity {

    TextView tDia, partido1, partido2, partido3;
    RadioButton rSi, rNo, rDuda;
    Button bSave, bView;
    EditText eHour;
    Switch confirm1, confirm2, confirm3;
    RadioGroup rGroup;

    FirebaseAuth auth;

    private static final int RSI_ID = 1000;
    private static final int RNO_ID = 1001;
    private static final int RDUDA_ID = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        auth = FirebaseAuth.getInstance();


        tDia = findViewById(R.id.tDia);
        Date d=new Date();
        Calendar now = Calendar.getInstance();
        Time today=new Time(Time.getCurrentTimezone());
        today.setToNow();
        int numdia=today.monthDay;

        String[] strDays = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

        SimpleDateFormat me=new SimpleDateFormat("MMMM");
        String mesString = me.format(d);

        String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + " " + numdia + " de " +  mesString;
        tDia.setText(day);

        rSi = findViewById(R.id.rSi);
        rSi.setId(RSI_ID);
        rNo = findViewById(R.id.rNo);
        rNo.setId(RNO_ID);
        rDuda = findViewById(R.id.rDuda);
        rDuda.setId(RDUDA_ID);
        rGroup = findViewById(R.id.rGroup);

        bSave = findViewById(R.id.saveP);
        bView = findViewById(R.id.viewP);

        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Calendario.this, ViewPlayers.class);
                startActivity(intent);
            }
        });

        eHour = findViewById(R.id.eHora);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveP();
            }
        });

        confirm1 = findViewById(R.id.confirm1);
        confirm2 = findViewById(R.id.confirm2);
        confirm3 = findViewById(R.id.confirm3);

        partido1 = findViewById(R.id.partido1);
        partido2 = findViewById(R.id.partido2);
        partido3 = findViewById(R.id.partido3);

        loadGames();

    }

    private void loadGames() {
        final FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(FirebaseReferences.CALENDAR_REFERENCE);

        String Hora = eHour.getText().toString();

        Date d=new Date();
        Calendar now = Calendar.getInstance();
        Time today=new Time(Time.getCurrentTimezone());
        today.setToNow();
        int numdia=today.monthDay;

        String[] strDays = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

        SimpleDateFormat me=new SimpleDateFormat("MMMM");
        String mesString = me.format(d);
        String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + " " + numdia;

        DatabaseReference dbPartido1 =
                FirebaseDatabase.getInstance().getReference()
                        .child("calendario")
                        .child(mesString)
                        .child(day)
                        .child("Partido1")
                        .child("Rival");

        dbPartido1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newPartido1 = (String) dataSnapshot.getValue();
                partido1.setText(newPartido1);

                if (newPartido1 != null) {
                    confirm1.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference dbPartido2 =
                FirebaseDatabase.getInstance().getReference()
                        .child("calendario")
                        .child(mesString)
                        .child(day)
                        .child("Partido2")
                        .child("Rival");


        dbPartido2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newPartido2 = (String) dataSnapshot.getValue();
                partido2.setText(newPartido2);

                if (newPartido2 != null) {
                    confirm2.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference dbPartido3 =
                FirebaseDatabase.getInstance().getReference()
                        .child("calendario")
                        .child(mesString)
                        .child(day)
                        .child("Partido3")
                        .child("Rival");

        dbPartido3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newPartido3 = (String) dataSnapshot.getValue();
                partido3.setText(newPartido3);

                if (newPartido3 != null) {
                    confirm3.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


     private void saveP() {
         final FirebaseUser user = auth.getCurrentUser();
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         final DatabaseReference myRef = database.getReference(FirebaseReferences.CALENDAR_REFERENCE);


         String Hora = eHour.getText().toString();
         String respuesta = "";

         Date d=new Date();
         Calendar now = Calendar.getInstance();
         Time today=new Time(Time.getCurrentTimezone());
         today.setToNow();
         int numdia=today.monthDay;

         String[] strDays = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

         SimpleDateFormat me=new SimpleDateFormat("MMMM");
         String mesString = me.format(d);
         String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + " " + numdia;

         int btn = rGroup.getCheckedRadioButtonId();
         switch (btn) {
             case RSI_ID:
                 respuesta = "Juega";
                 break;
             case RNO_ID:
                 respuesta = "No";
                 break;
             case RDUDA_ID:
                 respuesta = "Duda";
                 break;
             case -1:
                 respuesta = "Duda";
         }

         if (confirm1.isChecked() && user != null) {
             myRef.child(mesString).child(day).child("Partido1").child("Plantilla").child(user.getDisplayName()).child("Nombre1").setValue(user.getDisplayName());
         }

         if (confirm2.isChecked() && user != null) {
             myRef.child(mesString).child(day).child("Partido2").child("Plantilla").child(user.getDisplayName()).child("Nombre2").setValue(user.getDisplayName());
         }

         if (confirm3.isChecked() && user != null) {
             myRef.child(mesString).child(day).child("Partido3").child("Plantilla").child(user.getDisplayName()).child("Nombre3").setValue(user.getDisplayName());
         }


         if (user!= null) {
             myRef.child(mesString).child(day).child("Plantilla").child(user.getDisplayName()).child("NombreG").setValue(user.getDisplayName());
             myRef.child(mesString).child(day).child("Plantilla").child(user.getDisplayName()).child("HoraG").setValue(Hora);
             myRef.child(mesString).child(day).child("Plantilla").child(user.getDisplayName()).child("Estado").setValue(respuesta);

         }

         if (user!= null && rNo.isChecked()) {
             myRef.child(mesString).child(day).child("Plantilla").child(user.getDisplayName()).child("NombreG").setValue(user.getDisplayName());
             myRef.child(mesString).child(day).child("Plantilla").child(user.getDisplayName()).child("HoraG").setValue(" ");
             myRef.child(mesString).child(day).child("Plantilla").child(user.getDisplayName()).child("Estado").setValue(respuesta);
         }

    }


}
