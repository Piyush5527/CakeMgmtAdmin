package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditCakeDataForm extends DrawerBaseActivity {
    EditText name, p_500, p_1000, flavour;
    Button b1;
    FirebaseFirestore db;
    DrawerLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_edit_cake_data_form, null, false);
        dl.addView(v, 0);
        Intent intent = getIntent();

        name = findViewById(R.id.editCakeName);
        p_500 = findViewById(R.id.price500Cake);
        p_1000 = findViewById(R.id.Price1kg);
        flavour = findViewById(R.id.editCakeFlavour);
        b1 = findViewById(R.id.addingCake);
        layout=findViewById(R.id.drawerLayout);



        db = FirebaseFirestore.getInstance();

//        Toast.makeText(this, intent.getStringExtra("cakeId"), Toast.LENGTH_SHORT).show();
        String cakeId = intent.getStringExtra("cakeId");

        DocumentReference docRef=db.collection("Cake_Details").document(cakeId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document=task.getResult();
                if(document.exists()){
                    Map<String,Object> cakeData=new HashMap<>();
                    cakeData=document.getData();
                    name.setText(String.valueOf(cakeData.get("CakeName")));
                    p_500.setText(String.valueOf(document.getData().get("Price500")));
                    p_1000.setText(String.valueOf(document.getData().get("Price1kg")));
                    flavour.setText(String.valueOf(document.getData().get("CakeFlavour")));
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFieldNotEmpty()) {
                    Map<String,Object> updatedCakeData=new HashMap<>();
                    updatedCakeData.put("CakeFlavour",flavour.getText().toString());
                    updatedCakeData.put("CakeName",name.getText().toString());
                    updatedCakeData.put("Price1kg",p_1000.getText().toString());
                    updatedCakeData.put("Price500",p_500.getText().toString());

                    db.collection("Cake_Details").document(cakeId)
                            .set(updatedCakeData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Snackbar.make(layout,"Updated Successfully",Snackbar.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(layout,"Something Went wrong",Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


    }

    private boolean isFieldNotEmpty() {
        if (name.length() == 0) {
            name.setError("Cannot Be Empty");
            return false;
        }
        if (p_500.length() == 0) {
            name.setError("Cannot Be Empty");
            return false;
        }
        if (p_1000.length() == 0) {
            name.setError("Cannot Be Empty");
            return false;
        }
        if (flavour.length() == 0) {
            name.setError("Cannot Be Empty");
            return false;
        }

        return true;
    }
}