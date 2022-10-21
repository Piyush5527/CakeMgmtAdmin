package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class EditDeleteCakeData extends DrawerBaseActivity {
    ListView lv;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_edit_cake_data, null, false);
        dl.addView(v, 0);

        lv = findViewById(R.id.listView);

        db = FirebaseFirestore.getInstance();
        readingData();

    }

    public void readingData() {
        try {
            db.collection("Cake_Details").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task != null) {
                                    int cnt = 0, i = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        cnt++;
                                    }
                                    String[] cake_name = new String[cnt];
                                    String[] cake_ids = new String[cnt];
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        cake_name[i] = String.valueOf(i + 1) + ". " + document.get("CakeName").toString() + " | " + document.get("CakeFlavour").toString();
                                        cake_ids[i] = document.getId();
                                        i++;
//                                        Toast.makeText(EditDeleteCakeData.this,document.get("CakeName").toString() + " | " + document.get("CakeFlavour").toString() , Toast.LENGTH_SHORT).show();

                                    }
//                                    Toast.makeText(EditDeleteCakeData.this, cake_name[0], Toast.LENGTH_SHORT).show();
                                    ArrayAdapter aad = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, Arrays.asList(cake_name));
                                    lv.setAdapter(aad);

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            PopupMenu popupMenu = new PopupMenu(EditDeleteCakeData.this, view);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                                            popupMenu.show();
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.edit_data:
                                                            Intent intent = new Intent(EditDeleteCakeData.this, EditCakeDataForm.class);
                                                            intent.putExtra("cakeId", cake_ids[i]);
                                                            finish();
                                                            startActivity(intent);
                                                            break;

                                                        case R.id.delete_data:
                                                            db.collection("Cake_Details").document(cake_ids[i])
                                                                    .delete()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(EditDeleteCakeData.this, cake_name[i] + " Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                                            readingData();
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(EditDeleteCakeData.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                            break;
                                                    }
                                                    return true;
                                                }
                                            });


                                        }
                                    });

                                } else {
                                    Toast.makeText(EditDeleteCakeData.this, "Add Some Cakes First", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(EditDeleteCakeData.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}