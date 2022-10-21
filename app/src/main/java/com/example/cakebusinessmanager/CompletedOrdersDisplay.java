package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
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
    String CakeName;

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
                                    String cakeName=cakeNameRetriever(document);
//                                    Toast.makeText(CompletedOrdersDisplay.this, document.getData().get("CakeId").toString(), Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(CompletedOrdersDisplay.this, cakeName, Toast.LENGTH_SHORT).show();
//                                    DocumentReference docRef = db.collection("Cake_Details").document(document.getData().get("CakeId").toString());
//                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                DocumentSnapshot snapshot = task.getResult();
//                                                if(snapshot.exists())
//                                                {
//                                                    CakeName=snapshot.getData().get("CakeName").toString();
//                                                    Toast.makeText(CompletedOrdersDisplay.this, CakeName, Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        }
//                                    });


                                }

                            }
                        }
                    }
                });


    }

    public String cakeNameRetriever(QueryDocumentSnapshot document)
    {
//        final String[] CakeName = new String[1];
        DocumentReference docRef = db.collection("Cake_Details").document(document.getData().get("CakeId").toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists())
                    {
                        CakeName =snapshot.getData().get("CakeName").toString();
//                        Toast.makeText(CompletedOrdersDisplay.this, CakeName[0], Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
//        Toast.makeText(CompletedOrdersDisplay.this, CakeName, Toast.LENGTH_SHORT).show();

        return CakeName;
    }
}