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

public class ProfileActivity extends AppCompatActivity {

    TextView Pname,Pcons_no,Pelect_sec,Ptraffic,Psupply_vot,Pmob,Pemail,doneSubmitbt;
    private static String userpname,electricalsec,traffic,email,mobile;
    private static int consumerno,supplyvoltage;
    String ip = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setIp();
        loadDetails();

        doneSubmitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,HomescreenActivity.class);
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
        Pname = findViewById(R.id.pname);
        Pcons_no = findViewById(R.id.pcons_no);
        Pelect_sec = findViewById(R.id.pelect_sec);
        Ptraffic = findViewById(R.id.ptraffic);
        Psupply_vot = findViewById(R.id.psupply_voltage);
        Pmob = findViewById(R.id.pmob);
        Pemail = findViewById(R.id.pemail);
        doneSubmitbt = findViewById(R.id.pDone_bt);
    }

    private void loadDetails() {

        String url = "http://"+ ip +"/smartOutlet/profile.php";
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
                        userpname = user.getString("name");
                        consumerno = user.getInt("consumer_no");
                        electricalsec = user.getString("electrical_section");
                        traffic = user.getString("traffic");
                        supplyvoltage = user.getInt("supply_voltage");
                        mobile = user.getString("mobile");
                        email = user.getString("email");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Pname.setText(userpname);
                Pcons_no.setText(String.valueOf(consumerno));
                Pelect_sec.setText(electricalsec);
                Ptraffic.setText(traffic);
                Psupply_vot.setText(String.valueOf(supplyvoltage));
                Pmob.setText(mobile);
                Pemail.setText(email);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
    			Toast.makeText(ProfileActivity.this,""+error,Toast.LENGTH_LONG).show();
            }
        }) {
            SharedPreferences pref=getSharedPreferences("CURRENT_USER_ID",MODE_PRIVATE);
            String id = pref.getString("userId","");

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",id);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);
    }
}
