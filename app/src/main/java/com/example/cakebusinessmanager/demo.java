package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class demo extends DrawerBaseActivity {
    Spinner cake;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> aad;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_demo, null, false);
        dl.addView(v, 0);

        db = FirebaseFirestore.getInstance();
        cake = findViewById(R.id.cakeName);
        list.add("Select One Cake And Flavour");

        try {
            db.collection("Cake_Details").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task != null) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        String cakeItem=String.valueOf(document.get("CakeName"))+" | "+String.valueOf(document.get("CakeFlavour"));
                                        list.add(cakeItem);
//                                        ids.add(String.valueOf(document.getId()));
//                                        price500.add(String.valueOf(document.get("Price500")));
//                                        price1Kg.add(String.valueOf(document.get("Price1kg")));

                                    }
                                }
                            }
                        }
                    });
        } catch (Exception ex) {
            System.out.println(ex);
//            order.setEnabled(false);
        }
//        list.add("temp");
//        list.add("temp2");
        aad = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        cake.setAdapter(aad);
        cake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(demo.this, "hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}