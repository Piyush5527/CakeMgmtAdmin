package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CakePicView extends AppCompatActivity {
    String mapperId;
    ImageView imageOfCake;
    FirebaseFirestore db;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_pic_view);
        Intent intent=getIntent();
        mapperId=intent.getStringExtra("ImageMapperID");
        imageOfCake=findViewById(R.id.cakeImage);
        db=FirebaseFirestore.getInstance();
//        storageReference= FirebaseStorage.getInstance().getReference("cake images");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Retrieving...");
        progressDialog.show();
        DocumentReference reference=db.collection("ImageMapper").document(mapperId);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    String imageUUID=String.valueOf(task.getResult().get("ImageName"));
                    storageReference= FirebaseStorage.getInstance().getReference().child("cake images/"+imageUUID);

                    try {
                        final File localFile=File.createTempFile("CakeImage",".jpg");
                        storageReference.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(CakePicView.this, "Pic Retrieved", Toast.LENGTH_SHORT).show();
                                        Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageOfCake.setImageBitmap(bitmap);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(CakePicView.this,"Error", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                        progressDialog.setMessage("Retrieved " + (int) progress + "%");
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}