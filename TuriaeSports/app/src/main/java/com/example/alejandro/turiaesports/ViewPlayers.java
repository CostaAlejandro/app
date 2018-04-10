package com.example.alejandro.turiaesports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//TODO DEMAS ADAPTADORES

public class ViewPlayers extends AppCompatActivity {

    TextView tPartido1, tConfirmados;
    RecyclerView recycler1;
    MyAdapter adapter;

    int CConfirmados = 0;
    List<Partido1Datos> listData;
    FirebaseDatabase FDB;
    DatabaseReference DBR;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_players);
        tPartido1 = findViewById(R.id.tPartido1);
        tConfirmados = findViewById(R.id.tConfirmados1);

        recycler1 = findViewById(R.id.recycler1);
        recycler1.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        recycler1.setLayoutManager(LM);
        recycler1.setItemAnimator(new DefaultItemAnimator());
        recycler1.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        listData = new ArrayList<>();
        adapter = new MyAdapter(listData);

        FDB = FirebaseDatabase.getInstance();
        GetDataFirebase();
        GetGamesFirebase();
    }

    void GetGamesFirebase() {
        Date d=new Date();
        Calendar now = Calendar.getInstance();
        Time today=new Time(Time.getCurrentTimezone());
        today.setToNow();
        int numdia=today.monthDay;

        String[] strDays = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

        SimpleDateFormat me=new SimpleDateFormat("MMMM");
        String mesString = me.format(d);
        String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + " " + numdia;

        DBR = FDB.getReference("calendario").child(mesString).child(day).child("Partido").child("Rival");

        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Rival1 = (String) dataSnapshot.getValue();
                tPartido1.setText(Rival1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void GetDataFirebase() {
        Date d=new Date();
        Calendar now = Calendar.getInstance();
        Time today=new Time(Time.getCurrentTimezone());
        today.setToNow();
        int numdia=today.monthDay;

        String[] strDays = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

        SimpleDateFormat me=new SimpleDateFormat("MMMM");
        String mesString = me.format(d);
        String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + " " + numdia;

        DBR = FDB.getReference("calendario").child(mesString).child(day).child("Partido").child("Plantilla");
        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Partido1Datos data = new Partido1Datos();
                data = dataSnapshot.getValue(Partido1Datos.class);

                listData.add(data);
                recycler1.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        List<Partido1Datos> listArray;

        public MyAdapter(List<Partido1Datos> List) {
            this.listArray = List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Partido1Datos data = listArray.get(position);
            tConfirmados.setText("Asistirán: 0");

            String CEstado = data.getEstado();
            if (CEstado.equals("Si")) {
                CConfirmados = CConfirmados + 1;
                tConfirmados.setText("Asistirán: " + CConfirmados);
            }
            holder.StatusVH.setText(CEstado);
            holder.PlayerVH.setText(data.getNombre());


        }

        @Override
        public int getItemCount() {
            return listArray.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView PlayerVH;
            TextView StatusVH;

            public MyViewHolder(View itemView) {
                super(itemView);

                StatusVH = itemView.findViewById(R.id.statusVH);
                PlayerVH = itemView.findViewById(R.id.userVH);

            }
        }
    }

}
