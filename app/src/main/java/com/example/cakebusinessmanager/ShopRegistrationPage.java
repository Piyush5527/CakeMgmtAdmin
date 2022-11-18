package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShopRegistrationPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText firstName, lastName, startDate, shopName, password, email;
    Button register;
    DatePickerDialog dpd;
    boolean isAllFieldChecked;
    FirebaseFirestore db;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_registration_page);
        firstName = findViewById(R.id.FirstNameField);
        lastName = findViewById(R.id.LastNameField);
        startDate = findViewById(R.id.DatePickerField);
        shopName = findViewById(R.id.ShopNameField);
        password = findViewById(R.id.password);
        register = findViewById(R.id.RegisterButton);
        email = findViewById(R.id.EmailEnterField);

        layout=findViewById(R.id.Constraintlayout);
        db=FirebaseFirestore.getInstance();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(ShopRegistrationPage.this, this, year, month, day);
        dpd.setCancelable(true);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldChecked = checkAllFields();
                if (isAllFieldChecked) {
//                    Toast.makeText(ShopRegistrationPage.this, "All Fields Ok", Toast.LENGTH_SHORT).show();
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("FirstName", firstName.getText().toString());
                    docData.put("LastName", lastName.getText().toString());
                    docData.put("StartDate", startDate.getText().toString());
                    docData.put("ShopName", shopName.getText().toString());
                    docData.put("Password", password.getText().toString());
                    docData.put("Email",email.getText().toString());

                    db.collection("Store_Details").document("StoreData").set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Snackbar.make(layout,"Account Successfully Created",Snackbar.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ShopRegistrationPage.this,AdminHomePage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(layout,"Something Went Wrong!!",Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private boolean checkAllFields() {
        if (firstName.length() == 0) {
            firstName.setError("This Field is Empty");
            return false;
        }
        if (lastName.length() == 0) {
            lastName.setError("This Field is Empty");
            return false;
        }
        if (startDate.length() == 0) {
            startDate.setError("This Field is Empty");
            return false;
        }
        if (shopName.length() == 0) {
            shopName.setError("This Field is Empty");
            return false;
        }
        if (password.length() == 0) {
            password.setError("This Field is Empty");

            return false;
        }
        if (email.length() == 0) {
            email.setError("This field is Empty");
            return false;
        }
        if (password.length() < 4) {
            AlertDialog ad;
            AlertDialog.Builder builder = new AlertDialog.Builder(ShopRegistrationPage.this);
            builder.setTitle("Alert!");
            builder.setMessage("Password Cannot be of less than 4");
            builder.setPositiveButton("Ok", null);
            ad = builder.create();
            ad.show();
            return false;
        }

        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month = month + 1;
        String Date = year + "-" + month + "-" + dayOfMonth;
        startDate.setText(Date);
    }
}