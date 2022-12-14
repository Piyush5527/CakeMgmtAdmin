package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UpcomingOrders extends DrawerBaseActivity {
    ListView upcomingOrderData;
    FirebaseFirestore db;
    String cakeNameInOrder;
    DrawerLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_upcoming_orders, null, false);
        dl.addView(v, 0);
        upcomingOrderData = findViewById(R.id.upcoming_order);
        layout = findViewById(R.id.drawerLayout);
        db = FirebaseFirestore.getInstance();
        readingData();
    }

    public void readingData() {
        db.collection("Orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task != null) {
                                int cnt = 0, i = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    cnt++;
                                }
                                String[] futureOrders = new String[cnt];
                                String[] futureOrderId = new String[cnt];
//                                Toast.makeText(UpcomingOrders.this, "Hello", Toast.LENGTH_SHORT).show();
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    DocumentReference docRef = db.collection("Cake_Details").document(String.valueOf(document.get("CakeId")));
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                            DocumentSnapshot snapshot = task1.getResult();
//
                                            if (snapshot.exists()) {
                                                Map<String, Object> cakeData = new HashMap<>();
                                                cakeData = snapshot.getData();
                                                cakeNameInOrder = String.valueOf(cakeData.get("CakeName"));
                                            }
//
                                        }
                                    });
                                    futureOrders[i] = String.valueOf(i + 1) + ". " + String.valueOf(document.get("CustName")) + " " + String.valueOf(document.get("OrderDate"));
                                    futureOrderId[i] = document.getId();
                                    i++;
                                }
                                ArrayAdapter aad = new ArrayAdapter(UpcomingOrders.this, android.R.layout.simple_list_item_1, Arrays.asList(futureOrders));
//                                Toast.makeText(UpcomingOrders.this, futureOrders[0], Toast.LENGTH_LONG).show();
                                upcomingOrderData.setAdapter(aad);

                                upcomingOrderData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                        Toast.makeText(UpcomingOrders.this, i+"", Toast.LENGTH_SHORT).show();
                                        PopupMenu popupMenu = new PopupMenu(UpcomingOrders.this, view);
                                        popupMenu.getMenuInflater().inflate(R.menu.popup2_menu, popupMenu.getMenu());
                                        popupMenu.show();

                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem menuItem) {
                                                switch (menuItem.getItemId()) {
                                                    case R.id.cancelOrder:
                                                        db.collection("Orders").document(futureOrderId[i])
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Snackbar.make(layout, "Order Canceled Successfully", Snackbar.LENGTH_SHORT).show();
                                                                        readingData();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Snackbar.make(layout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                        break;
                                                    case R.id.editOrder:
                                                        Intent intent = new Intent(UpcomingOrders.this, EditOrderForm.class);
                                                        intent.putExtra("OrderId", futureOrderId[i]);
                                                        finish();
                                                        startActivity(intent);
                                                        break;
                                                    case R.id.orderCompleted:
                                                        try {
                                                            DocumentReference docRef = db.collection("Orders").document(futureOrderId[i]);
                                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot snapshot = task.getResult();
                                                                        if (snapshot.exists()) {
                                                                            HashMap<String, Object> OrderCompleted = new HashMap<>();
                                                                            OrderCompleted.put("CustName", snapshot.getData().get("CustName").toString());
                                                                            OrderCompleted.put("CustAddress", snapshot.getData().get("CustAddress").toString());
                                                                            OrderCompleted.put("OrderDate", snapshot.getData().get("OrderDate").toString());
                                                                            OrderCompleted.put("OrderTime", snapshot.getData().get("OrderTime").toString());
                                                                            OrderCompleted.put("CustPhone", snapshot.getData().get("CustPhone").toString());
                                                                            OrderCompleted.put("Weight", snapshot.getData().get("Weight").toString());
                                                                            OrderCompleted.put("ExtraPrice", snapshot.getData().get("ExtraPrice").toString());
                                                                            OrderCompleted.put("TotalPrice", snapshot.getData().get("TotalPrice"));
                                                                            OrderCompleted.put("CakeId", snapshot.getData().get("CakeId").toString());
                                                                            OrderCompleted.put("CakeName", snapshot.getData().get("CakeName").toString());

                                                                            OrderCompleted.put("PaymentStatus",false);
                                                                            try{
                                                                                OrderCompleted.put("UserId",snapshot.getData().get("UserId").toString());
                                                                            }
                                                                            catch (Exception ex)
                                                                            {
                                                                            }
                                                                            db.collection("CompletedOrders").document().set(OrderCompleted)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            Toast.makeText(UpcomingOrders.this, "Order Completed", Toast.LENGTH_SHORT).show();

                                                                                            db.collection("Orders").document(futureOrderId[i])
                                                                                                    .delete()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void unused) {
                                                                                                            readingData();
                                                                                                        }
                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {

                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(UpcomingOrders.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });


                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        } catch (Exception ex) {
                                                            Toast.makeText(UpcomingOrders.this, "Error", Toast.LENGTH_SHORT).show();
                                                        }

                                                        break;
                                                }
                                                return true;
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });

    }
}