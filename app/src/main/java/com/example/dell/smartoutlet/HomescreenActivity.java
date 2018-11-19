package com.example.dell.smartoutlet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomescreenActivity extends AppCompatActivity {

    TextView profilebt,devicebt,consumebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        init();

        profilebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        devicebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this,DeviceControlActivity.class);
                startActivity(intent);
            }
        });

        consumebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this,ConsuptionHistory.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        profilebt = findViewById(R.id.profileButton);
        devicebt = findViewById(R.id.DevicectButton);
        consumebt = findViewById(R.id.consButton);
    }
}
