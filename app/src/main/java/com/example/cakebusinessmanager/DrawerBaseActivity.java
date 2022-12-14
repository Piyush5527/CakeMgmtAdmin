package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity {

    ActionBarDrawerToggle tog;
    NavigationView navView;
    DrawerLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);

        dl=findViewById(R.id.drawerLayout);
        tog = new ActionBarDrawerToggle(DrawerBaseActivity.this, dl, 0,0);
        tog.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView=findViewById(R.id.navView);
        dl.setDrawerListener(tog);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(DrawerBaseActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(DrawerBaseActivity.this, AdminHomePage.class);
                        finish();
                        startActivity(i);
                        break;
                    case R.id.new_cake:
                        Toast.makeText(DrawerBaseActivity.this, "Opening Add New", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(DrawerBaseActivity.this, AddNewCake.class);
                        finish();
                        startActivity(i1);
                        break;
                    case R.id.edit_cake_data:
                        Toast.makeText(DrawerBaseActivity.this, "Opening Edit Page", Toast.LENGTH_SHORT).show();
                        Intent i2 = new Intent(DrawerBaseActivity.this, EditDeleteCakeData.class);
                        finish();
                        startActivity(i2);
                        break;
                    case R.id.Show_all_Cakes:
                        Toast.makeText(DrawerBaseActivity.this, "Showing Cakes", Toast.LENGTH_SHORT).show();
                        Intent i3 = new Intent(DrawerBaseActivity.this, ShowAllCakes.class);
                        finish();
                        startActivity(i3);
                        break;
                    case R.id.new_order:
                        Toast.makeText(DrawerBaseActivity.this, "New Order Page", Toast.LENGTH_SHORT).show();
                        Intent i4 = new Intent(DrawerBaseActivity.this, NewCakeOrder.class);
                        finish();
                        startActivity(i4);
                        break;
                    case R.id.upcoming_order:
                        Toast.makeText(DrawerBaseActivity.this, "Opening Upcoming Order", Toast.LENGTH_SHORT).show();
                        Intent i5 = new Intent(DrawerBaseActivity.this, UpcomingOrders.class);
                        finish();
                        startActivity(i5);
                        break;
                    case R.id.msales:
                        Toast.makeText(DrawerBaseActivity.this, "Opening Month Sales", Toast.LENGTH_SHORT).show();
                        Intent i6=new Intent(DrawerBaseActivity.this,MonthlySales.class);
                        finish();
                        startActivity(i6);
                        break;
                    case R.id.aSales:
                        Toast.makeText(DrawerBaseActivity.this, "Opening All Sales", Toast.LENGTH_SHORT).show();
                        Intent i7=new Intent(DrawerBaseActivity.this,AllSales.class);
                        finish();
                        startActivity(i7);
                        break;
                    case R.id.totalCompletedOrder:
                        Toast.makeText(DrawerBaseActivity.this, "Opening Total Completed Order", Toast.LENGTH_SHORT).show();
                        Intent i8=new Intent(DrawerBaseActivity.this,CompletedOrdersDisplay.class);
                        finish();
                        startActivity(i8);
                        break;
//                    case R.id.setExtraDetails:
//                        Toast.makeText(DrawerBaseActivity.this, "Opening Maps", Toast.LENGTH_SHORT).show();
//                        Intent i9=new Intent(DrawerBaseActivity.this,CompletedOrdersDisplay.class);
//                        finish();
//                        startActivity(i9);
//                        break;
                }
                return true;
            }
        });

        
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