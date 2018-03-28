package com.example.alejandro.turiaesports;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    TextView tDia;
    RadioButton rSi, rNo;
    Button bSave, bView;
    EditText eHour;
    RadioGroup rGroup;

    FirebaseAuth auth;


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
        rNo = findViewById(R.id.rNo);

        bSave = findViewById(R.id.saveP);
        bView = findViewById(R.id.viewP);

        eHour = findViewById(R.id.eHora);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveP();
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch(view.getId()) {
            case R.id.rSi:
                if (checked = true) {

                }
        }
    }

     public void saveP() {
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

         myRef.child(mesString).child(day).child(user.getDisplayName()).child("Hora").setValue(Hora);

         //if (user != null && Hora != null && )

    }


}
