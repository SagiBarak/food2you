package com.sagib.food2you;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new InfoFragment()).commit();
                    return true;
                case R.id.navigation_food:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new MenuFragment()).commit();
                    return true;
                case R.id.navigation_order:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new OrderLandingFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);

        prefs = getSharedPreferences("Data", MODE_PRIVATE);
        prefs.edit().remove("Products").commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
