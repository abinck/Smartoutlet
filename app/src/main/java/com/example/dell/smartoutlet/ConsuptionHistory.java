package com.example.dell.smartoutlet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class ConsuptionHistory extends AppCompatActivity {

    TextView ConsUnit,ConsAmount,MaxCons,MinCons,ConsTraffic,ConsCost,ConsSubmitBt;
    double consUnit,consAmount,maxCons,minCons,consTraffic,consCost;
    String ip="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consuption_history);
        init();
        setIp();
        loadDetails();

        ConsSubmitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsuptionHistory.this,HomescreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setIp() {
        SharedPreferences sp=getSharedPreferences("ipshare",MODE_PRIVATE);
        ip=sp.getString("ip",null);
    }


    private void init() {
        ConsUnit = findViewById(R.id.consUnits);
        ConsAmount = findViewById(R.id.consAmount);
        MaxCons = findViewById(R.id.Maxcons);
        MinCons = findViewById(R.id.Mincons);
        ConsTraffic = findViewById(R.id.consTraffic);
        ConsCost = findViewById(R.id.consCost);
        ConsSubmitBt = findViewById(R.id.cDonebt);
    }

    private void loadDetails() {
        String url = "http://"+ ip +"/smartOutlet/consuptionHistory.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting user id from json array
                        JSONObject user = array.getJSONObject(0);
                        consUnit = user.getDouble("cunit_tot");
                        consAmount = user.getDouble("amount");
                        maxCons = user.getDouble("cmax");
                        minCons = user.getDouble("cmin");
                        consTraffic = user.getDouble("traffic_rate");
                        consCost = user.getDouble("total_cost");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ConsUnit.setText(String.valueOf(consUnit));
                ConsAmount.setText(String.valueOf(consAmount));
                MaxCons.setText(String.valueOf(maxCons));
                MinCons.setText(String.valueOf(minCons));
                ConsTraffic.setText(String.valueOf(consTraffic));
                ConsCost.setText(String.valueOf(consCost));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConsuptionHistory.this,""+error,Toast.LENGTH_LONG).show();

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
}
