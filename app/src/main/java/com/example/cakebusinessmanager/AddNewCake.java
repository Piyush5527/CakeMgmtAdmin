package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNewCake extends DrawerBaseActivity {
    Button b, upload;
    EditText ed1, ed2, ed3, ed4;
    Spinner category;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> aad;
    String categoryOfItem;
    TextView fileName;
    ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseStorage storage;

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
        upload = findViewById(R.id.addFile);
        category = findViewById(R.id.addCakeCategory);
        fileName = findViewById(R.id.fileName);
        imageView = findViewById(R.id.imageView2);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        list.add("Cakes");
        list.add("Cup Cakes");
        list.add("Cookies");
        list.add("Pastries");
        list.add("Chocolates");
        list.add("Others");

        aad = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        category.setAdapter(aad);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryOfItem = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(AddNewCake.this, "Opening Gallery", Toast.LENGTH_SHORT).show();
                selectImage();
            }
        });
        db = FirebaseFirestore.getInstance();
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
                        ProgressDialog progressDialog = new ProgressDialog(AddNewCake.this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        String fileNameWithUUID = UUID.randomUUID().toString();
                        StorageReference ref = storageReference.child("cake icons/" + fileNameWithUUID);
                        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String generatedFilePath = uri.toString();
                                        Map<String, Object> CakeDetails = new HashMap<>();
                                        CakeDetails.put("CakeName", cakeName);
                                        CakeDetails.put("Price500", Price);
                                        CakeDetails.put("Price1kg", price_kg);
                                        CakeDetails.put("CakeFlavour", cakeFlavour);
                                        CakeDetails.put("Category",categoryOfItem);
                                        CakeDetails.put("ImageUrl",generatedFilePath);


                                        db.collection("Cake_Details").document().set(CakeDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                                                b.startAnimation(animation);
                                                Snackbar.make(findViewById(R.id.drawerLayout), "Data Added Successfully", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddNewCake.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddNewCake.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
                        ;


                    }
                } catch (Exception Ex) {
                    Ex.printStackTrace();
                }
            }
        });


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(AddNewCake.this, "In res", Toast.LENGTH_SHORT).show();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void uploadImage() {
        if (filePath != null) {
            fileName.setTextSize(12.0f);
            fileName.setText(getFileName(filePath));

            //uploading
        } else {
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri filePath) {
        String res = null;
        if (filePath.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
            try {

                if (cursor != null && cursor.moveToFirst()) {
                    res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (res == null) {
            res = filePath.getPath();
            int cut = res.lastIndexOf('/');
            if (cut != -1) {
                res = res.substring(cut + 1);
            }
        }
        return res;
    }


}