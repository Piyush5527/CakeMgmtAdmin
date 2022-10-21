package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button start;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            fstore = FirebaseFirestore.getInstance();
            start = findViewById(R.id.startApp);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    if (!isNetworkConnected()) {
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Error!");
                        builder.setMessage("Your Device is Not Connected make sure It has active internet Connection");
                        builder.setPositiveButton("Retry", null);
                        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        fstore.collection("Store_Details").document("StoreData")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean isEmpty = task.getResult().exists();
                                            System.out.println(isEmpty);
                                            if (isEmpty == false) {
                                                Intent intent=new Intent(getApplicationContext(),ShopRegistrationPage.class);
                                                finish();
                                                startActivity(intent);
                                            } else {
//                                                Toast.makeText(MainActivity.this, "Data Exists", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(MainActivity.this,LoadingPage.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public boolean isNetworkConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception ex) {
            return false;
        }
    }
}