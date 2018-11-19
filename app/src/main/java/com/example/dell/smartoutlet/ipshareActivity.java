package com.example.dell.smartoutlet;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ipshareActivity extends AppCompatActivity {

    SharedPreferences sp;
    String ipaddr="";
    public static final String IPADDRESS="ipshare";
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipshare);

        checkPermission();

        sp=getSharedPreferences(IPADDRESS,MODE_PRIVATE);
        if(sp.contains("ip")){
            ipaddr=sp.getString("ip","");
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View mView = layoutInflater.inflate(R.layout.main_ip,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(mView);

        editText = (EditText) mView.findViewById(R.id.editText);
        editText.setText(ipaddr);

        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ipaddr = editText.getText().toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ip",ipaddr);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void checkPermission() {
        ActivityCompat.requestPermissions(ipshareActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
    }
}
