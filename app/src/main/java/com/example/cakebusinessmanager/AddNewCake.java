package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewCake extends DrawerBaseActivity {
    Button b;
    EditText ed1, ed2, ed3, ed4;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_add_new_cake, null, false);
        dl.addView(v, 0);

        ed1 = findViewById(R.id.addNewCake);
        ed2 = findViewById(R.id.priceNewCake);
        ed3 = findViewById(R.id.Price1kg);
        ed4 = findViewById(R.id.addCakeFlavour);

        db=FirebaseFirestore.getInstance();
        b = findViewById(R.id.addingCake);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cakeName, Price, price_kg, cakeFlavour;
                cakeName = ed1.getText().toString();
                Price = ed2.getText().toString();
                price_kg = ed3.getText().toString();
                cakeFlavour = ed4.getText().toString();
                try {
                    if (cakeName.equals("") || Price.equals("") || price_kg.equals("") || cakeFlavour.equals("")) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(AddNewCake.this);
                        adb.setTitle("Alert!!");
                        adb.setMessage("Fields Are Missing Please fill it and Try Again");
                        adb.setPositiveButton("OK", null);
                        AlertDialog ad = adb.create();
                        ad.show();
                    } else {
                        Map<String, Object> CakeDetails=new HashMap<>();
                        CakeDetails.put("CakeName",cakeName);
                        CakeDetails.put("Price500",Price);
                        CakeDetails.put("Price1kg",price_kg);
                        CakeDetails.put("CakeFlavour",cakeFlavour);

                        db.collection("Cake_Details").document().set(CakeDetails)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                                        b.startAnimation(animation);
                                        Snackbar.make(findViewById(R.id.drawerLayout), "Data Added Successfully", Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddNewCake.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } catch (Exception Ex) {
                    Ex.printStackTrace();
                }
            }
        });
    }
}