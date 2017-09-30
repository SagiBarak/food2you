package com.sagib.food2you;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_info:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new InfoFragment(), "Info").commit();
                    return true;
                case R.id.navigation_food:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new ProductsFragment(), "Products").commit();
                    return true;
                case R.id.navigation_order:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new OrderLandingFragment(), "Order").commit();
                    return true;
            }
            return false;
        }

    };

    BottomNavigationView navigation;

    BroadcastReceiver orderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int selectedItemId = navigation.getSelectedItemId();
            if (selectedItemId != R.id.navigation_order) {
                navigation.setOnNavigationItemSelectedListener(null);
                navigation.setSelectedItemId(R.id.navigation_order);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            }
        }
    };
    BroadcastReceiver productsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int selectedItemId = navigation.getSelectedItemId();
            if (selectedItemId != R.id.navigation_food) {
                navigation.setOnNavigationItemSelectedListener(null);
                navigation.setSelectedItemId(R.id.navigation_food);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);

        prefs = getSharedPreferences("Data", MODE_PRIVATE);
        prefs.edit().remove("Products").commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setBackgroundColor(Color.argb(255, 255, 137, 64));

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new InfoFragment()).commit();
    }

    @Override
    protected void onStop() {
        prefs.edit().remove("Products").commit();
        super.onStop();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(orderReceiver, new IntentFilter("OrderFragment"));
        LocalBroadcastManager.getInstance(this).registerReceiver(productsReceiver, new IntentFilter("ProductsFragment"));
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("האם ברצונך לצאת מהאפליקציה?").setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        }).setNegativeButton("לא", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
