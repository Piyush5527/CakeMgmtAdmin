package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountPage extends AppCompatActivity {
    EditText firstName, lastName, startDate, shopName, password;
    Button update;
    FirebaseFirestore db;
    ConstraintLayout layout;
    static String emailId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        firstName = findViewById(R.id.FirstNameField);
        lastName = findViewById(R.id.LastNameField);
        startDate = findViewById(R.id.DatePickerField);
        shopName = findViewById(R.id.ShopNameField);
        password = findViewById(R.id.password);
        update = findViewById(R.id.updateData);
        layout=findViewById(R.id.Constraintlayout);
        db=FirebaseFirestore.getInstance();

        DocumentReference reference=db.collection("Store_Details").document("StoreData");
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firstName.setText(String.valueOf(documentSnapshot.getData().get("FirstName")));
                lastName.setText(String.valueOf(documentSnapshot.getData().get("LastName")));
                shopName.setText(String.valueOf(documentSnapshot.getData().get("ShopName")));
                password.setText(String.valueOf(documentSnapshot.getData().get("Password")));
                startDate.setText(String.valueOf(documentSnapshot.getData().get("StartDate")));
                emailId=String.valueOf(documentSnapshot.getData().get("Email"));
//                lastName.setText(String.valueOf(documentSnapshot.getData().get("FirstName")));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFieldNotEmpty()) {
                    if(password.getText().toString().length() != 4 )
                    {
                        AlertDialog.Builder dialog=new AlertDialog.Builder(AccountPage.this);
                        dialog.setTitle("Alert!!");
                        dialog.setMessage("Password can only be of 4 characters");
                        dialog.setPositiveButton("Ok",null);
                        AlertDialog dg=dialog.create();
                        dg.show();
                    }
                    else {
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("FirstName", firstName.getText().toString());
                        updatedData.put("LastName", lastName.getText().toString());
                        updatedData.put("ShopName", shopName.getText().toString());
                        updatedData.put("Password", password.getText().toString());
                        updatedData.put("StartDate", startDate.getText().toString());
                        updatedData.put("Email", emailId);

                        db.collection("Store_Details").document("StoreData")
                                .set(updatedData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Snackbar.make(layout,"Data Updated Successfully",Snackbar.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(layout,"Something went wrong",Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                else
                {
                    Snackbar.make(layout,"Some Fields are Empty",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isFieldNotEmpty() {
        if(firstName.getText()==null)
        {
            firstName.setError("Empty");
            return false;
        }

        if(lastName.getText()==null)
        {
            lastName.setError("Empty");
            return false;
        }
        if(shopName.getText()==null)
        {
            shopName.setError("Empty");
            return false;
        }
        if(startDate.getText()==null)
        {
            startDate.setError("Empty");
            return false;
        }
        if(password.getText()==null)
        {
            password.setError("Empty");
            return false;
        }
        return true;
    }
}