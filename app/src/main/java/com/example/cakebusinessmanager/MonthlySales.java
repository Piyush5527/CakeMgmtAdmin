package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MonthlySales extends DrawerBaseActivity {
    TableLayout tl;
    TextView monthSales, Days, avgSales;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_monthly_sales, null, false);
        dl.addView(v, 0);

        tl = findViewById(R.id.tableLayout);
        monthSales=findViewById(R.id.totalmonthsales);
        Days=findViewById(R.id.Days);
        avgSales=findViewById(R.id.avgSalesThisMonth);

        db=FirebaseFirestore.getInstance();

        try {
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day=localDate.getDayOfMonth();
            final int[] monthCalculation = {0};
            String dt = String.valueOf(year) + "-" + String.valueOf(month) + "-__";

            db.collection("CompletedOrders").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                if(task!=null)
                                {
                                    int cnt = 0;
                                    for (QueryDocumentSnapshot snapshot : task.getResult())
                                    {
                                        TableRow row = new TableRow(MonthlySales.this);
                                        if (cnt % 2 == 1) {
                                            row.setBackgroundResource(R.color.grey);
                                        }
                                        TextView tv0 = new TextView(MonthlySales.this);
                                        tv0.setText(String.valueOf(cnt+1));
                                        tv0.setGravity(Gravity.CENTER);
                                        tv0.setTextColor(Color.BLACK);
                                        tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        row.addView(tv0);

                                        TextView tv1 = new TextView(MonthlySales.this);
//                                        tv1.setText(snapshot.getData().get(""));
                                        tv1.setGravity(Gravity.CENTER);
                                        tv1.setTextColor(Color.BLACK);
                                        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        row.addView(tv1);

                                        TextView tv2 = new TextView(MonthlySales.this);
//                                        tv2.setText(String.valueOf(c.getString(9)));
                                        tv2.setGravity(Gravity.CENTER);
                                        tv2.setTextColor(Color.BLACK);
                                        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        row.addView(tv2);

//                                        monthCalculation[0] +=c.getInt(9);

                                        TextView tv3 = new TextView(MonthlySales.this);
                                        tv3.setGravity(Gravity.CENTER);
//                                        tv3.setText(String.valueOf(c.getString(1)));
                                        tv3.setTextColor(Color.BLACK);
                                        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        row.addView(tv3);

                                        TextView tv4 = new TextView(MonthlySales.this);
                                        tv4.setGravity(Gravity.CENTER);
//                                        tv4.setText(String.valueOf(c.getString(4)));
                                        tv4.setTextColor(Color.BLACK);
                                        tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        row.addView(tv4);


                                        cnt++;
                                    }
                                }
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}