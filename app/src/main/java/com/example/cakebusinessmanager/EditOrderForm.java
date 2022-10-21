package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditOrderForm extends DrawerBaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText custName, orderDate, orderTime, cakeName, extraPrice, totalPrice, custPhone, custAdd;

    Button update;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    FirebaseFirestore db;
    //    static int extPrice;
    static String idOfCake;
    DrawerLayout layout;
    String orderId;
    static float weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_edit_order_form, null, false);
        dl.addView(v, 0);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("OrderId");

        layout=findViewById(R.id.drawerLayout);

        db = FirebaseFirestore.getInstance();

        custName = findViewById(R.id.custName);
        orderDate = findViewById(R.id.order_date1);
        orderTime = findViewById(R.id.OrderTime);
        cakeName = findViewById(R.id.Cakename);
        extraPrice = findViewById(R.id.extraPrice);
        totalPrice = findViewById(R.id.totalPrice);
        custPhone = findViewById(R.id.custPhone);
        custAdd = findViewById(R.id.custAddress);
        update = findViewById(R.id.addOrder);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        dpd = new DatePickerDialog(EditOrderForm.this, this, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });
        tpd = new TimePickerDialog(EditOrderForm.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this, hour, min, false);

        orderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show();
            }
        });

        setData(orderId);

        try {


            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFieldNotEmpty()) {
                        HashMap<String, Object> updatedData = new HashMap<>();
                        updatedData.put("CustName", custName.getText().toString());
                        updatedData.put("OrderDate", orderDate.getText().toString());
                        updatedData.put("OrderTime", orderTime.getText().toString());
                        updatedData.put("TotalPrice", totalPrice.getText().toString());
                        updatedData.put("CustAddress", custAdd.getText().toString());
                        updatedData.put("CakeId", idOfCake);
                        updatedData.put("ExtraPrice", extraPrice.getText().toString());
                        updatedData.put("Weight",weight );
                        updatedData.put("CustPhone", custPhone.getText().toString());

                        db.collection("Orders").document(orderId)
                                .set(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Snackbar.make(layout,"Changes Saved Successfully",Snackbar.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(layout,"Something Went Wrong",Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        } catch (Exception ex) {

        }

    }

    public void setData(String orderId) {
        try {
//        Toast.makeText(this, orderId, Toast.LENGTH_SHORT).show();
            DocumentReference docRef = db.collection("Orders").document(orderId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            custName.setText(String.valueOf(snapshot.getData().get("CustName")));
                            orderDate.setText(String.valueOf(snapshot.getData().get("OrderDate")));
                            orderTime.setText(String.valueOf(snapshot.getData().get("OrderTime")));
                            idOfCake = String.valueOf(snapshot.getData().get("CakeId"));
                            weight=Float.parseFloat(snapshot.getData().get("Weight").toString());
//                        cakeName.setText(String.valueOf(snapshot.get("Cu")));
                            extraPrice.setText(String.valueOf(snapshot.getData().get("ExtraPrice")));
                            totalPrice.setText(String.valueOf(snapshot.getData().get("TotalPrice")));
                            custPhone.setText(String.valueOf(snapshot.getData().get("CustPhone")));
                            custAdd.setText(String.valueOf(snapshot.getData().get("CustAddress")));
                            DocumentReference ref = db.collection("Cake_Details").document(idOfCake);
                            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot snapshot = task.getResult();
                                    if (snapshot.exists()) {
                                        cakeName.setText(String.valueOf(snapshot.getData().get("CakeName")));
                                    }
                                }
                            });
                        }
                    }
                }
            });

        } catch (Exception ex) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month += 1;
        String date = year + "-" + month + "-" + dayOfMonth;
        orderDate.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        orderTime.setText(time);
    }

    private boolean isFieldNotEmpty() {
        if (custName.length() == 0) {
            custName.setError("Cannot Be Empty");
            return false;
        }
        if (orderDate.length() == 0) {
            orderDate.setError("Cannot Be Empty");
            return false;
        }
        if (orderTime.length() == 0) {
            orderTime.setError("Cannot Be Empty");
            return false;
        }
        if (cakeName.length() == 0) {
            cakeName.setError("Cannot Be Empty");
            return false;
        }
        if (extraPrice.length() == 0) {
            extraPrice.setError("Cannot Be Empty");
            return false;
        }
        if (totalPrice.length() == 0) {
            totalPrice.setError("Cannot Be Empty");
            return false;
        }
        if (custPhone.length() == 0) {
            custPhone.setError("Cannot Be Empty");
            return false;
        }
        if (custAdd.length() == 0) {
            custAdd.setError("Cannot Be Empty");
            return false;
        }

        return true;
    }
}