package com.example.cakebusinessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewCakeOrder extends DrawerBaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText order_date, order_time, pr500, pr1000, custName, weight, extraPrice, totPrice, phNumber, custAdd;
    CheckBox disabler;
    Button order, calculate;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    Spinner cake;
    ArrayAdapter<String> aad;
    ArrayList<String> list = new ArrayList<>(), ids = new ArrayList<>(), price500 = new ArrayList<>(), price1Kg = new ArrayList<>();
    FirebaseFirestore db;
    static int extPrice;
    static String idOfCake;
    DrawerLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_new_cake_order, null, false);
        dl.addView(v, 0);
        layout = findViewById(R.id.drawerLayout);

        order_date = findViewById(R.id.order_date1);
        order_time = findViewById(R.id.OrderTime);
        cake = findViewById(R.id.cakeName);
        pr500 = findViewById(R.id.Price500g);
        pr1000 = findViewById(R.id.Price1000g);
        custName = findViewById(R.id.custName);
        weight = findViewById(R.id.weight);
        extraPrice = findViewById(R.id.extraPrice);
        totPrice = findViewById(R.id.totalPrice);
        phNumber = findViewById(R.id.custPhone);
        custAdd = findViewById(R.id.custAddress);
        order = findViewById(R.id.addOrder);
        disabler = findViewById(R.id.extraPriceDisabler);
        calculate = findViewById(R.id.button2);

        db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        dpd = new DatePickerDialog(NewCakeOrder.this, this, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        order_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });
        tpd = new TimePickerDialog(NewCakeOrder.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this, hour, min, false);

        order_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show();
            }
        });

        if (!disabler.isChecked()) {
            extraPrice.setEnabled(false);
        }
        disabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    extraPrice.setEnabled(true);
                } else {
                    extraPrice.setEnabled(false);
                }
            }
        });
        list.add("Select One");
        ids.add(null);
        price500.add(null);
        price1Kg.add(null);
        try {
            db.collection("Cake_Details").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task != null) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        list.add(String.valueOf(document.get("CakeName")) + " | " + String.valueOf("CakeFlavour"));
                                        ids.add(String.valueOf(document.getId()));
                                        price500.add(String.valueOf(document.get("Price500")));
                                        price1Kg.add(String.valueOf(document.get("Price1kg")));

                                    }
                                }
                            }
                        }
                    });
            aad = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
            cake.setAdapter(aad);
            cake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(NewCakeOrder.this, i+"", Toast.LENGTH_SHORT).show();
                    if (i != 0) {
                        pr500.setText(price500.get(i));
                        pr1000.setText(price1Kg.get(i));
                        idOfCake = ids.get(i);
                    } else if (i == 0) {
                        pr500.setText("");
                        pr1000.setText("");
                        idOfCake = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception ex) {
            System.out.println(ex);
            order.setEnabled(false);
        }
        if (!disabler.isChecked()) {
            extraPrice.setEnabled(false);
        }
        disabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    extraPrice.setEnabled(true);
                } else {
                    extraPrice.setEnabled(false);
                }
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(NewCakeOrder.this, "Hello", Toast.LENGTH_SHORT).show();
                if (!(weight.getText().toString().equals("")) || !(pr500.getText().toString().equals(""))) {
                    float weigh = Float.parseFloat(weight.getText().toString());
                    if (extraPrice.getText().toString().equals("")) {
                        extPrice = 0;
                    } else {
                        extPrice = Integer.parseInt(extraPrice.getText().toString());
                    }
                    if (weigh < 1) {
                        int pr_500 = Integer.parseInt(pr500.getText().toString());
                        float total_pr = pr_500 * 2 * weigh + extPrice;
                        totPrice.setText(String.valueOf(total_pr));
                    } else {
                        int pr_1000 = Integer.parseInt(pr1000.getText().toString());
                        float total_pr = pr_1000 * weigh + extPrice;
                        totPrice.setText(String.valueOf(total_pr));
                    }
                } else {
                }
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int extras;
                try {
                    if (extraPrice.length() == 0) {
                        extras = 0;
                    } else {
                        extras = Integer.parseInt(extraPrice.getText().toString());
                    }

                    if (ifFieldFilled()) {
                        String Name = custName.getText().toString();
                        String Address = custAdd.getText().toString();
                        String OrderDate = order_date.getText().toString();
                        String OrderTime = order_time.getText().toString();
                        String Phone = phNumber.getText().toString();
                        String Weight = weight.getText().toString();
                        int Extras = extras;
                        float TotPrice = Float.parseFloat(totPrice.getText().toString());
                        String cakeID = idOfCake;
                        String CakeId="/Cake_Details/"+cakeID;

                        Map<String, Object> NewOrder = new HashMap<>();
                        NewOrder.put("CustName", Name);
                        NewOrder.put("CustAddress", Address);
                        NewOrder.put("OrderDate", OrderDate);
                        NewOrder.put("OrderTime", OrderTime);
                        NewOrder.put("CustPhone", Phone);
                        NewOrder.put("Weight", Weight);
                        NewOrder.put("ExtraPrice",Extras);
                        NewOrder.put("TotalPrice", TotPrice);
                        NewOrder.put("CakeId", CakeId);

                        db.collection("Orders").document().set(NewOrder).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Snackbar.make(layout, "New Order Added", Snackbar.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(layout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } catch (Exception ex) {
                    Toast.makeText(NewCakeOrder.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ifFieldFilled() {
        if (custName.length() == 0) {
            custName.setError("Field Empty");
            return false;
        }
        if (order_date.length() == 0) {
            order_time.setError("Field Empty");
            return false;

        }
        if (order_time.length() == 0) {
            order_time.setError("Field Empty");
            return false;
        }
        if (pr500.length() == 0) {
            pr500.setError("Field Empty");
            return false;

        }
        if (pr1000.length() == 0) {
            pr1000.setError("Field Empty");
            return false;
        }
        if (weight.length() == 0) {
            weight.setError("Field Empty");
            return false;
        }
        if (totPrice.length() == 0) {
            totPrice.setError("Field Empty");
            return false;
        }
        if (phNumber.length() == 0) {
            phNumber.setError("Field Empty");
            return false;
        }
        if (custAdd.length() == 0) {
            custAdd.setError("Field Empty");
            return false;
        }

        return true;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month += 1;
        String date = year + "-" + month + "-" + dayOfMonth;
        order_date.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        order_time.setText(time);
    }
}