package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PasswordAuthPage extends AppCompatActivity {
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, b_ok, b_clear;
    RadioButton r1, r2, r3, r4;
    ProgressBar p1;
    String enteredPassword = "";
    String passcode = "";
    int wrongPassCnt = 2, progress = 0;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_auth_page);

        b1 = findViewById(R.id.keypad1);
        b2 = findViewById(R.id.keypad2);
        b3 = findViewById(R.id.keypad3);
        b4 = findViewById(R.id.keypad4);
        b5 = findViewById(R.id.keypad5);
        b6 = findViewById(R.id.keypad6);
        b7 = findViewById(R.id.keypad7);
        b8 = findViewById(R.id.keypad8);
        b9 = findViewById(R.id.keypad9);
        b0 = findViewById(R.id.keypad0);
        b_ok = findViewById(R.id.keypadOK);
        b_clear = findViewById(R.id.keypadCLEAR);

        r1 = findViewById(R.id.radioButton);
        r2 = findViewById(R.id.radioButton2);
        r3 = findViewById(R.id.radioButton3);
        r4 = findViewById(R.id.radioButton4);

        p1 = findViewById(R.id.progressBar);

        p1.setVisibility(View.INVISIBLE);

        db=FirebaseFirestore.getInstance();


        DocumentReference docRef=db.collection("Store_Details").document("StoreData");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document=task.getResult();
                if(document.exists()){
                    Map<String,Object> storeData=new HashMap<>();
                    storeData=document.getData();
                    passcode=storeData.get("Password").toString();

                }

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "1";
                digitShow(enteredPassword);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "2";
                digitShow(enteredPassword);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "3";
                digitShow(enteredPassword);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "4";
                digitShow(enteredPassword);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "5";
                digitShow(enteredPassword);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "6";
                digitShow(enteredPassword);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "7";
                digitShow(enteredPassword);
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "8";
                digitShow(enteredPassword);
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "9";
                digitShow(enteredPassword);
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredPassword.length() != 4)
                    enteredPassword = enteredPassword + "0";
                digitShow(enteredPassword);
            }
        });
        b_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(passwordPage.this, enteredPassword + "", Toast.LENGTH_SHORT).show();
                if (passcode.equals(enteredPassword)) {
                    Intent intent = new Intent(PasswordAuthPage.this, AdminHomePage.class);
                    finish();
                    startActivity(intent);
                } else {
                    if (wrongPassCnt > 0) {

                        radioButtonDisabler(false);
                        enteredPassword = "";
                        wrongPassCnt--;
                        Toast.makeText(PasswordAuthPage.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        buttonDisabler(false);

                        p1.setVisibility(View.VISIBLE);

                        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                p1.setProgress((int) progress * 100 / (5000 / 1000));
                                progress++;
                            }

                            @Override
                            public void onFinish() {
                                buttonDisabler(true);
                                wrongPassCnt = 2;
                                enteredPassword = "";
                                radioButtonDisabler(false);
                                p1.setProgress(0);
                                progress = 0;
                                p1.setVisibility(View.INVISIBLE);
                            }
                        };
                        countDownTimer.start();
                    }
                }
            }
        });
        b_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredPassword = "";
                radioButtonDisabler(false);
            }
        });
    }

    public void radioButtonDisabler(boolean para) {
        r1.setChecked(para);
        r2.setChecked(para);
        r3.setChecked(para);
        r4.setChecked(para);
    }

    public void buttonDisabler(boolean para) {
        b1.setEnabled(para);
        b2.setEnabled(para);
        b3.setEnabled(para);
        b4.setEnabled(para);
        b5.setEnabled(para);
        b6.setEnabled(para);
        b7.setEnabled(para);
        b8.setEnabled(para);
        b9.setEnabled(para);
        b0.setEnabled(para);
        b_ok.setEnabled(para);
        b_clear.setEnabled(para);
    }

    public void digitShow(String pass) {
        int cnt = pass.length();
        switch (cnt) {
            case 1:
                r1.setChecked(true);
                break;
            case 2:
                r2.setChecked(true);
                break;
            case 3:
                r3.setChecked(true);
                break;
            case 4:
                r4.setChecked(true);
                break;
        }
    }

}
