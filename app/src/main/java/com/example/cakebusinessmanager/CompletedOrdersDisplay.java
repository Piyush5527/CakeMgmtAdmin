package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class CompletedOrdersDisplay extends DrawerBaseActivity {
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    TableLayout tl;
    private static final int img_from_gallery = 1;
    private static int orderID;
    private final int PICK_IMAGE_REQUEST = 22;
    HashMap<Integer, Object> compOrId = new HashMap<>();
    HashMap<Integer, Object> cakeId = new HashMap<>();
    private int SEND_SMS_CODE = 1;
    private Uri filePath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_completed_orders_display, null, false);
        dl.addView(v, 0);

        tl = findViewById(R.id.tableLayout);
        imageView = findViewById(R.id.imageUploader);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        db.collection("CompletedOrders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int cnt = 0;
                            int SNo = 1;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final int id = cnt;
                                String custName = String.valueOf(document.getData().get("CustName"));
                                String totalPrice = String.valueOf(document.getData().get("TotalPrice"));
                                String Date = String.valueOf(document.getData().get("OrderDate"));
                                String oId = document.getId();
                                String ckId = String.valueOf(document.getData().get("CakeId"));
                                DocumentReference docRef = db.collection("Cake_Details").document(document.getData().get("CakeId").toString());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snapshot = task.getResult();
                                            if (snapshot.exists()) {
                                                String cakeName = snapshot.getData().get("CakeName").toString();
                                                compOrId.put(id, oId);
                                                cakeId.put(id, ckId);
                                                tableDataSetter(custName, totalPrice, Date, cakeName, id);
                                            }
                                        }
                                    }
                                });
                                cnt++;
                            }

                        }
                    }

                });
    }

    public void tableDataSetter(String custName, String totPrice, String orderDate, String cakeName, int orderId) {

        TableRow row = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(custName);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextColor(Color.BLACK);
        tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(totPrice);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(Color.BLACK);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv1);

        TextView tv3 = new TextView(this);
        tv3.setText(cakeName);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextColor(Color.BLACK);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv3);

        TextView tv2 = new TextView(this);
        tv2.setText(orderDate);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(Color.BLACK);
        tv2.setPadding(10, 0, 10, 0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv2);


        Button b1 = new Button(this);
        b1.setText("Contact");
        b1.setGravity(Gravity.CENTER);
        b1.setTextColor(Color.BLACK);
        b1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(b1);
        b1.setId(orderId);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(CompletedOrdersDisplay.this, String.valueOf(compOrId.get(orderId)), Toast.LENGTH_SHORT).show();
                PopupMenu pop = new PopupMenu(CompletedOrdersDisplay.this, b1);
                pop.getMenuInflater().inflate(R.menu.contact_menu, pop.getMenu());
                pop.show();
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.messageCustomer:

                                if (ContextCompat.checkSelfPermission(CompletedOrdersDisplay.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                    requestStoragePermission();
                                } else {
                                    String orId = String.valueOf(compOrId.get(orderId));
//                                    Toast.makeText(CompletedOrdersDisplay.this, orId, Toast.LENGTH_SHORT).show();

                                    DocumentReference ref = db.collection("CompletedOrders").document(orId);
                                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot snapshot = task.getResult();
                                                String phNum = String.valueOf(snapshot.getData().get("CustPhone"));
                                                String custName = String.valueOf(snapshot.getData().get("CustName"));
//                                                Toast.makeText(CompletedOrdersDisplay.this, phNum, Toast.LENGTH_SHORT).show();
                                                DocumentReference shopRef = db.collection("Store_Details").document("StoreData");
                                                shopRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            String shopName = String.valueOf(task.getResult().getData().get("ShopName"));
                                                            String msg = "Hello " + custName + " Your order" + " will be Delivered today. If this order is not given by you please contact Thanks BY:-" + shopName + " ";
                                                            SmsManager sms = SmsManager.getDefault();
                                                            sms.sendTextMessage(phNum + "", shopName, msg + "", null, null);
                                                            Snackbar.make(findViewById(R.id.drawerLayout), "Message Sent", Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                            }
                                        }
                                    });

