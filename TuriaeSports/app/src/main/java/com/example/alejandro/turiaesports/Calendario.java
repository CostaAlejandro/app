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

    TextView tDia, tPartido;
    RadioButton rSi, rNo, rDuda;
    Button bSave, bView;
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


        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveP();
            }
        });
        tPartido = findViewById(R.id.tPartido);

        loadGames();

    }

    private void loadGames() {
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
                        .child("Partido")
                        .child("Rival");

        dbPartido1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newPartido1 = (String) dataSnapshot.getValue();
                tPartido.setText(newPartido1);

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
                 respuesta = "Si";
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


         if (user != null) {
             myRef.child(mesString).child(day).child("Partido").child("Plantilla").child(user.getDisplayName()).child("Nombre").setValue(user.getDisplayName());
             myRef.child(mesString).child(day).child("Partido").child("Plantilla").child(user.getDisplayName()).child("Estado").setValue(respuesta);

         }

    }


}
