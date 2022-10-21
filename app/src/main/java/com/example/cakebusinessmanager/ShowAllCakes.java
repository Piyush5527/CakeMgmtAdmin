package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Type;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowAllCakes extends DrawerBaseActivity {
    TableLayout tl;
    FirebaseFirestore db;
    private static ArrayList<Type> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_show_all_cakes, null, false);
        dl.addView(v, 0);

        tl = findViewById(R.id.table);
        try {
            db = FirebaseFirestore.getInstance();
            db.collection("Cake_Details").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                if (task != null) {
                                    int cnt = 0;
                                    int SNo =1;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String CakeName = document.getData().get("CakeName").toString();
                                        String CakePrice500 = document.getData().get("Price500").toString();
                                        String CakePrice1kg = document.getData().get("Price1kg").toString();
                                        String Flavour = document.getData().get("CakeFlavour").toString();


                                        TableRow row=new TableRow(getApplicationContext());
                                        if(cnt%2==1) {
                                            row.setBackgroundResource(R.color.grey);
                                        }
                                        TextView tv0=new TextView(getApplicationContext());
                                        tv0.setText(String.valueOf(SNo));
                                        tv0.setGravity(Gravity.CENTER);
                                        tv0.setTextColor(Color.BLACK);
                                        tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
                                        row.addView(tv0);

                                        TextView tv1=new TextView(getApplicationContext());
                                        tv1.setText(CakeName);
                                        tv1.setGravity(Gravity.CENTER);
                                        tv1.setTextColor(Color.BLACK);
                                        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
                                        row.addView(tv1);

                                        TextView tv2=new TextView(getApplicationContext());
                                        tv2.setText(CakePrice500);
                                        tv2.setGravity(Gravity.CENTER);
                                        tv2.setTextColor(Color.BLACK);
                                        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
                                        row.addView(tv2);

                                        TextView tv3=new TextView(getApplicationContext());
                                        tv3.setGravity(Gravity.CENTER);
                                        tv3.setText(CakePrice1kg);
                                        tv3.setTextColor(Color.BLACK);
                                        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
                                        row.addView(tv3);

                                        TextView tv4=new TextView(getApplicationContext());
                                        tv4.setGravity(Gravity.CENTER);
                                        tv4.setText(Flavour);
                                        tv4.setTextColor(Color.BLACK);
                                        tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
                                        row.addView(tv4);

                                        cnt++;
                                        SNo++;
                                        tl.addView(row);
                                    }
                                }
                            }
                        }
                    });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}