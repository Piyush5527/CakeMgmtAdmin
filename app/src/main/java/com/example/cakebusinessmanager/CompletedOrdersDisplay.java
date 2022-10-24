package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CompletedOrdersDisplay extends AppCompatActivity {
    FirebaseFirestore db;
    TableLayout tl;
    ImageView iv;
    private static final int img_from_gallery = 1;
    private static int orderID;
    //    String CakeName;
    HashMap<Integer, Object> cakeId = new HashMap<>();
    private int SEND_SMS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders_display);

        tl = findViewById(R.id.tableLayout);
        db = FirebaseFirestore.getInstance();

        db.collection("CompletedOrders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task != null) {
                                int cnt = 0;
                                int SNo = 1;

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    final int id = cnt;
                                    String custName = document.getData().get("CustName").toString();
                                    String totalPrice = document.getData().get("TotalPrice").toString();
                                    String Date = document.getData().get("OrderDate").toString();
                                    String oId = document.getId();
                                    DocumentReference docRef = db.collection("Cake_Details").document(document.getData().get("CakeId").toString());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot snapshot = task.getResult();
                                                if (snapshot.exists()) {
                                                    String cakeName = snapshot.getData().get("CakeName").toString();
                                                    cakeId.put(id, oId);
                                                    tableDataSetter(custName, totalPrice, Date, cakeName, id);
                                                }
                                            }
                                        }
                                    });
                                    cnt++;
                                }

                            }
                        }
                    }
                });
    }

    public void tableDataSetter(String custName, String totPrice, String orderDate, String cakeName, int orderId) {

        TableRow row = new TableRow(this);
//            int cnt = 0;
//            if (cnt % 2 == 1) {
//                row.setBackgroundResource(R.color.grey);
//            }

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

        TextView tv2 = new TextView(this);
        tv2.setText(orderDate);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(Color.BLACK);
        tv2.setPadding(10, 0, 10, 0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(cakeName);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextColor(Color.BLACK);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv3);

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
//                Toast.makeText(CompletedOrdersDisplay.this, String.valueOf(cakeId.get(orderId)), Toast.LENGTH_SHORT).show();
                PopupMenu pop = new PopupMenu(CompletedOrdersDisplay.this, b1);
                pop.getMenuInflater().inflate(R.menu.contact_menu, pop.getMenu());
                pop.show();
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.messageCustomer:

                                if (ContextCompat.checkSelfPermission(CompletedOrdersDisplay.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                                    Toast.makeText(CompletedOrdersDisplay.this, "hello1", Toast.LENGTH_SHORT).show();
                                    requestStoragePermission();
                                } else {
                                    String orId = String.valueOf(cakeId.get(orderId));
                                    Toast.makeText(CompletedOrdersDisplay.this, "hello", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(CompletedOrdersDisplay.this, orId, Toast.LENGTH_SHORT).show();
                                    String phNum = "7894561120";
//                                    Toast.makeText(CompletedOrdersDisplay.this, phNum + "", Toast.LENGTH_SHORT).show();
//                                    String msg = "Hello " + c1.getString(2) + " Your order of " + c1.getString(1) + " will be Delivered today. If this order is not given by you please contact Thanks BY:-" + c2.getString(4) + " ";
////                                        Toast.makeText(CompletedOrdersDisplay.this, msg+"", Toast.LENGTH_SHORT).show();
//                                    SmsManager sms = SmsManager.getDefault();
//                                    sms.sendTextMessage(phNum + "", "PJ", msg + "", null, null);
//                                    Snackbar.make(findViewById(R.id.bg), "Message Sent", Snackbar.LENGTH_SHORT).show();
                                }
                                break;
//                            case R.id.callCustomer:
//                                int O_id = b1.getId();
//                                Cursor c1 = db.rawQuery("select * from completedOrder where O_id=" + O_id, null);
//                                c1.moveToFirst();
//                                String phNum = c1.getString(7);
//                                Intent callIntent = new Intent(Intent.ACTION_VIEW);
//                                callIntent.setData(Uri.parse("tel:" + phNum));
//                                startActivity(callIntent);
//                                break;
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
//        b2.setId(c.getInt(0));
        row.addView(b2);

        Button b3 = new Button(this);
        b3.setText("View");
        b3.setGravity(Gravity.CENTER);
        b3.setTextColor(Color.BLACK);
        b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
//        b3.setId(c.getInt(0));
        row.addView(b3);

        tl.addView(row);
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
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, SEND_SMS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}