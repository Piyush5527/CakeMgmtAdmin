package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AdminHomePage extends AppCompatActivity {
    ActionBarDrawerToggle tog;
    NavigationView nv;
    TextView tv, tot_sales, upCakeOrder, monthSales, avgSales,getStartDateTV;
    FirebaseFirestore db;
    SwipeRefreshLayout refreshLayout;
    long backPressed_time;
    long Period = 2000;
    public static String stDate;
    final DecimalFormat df=new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        tv = findViewById(R.id.textView5);
        tot_sales = findViewById(R.id.tot_sales);
        upCakeOrder = findViewById(R.id.tot_cakes);
        monthSales = findViewById(R.id.mon_sales);
        avgSales = findViewById(R.id.avg_sales);
        getStartDateTV=findViewById(R.id.DateRetriever);
        refreshLayout = findViewById(R.id.refereshLayout);
        db = FirebaseFirestore.getInstance();

//        startDateRetriever();
        setData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(AdminHomePage.this, "Refreshed", Toast.LENGTH_SHORT).show();
                setData();
                refreshLayout.setRefreshing(false);
            }
        });
        DrawerLayout dl = findViewById(R.id.drawerLayout);
        tog = new ActionBarDrawerToggle(AdminHomePage.this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(tog);
        tog.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = findViewById(R.id.navView);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(AdminHomePage.this, "Home", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(AdminHomePage.this, HomePage.class);
//                        finish();
//                        startActivity(i);
                        break;
                    case R.id.new_cake:
                        Toast.makeText(AdminHomePage.this, "Opening Add New", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AdminHomePage.this, AddNewCake.class);
                        startActivity(i);
                        break;
                    case R.id.edit_cake_data:
                        Toast.makeText(AdminHomePage.this, "Opening Edit Page", Toast.LENGTH_SHORT).show();
                        Intent i2 = new Intent(AdminHomePage.this, EditDeleteCakeData.class);
                        startActivity(i2);
                        break;
                    case R.id.Show_all_Cakes:
                        Toast.makeText(AdminHomePage.this, "Showing Cakes", Toast.LENGTH_SHORT).show();
                        Intent i3 = new Intent(AdminHomePage.this, ShowAllCakes.class);
                        startActivity(i3);
                        break;
                    case R.id.new_order:
                        Toast.makeText(AdminHomePage.this, "New Order Page", Toast.LENGTH_SHORT).show();
                        Intent i4 = new Intent(AdminHomePage.this, NewCakeOrder.class);
                        startActivity(i4);
                        break;
                    case R.id.upcoming_order:
                        Toast.makeText(AdminHomePage.this, "Opening Upcoming Order", Toast.LENGTH_SHORT).show();
                        Intent i5 = new Intent(AdminHomePage.this, UpcomingOrders.class);
                        startActivity(i5);
                        break;
                    case R.id.msales:
                        Toast.makeText(AdminHomePage.this, "Opening Month Sales", Toast.LENGTH_SHORT).show();
//                        Intent i6=new Intent(AdminHomePage.this,MonthlySalesDetails.class);
//                        startActivity(i6);
                        break;
                    case R.id.aSales:
                        Toast.makeText(AdminHomePage.this, "Opening All Sales", Toast.LENGTH_SHORT).show();
//                        Intent i7=new Intent(AdminHomePage.this,AllSales.class);
//                        startActivity(i7);
                        break;
                    case R.id.totalCompletedOrder:
                        Toast.makeText(AdminHomePage.this, "Opening Total Completed Order", Toast.LENGTH_SHORT).show();
                        Intent i8 = new Intent(AdminHomePage.this, CompletedOrdersDisplay.class);
                        startActivity(i8);
                        break;
                }
                return true;
            }
        });
    }
    public void setData() {
        DocumentReference reference = db.collection("Store_Details").document("StoreData");
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    tv.setText("Hello " + snapshot.getData().get("FirstName").toString());
                }
            }
        });

        //SETTING TOTAL SALES
        db.collection("CompletedOrders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalSales = 0.0d;
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                double amount = Double.parseDouble(snapshot.getData().get("TotalPrice").toString());
                                totalSales += amount;
                            }
//                            Toast.makeText(AdminHomePage.this, String.valueOf(totalSales), Toast.LENGTH_SHORT).show();
                            tot_sales.setText(String.valueOf(totalSales));
                        }
                    }
                });
        //SETTING UPCOMING CAKE ORDERS
        db.collection("Orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int upOrder = 0;
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                upOrder++;
                            }
                            upCakeOrder.setText(String.valueOf(upOrder));
                        }
                    }
                });

        //SETTING MONTH SALES
        try {
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
//            int day=localDate.getDayOfMonth();
            int monthCalculation = 0;

            String stDt = String.valueOf(year) + "-" + String.valueOf(month);

            db.collection("CompletedOrders").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                double monthSale = 0.0d;
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    if (snapshot.getData().get("OrderDate").toString().indexOf(stDt) != -1) {
//                                        Toast.makeText(AdminHomePage.this, "True", Toast.LENGTH_SHORT).show();
                                        monthSale = Double.parseDouble(snapshot.getData().get("TotalPrice").toString()) + monthSale;
                                    }
//                                    else
//                                    {
////                                        Toast.makeText(AdminHomePage.this, "false", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                                monthSales.setText(String.valueOf(monthSale));
                            }
                        }
                    });
            //AVERAGE SALES PER DAY
            try {
                DocumentReference ref = db.collection("Store_Details").document("StoreData");
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            stDate = String.valueOf(snapshot.getData().get("StartDate"));
                            getStartDateTV.setText(stDate);
//                            Toast.makeText(AdminHomePage.this, getStartDateTV.getText(), Toast.LENGTH_SHORT).show();
//
                            db.collection("CompletedOrders").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            Date date = new Date();
                                            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                            int year = localDate.getYear();
                                            int month = localDate.getMonthValue();
                                            int day = localDate.getDayOfMonth();
                                            String strtDate=getStartDateTV.getText().toString();
                                            String endDt = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date date1 = sdf.parse(strtDate);
                                                Date date2=sdf.parse(endDt);
                                                long diffInTime=date2.getTime()-date1.getTime();
                                                float diff=(float) diffInTime/(24*60*60*1000);
                                                if (task.isSuccessful()) {
                                                    double totalSales=0.0d;
                                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                        totalSales=totalSales+Double.parseDouble(snapshot.getData().get("TotalPrice").toString());
                                                    }
                                                    double averageSales=totalSales/diff;
                                                    avgSales.setText(String.valueOf(df.format(averageSales)));
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                });
//                Toast.makeText(this, getStartDateTV.getText(), Toast.LENGTH_SHORT).show();


            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(this, ex + "", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (tog.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}