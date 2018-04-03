package com.example.alejandro.turiaesports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayers extends AppCompatActivity {

    TextView tGeneral, tPartido1, tPartido2, tPartido3;
    RecyclerView recycler1, recycler2, recycler3, recyclerG;
    MyAdapter adapter;
    List<Partido1Datos> listData;
    FirebaseDatabase FDB;
    DatabaseReference DBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_players);

        recycler2 = findViewById(R.id.recycler2);
        recycler3 = findViewById(R.id.recycler3);
        

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
    }

    void GetDataFirebase() {
        DBR = FDB.getReference("calendario").child("abril").child("Martes 3").child("Partido1").child("Plantilla");
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

            holder.MyText.setText(data.getNombre1());
        }

        @Override
        public int getItemCount() {
            return listArray.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView MyText;

            public MyViewHolder(View itemView) {
                super(itemView);

                MyText = itemView.findViewById(R.id.userVH);
            }
        }
    }

}
