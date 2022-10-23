package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CompletedOrdersDisplay extends AppCompatActivity {
    FirebaseFirestore db;
    TableLayout tl;
    ImageView iv;
    private static final int img_from_gallery = 1;
    private static int orderID;
//    String CakeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders_display);

        tl = findViewById(R.id.tableLayout);
        db = FirebaseFirestore.getInstance();

        db.collection("CompletedOrders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task != null) {
                                int cnt = 0;
                                int SNo = 1;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String custName = document.getData().get("CustName").toString();
                                    String totalPrice = document.getData().get("TotalPrice").toString();
                                    String Date = document.getData().get("OrderDate").toString();
//                                    String cakeName=cakeNameRetriever(document);
                                    DocumentReference docRef = db.collection("Cake_Details").document(document.getData().get("CakeId").toString());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot snapshot = task.getResult();
                                                if (snapshot.exists()) {
                                                    String cakeName = snapshot.getData().get("CakeName").toString();

                                                    tableDataSetter(custName, totalPrice, Date, cakeName);

                                                }
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    }
                });


    }

//    public String cakeNameRetriever(QueryDocumentSnapshot document) {
////        final String[] CakeName = new String[1];
//        DocumentReference docRef = db.collection("Cake_Details").document(document.getData().get("CakeId").toString());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot snapshot = task.getResult();
//                    if (snapshot.exists()) {
//                        CakeName = snapshot.getData().get("CakeName").toString();
////                        Toast.makeText(CompletedOrdersDisplay.this, CakeName[0], Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//        });
////        Toast.makeText(CompletedOrdersDisplay.this, CakeName, Toast.LENGTH_SHORT).show();
//        return CakeName;
//    }

    public void tableDataSetter(String custName, String totPrice, String orderDate, String cakeName) {

        TableRow row = new TableRow(this);
//            int cnt = 0;
//            if (cnt % 2 == 1) {
//                row.setBackgroundResource(R.color.grey);
//            }

        TextView tv0 = new TextView(this);
        tv0.setText(custName);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextColor(Color.BLACK);
        tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(totPrice);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(Color.BLACK);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(orderDate);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(Color.BLACK);
        tv2.setPadding(10, 0, 10, 0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(cakeName);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextColor(Color.BLACK);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv3);

        Button b1 = new Button(this);
        b1.setText("Contact");
        b1.setGravity(Gravity.CENTER);
        b1.setTextColor(Color.BLACK);
        b1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(b1);
//        b1.setId(c.getInt(0));

        Button b2 = new Button(this);
        b2.setText("Image");
        b2.setGravity(Gravity.CENTER);
        b2.setTextColor(Color.BLACK);
        b2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
//        b2.setId(c.getInt(0));
        row.addView(b2);

        Button b3 = new Button(this);
        b3.setText("View");
        b3.setGravity(Gravity.CENTER);
        b3.setTextColor(Color.BLACK);
        b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
//        b3.setId(c.getInt(0));
        row.addView(b3);

        tl.addView(row);
    }
}