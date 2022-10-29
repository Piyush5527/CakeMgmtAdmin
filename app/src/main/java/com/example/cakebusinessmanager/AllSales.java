package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AllSales extends DrawerBaseActivity {
    TableLayout tl;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_monthly_sales, null, false);
        dl.addView(v, 0);

        tl = findViewById(R.id.tableLayout);
        db = FirebaseFirestore.getInstance();
        setDataOnLoad();
    }

    private void setDataOnLoad() {
        db.collection("CompletedOrders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            int cnt = 0;

                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                final int srNo = cnt + 1;
                                String custName = snapshot.getData().get("CustName").toString();
                                String totPrice = snapshot.getData().get("TotalPrice").toString();
                                String orderDate = snapshot.getData().get("OrderDate").toString();
                                DocumentReference ref = db.collection("Cake_Details").document(snapshot.getData().get("CakeId").toString());
                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            TableRow row = new TableRow(AllSales.this);
                                            TextView tv0 = new TextView(AllSales.this);
                                            tv0.setText(String.valueOf(srNo));
                                            tv0.setGravity(Gravity.CENTER);
                                            tv0.setTextColor(Color.BLACK);
                                            tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                            row.addView(tv0);

                                            TextView tv1 = new TextView(AllSales.this);
                                            tv1.setText(custName);
                                            tv1.setGravity(Gravity.CENTER);
                                            tv1.setTextColor(Color.BLACK);
                                            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                            row.addView(tv1);

                                            TextView tv2 = new TextView(AllSales.this);
                                            tv2.setText(String.valueOf(totPrice));
                                            tv2.setGravity(Gravity.CENTER);
                                            tv2.setTextColor(Color.BLACK);
                                            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                            row.addView(tv2);

                                            TextView tv3 = new TextView(AllSales.this);
                                            tv3.setGravity(Gravity.CENTER);
                                            tv3.setText(String.valueOf(task.getResult().getData().get("CakeName")));
                                            tv3.setTextColor(Color.BLACK);
                                            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                            row.addView(tv3);

                                            TextView tv4 = new TextView(AllSales.this);
                                            tv4.setGravity(Gravity.CENTER);
                                            tv4.setText(String.valueOf(orderDate));
                                            tv4.setTextColor(Color.BLACK);
                                            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                            row.addView(tv4);

                                            tl.addView(row);
                                        }
                                    }
                                });
                                cnt++;
                            }
                        }
                    }
                });
    }
}