//                                    String msg = "Hello " + c1.getString(2) + " Your order of " + c1.getString(1) + " will be Delivered today. If this order is not given by you please contact Thanks BY:-" + c2.getString(4) + " ";
////                                        Toast.makeText(CompletedOrdersDisplay.this, msg+"", Toast.LENGTH_SHORT).show();
//                                    SmsManager sms = SmsManager.getDefault();
//                                    sms.sendTextMessage(phNum + "", "PJ", msg + "", null, null);
//                                    Snackbar.make(findViewById(R.id.bg), "Message Sent", Snackbar.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.callCustomer:
                                String orId = String.valueOf(compOrId.get(orderId));
                                DocumentReference ref = db.collection("CompletedOrders").document(orId);
                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snapshot = task.getResult();
                                            String phNum = String.valueOf(snapshot.getData().get("CustPhone"));
                                            Intent callIntent = new Intent(Intent.ACTION_VIEW);
                                            callIntent.setData(Uri.parse("tel:" + phNum));
                                            startActivity(callIntent);

                                        }
                                    }
                                });
                        }
                        return true;
                    }
                });
            }
        });

        Button b2 = new Button(this);
        b2.setText("Image");
        b2.setGravity(Gravity.CENTER);
        b2.setTextColor(Color.BLACK);
        b2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        b2.setId(orderId);
        row.addView(b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CompletedOrdersDisplay.this, b2);
                popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.chooseImg:
                                orderID = b2.getId();
                                selectImage(String.valueOf(compOrId.get(orderID)));
//                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(Intent.createChooser(i, "Choose Action"), img_from_gallery);
                                break;

                            case R.id.viewImg:
//                                Toast.makeText(CompletedOrdersDisplay.this, "Pic View Page", Toast.LENGTH_SHORT).show();
                                String orderId = Objects.requireNonNull(compOrId.get(b2.getId())).toString();
                                DocumentReference ref = db.collection("ImageMapper").document(orderId);
                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean isImageAvailable = task.getResult().exists();
                                            if (isImageAvailable == false) {
                                                Toast.makeText(CompletedOrdersDisplay.this, "Image Not available", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(CompletedOrdersDisplay.this, "Opening Image", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CompletedOrdersDisplay.this, CakePicView.class);
                                                intent.putExtra("ImageMapperID", orderId);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });
                                break;
                        }

                        return true;
                    }
                });
            }
        });

//        Button b3 = new Button(this);
//        b3.setText("View");
//        b3.setGravity(Gravity.CENTER);
//        b3.setTextColor(Color.BLACK);
//        b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
////        b3.setId(c.getInt(0));
//        row.addView(b3);

        tl.addView(row);
    }

    private void selectImage(String orId) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
//            Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String fileNameWithUUID = UUID.randomUUID().toString();


//            Toast.makeText(this, , Toast.LENGTH_SHORT).show();
            // Defining the child of storageReference
            StorageReference ref = storageReference.child("cake images/" + fileNameWithUUID);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String generatedFilePath = uri.toString();
                                            Toast.makeText(CompletedOrdersDisplay.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                                            HashMap<String, Object> newImageMapper = new HashMap<>();
                                            newImageMapper.put("OrderId", compOrId.get(orderID));
                                            newImageMapper.put("CakeId", cakeId.get(orderID));
                                            newImageMapper.put("ImageName", fileNameWithUUID);
                                            newImageMapper.put("ImageUrl", generatedFilePath);

                                            db.collection("ImageMapper").document((String) Objects.requireNonNull(compOrId.get(orderID))).set(newImageMapper)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                        }
                                    });


                                }

                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CompletedOrdersDisplay.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for sending Message")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CompletedOrdersDisplay.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}