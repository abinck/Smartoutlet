package com.example.dell.smartoutlet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceControlActivity extends AppCompatActivity {

    TextView bulbOnBt,bulbOffBt,bulbUnitCons,bulbCost,fanOnBt,fanOffBt,fanUnitcons,fanCost,dUpdatebt,dSubmitbt;
    ImageView bulbState,fanState;
    double bUnit,bCost,fUnit,fCost;
    int bstate,fstate;
    StringRequest stringRequest,stringRequest2;
    String ip="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        init();
        setIp();
        loadData();

        bulbOnBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bulbState.setBackgroundColor(Color.GREEN);
                bstate = 1;
            }
        });

        bulbOffBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bulbState.setBackgroundColor(Color.RED);
                bstate = 0;
            }
        });

        fanOnBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fanState.setBackgroundColor(Color.GREEN);
                fstate = 1;
            }
        });

        fanOffBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fanState.setBackgroundColor(Color.RED);
                fstate = 0;
            }
        });

        dSubmitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceControlActivity.this,HomescreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dUpdatebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

    }

    private void setIp() {
        SharedPreferences sp=getSharedPreferences("ipshare",MODE_PRIVATE);
        ip=sp.getString("ip",null);
    }


    private void update() {
//        stringRequest.cancel();
        String url = "http://"+ ip +"/smartOutlet/deviceupdate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Toast.makeText(DeviceControlActivity.this,""+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeviceControlActivity.this,""+error,Toast.LENGTH_LONG).show();

            }
        }) {

            SharedPreferences pref=getSharedPreferences("CURRENT_USER_ID",MODE_PRIVATE);
            String id = pref.getString("userId","");

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String bulbstate,fanstate;
                bulbstate=String.valueOf(bstate);
                fanstate=String.valueOf(fstate);
                Map<String,String> params = new HashMap<>();
                params.put("userid",id);
                params.put("bstate",bulbstate);
                params.put("fstate",fanstate);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);

    }

    private void loadData() {
        String url = "http://192.168.43.95/smartOutlet/deviceControl.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

//                    Toast.makeText(DeviceControlActivity.this,""+response,Toast.LENGTH_LONG).show();

                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting user id from json array
                        JSONObject user = array.getJSONObject(0);
                        bstate = user.getInt("bulb_state");
                        bUnit = user.getDouble("bc_unit");
                        bCost = user.getDouble("bcost");
                        fstate = user.getInt("fan_state");
                        fUnit = user.getDouble("fc_unit");
                        fCost = user.getDouble("fcost");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(bstate == 0)
                {
                    bulbState.setBackgroundColor(Color.RED);
                }
                else if(bstate == 1)
                {
                    bulbState.setBackgroundColor(Color.GREEN);
                }

                if(fstate == 0)
                {
                    fanState.setBackgroundColor(Color.RED);
                }
                else if(fstate == 1)
                {
                    fanState.setBackgroundColor(Color.GREEN);
                }

                bulbUnitCons.setText(String.valueOf(bUnit));
                bulbCost.setText(String.valueOf(bCost));
                fanUnitcons.setText(String.valueOf(fUnit));
                fanCost.setText(String.valueOf(fCost));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeviceControlActivity.this,""+error,Toast.LENGTH_LONG).show();

            }
        }) {

            SharedPreferences pref=getSharedPreferences("CURRENT_USER_ID",MODE_PRIVATE);
            String id = pref.getString("userId","");

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userid",id);
                return params;

            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);
    }

    private void init() {
        bulbState = findViewById(R.id.bulbstate);
        fanState = findViewById(R.id.fanstate);
        bulbOnBt = findViewById(R.id.bOnBt);
        bulbOffBt = findViewById(R.id.bOffBt);
        bulbUnitCons = findViewById(R.id.bunitcons);
        bulbCost = findViewById(R.id.bcost);
        fanOnBt = findViewById(R.id.fOnbt);
        fanOffBt = findViewById(R.id.fOffbt);
        fanUnitcons = findViewById(R.id.funitcons);
        fanCost = findViewById(R.id.fcost);
        dUpdatebt = findViewById(R.id.deviceUpdateBt);
        dSubmitbt = findViewById(R.id.deviceDoneBt);
    }
}
