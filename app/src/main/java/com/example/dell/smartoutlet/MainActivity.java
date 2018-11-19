package com.example.dell.smartoutlet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.smartoutlet.Utils.CustomToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static CheckBox show_hide_password;
    private static EditText username, password;
    private static Button loginButton,signupButton;
    private static LinearLayout linearLayout;
    private static View view;
    private static Animation shakeAnimation;
    private static ImageView logo;
    private static String ip=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setIp();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {

                // If it is checked then show password else hide
                // password
                if (isChecked) {

                    show_hide_password.setText(R.string.show_pwd);// change
                    // checkbox
                    // text

                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password

                } else {
                    show_hide_password.setText(R.string.hide_pwd);// change
                    // checkbox
                    // text

                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password

                }
            }
        });

    }

    private void setIp() {
        SharedPreferences sp=getSharedPreferences("ipshare",MODE_PRIVATE);
        ip=sp.getString("ip",null);
    }


    private void initViews() {
        username = findViewById(R.id.uname);
        password = findViewById(R.id.pwd);
        loginButton = findViewById(R.id.login);
        signupButton = findViewById(R.id.signup);
        show_hide_password = findViewById(R.id.show_hide_password);
        linearLayout = findViewById(R.id.login_layout);
        logo = findViewById(R.id.mainlogo);
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
    }

    private void checkValidation() {
        // Get email id and password
        String getUsername = username.getText().toString();
        String getPassword = password.getText().toString();

        // Check for both field is empty or not
        if (getUsername.equals("") || getUsername.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            linearLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(MainActivity.this, view,"Enter both credentials.");

        }

        // Else do login and do your stuff
        else{
            String url = "http://"+ ip +"/smartOutlet/log.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(response.trim().equals("[]")){
                        logo.startAnimation(shakeAnimation);
                        new CustomToast().Show_Toast(MainActivity.this, view,"You have entered wrong email or password");

                    }
                    else{

                        SharedPreferences pref=getSharedPreferences("CURRENT_USER_ID",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting user id from json array
                                JSONObject user = array.getJSONObject(i);
                                String id = user.getString("id");
                                String name = user.getString("name");
                                editor.putString("userId",id);
                                editor.putString("username",name);
                                editor.commit();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),HomescreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
					Toast.makeText(MainActivity.this,""+error,Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("username",username.getText().toString());
                    params.put("password",password.getText().toString());
                    return params;
                }
            };
            RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
            requestqueue.add(stringRequest);
        }
    }
}
