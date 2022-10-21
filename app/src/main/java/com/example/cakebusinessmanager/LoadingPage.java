package com.example.cakebusinessmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

public class LoadingPage extends AppCompatActivity {
    ProgressBar pb;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        pb=findViewById(R.id.loading);
        layout=findViewById(R.id.LoadingPage);
        CountDownTimer cdt=new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                pb.setVisibility(View.GONE);
                CountDownTimer cdt1=new CountDownTimer(1000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                        finish();
                        Intent i=new Intent(LoadingPage.this,PasswordAuthPage.class);
                        startActivity(i);
                    }
                };
                Snackbar.make(layout,"Connection Success Opening Password Page",Snackbar.LENGTH_SHORT).show();
                cdt1.start();
            }
        };
        cdt.start();
    }
}