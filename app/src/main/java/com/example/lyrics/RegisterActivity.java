package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {



    String REGISTER_URL = "https://studev.groept.be/api/a22pt108/registerrequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView theView = (TextView) findViewById(R.id.tempTextRegister);
        theView.setVisibility(View.INVISIBLE);

    }

    public void onBtnCreateAccountClicked(View Caller) {
        EditText usernameEditText = findViewById(R.id.newUsernameInput);
        String username = usernameEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.newPasswordInput);
        String password = passwordEditText.getText().toString();
        if(username.length() == 0 || password.length() == 0) {
            TextView theView = (TextView) findViewById(R.id.tempTextRegister);
            theView.setText(getString(R.string.fillUsernameAndPassword));
            theView.setVisibility(View.VISIBLE);
        }
        else {
            requestRegister(username, password);
        }
    }

    private void requestRegister(String username, String password) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if( response.equals("[]")){
                            successfulRegister();
                        }
                        else {
                            TextView theView = (TextView) findViewById(R.id.tempTextRegister);
                            if(response.contains("Duplicate entry")){
                                theView.setText(getString(R.string.usernameTaken));
                                theView.setVisibility(View.VISIBLE);
                            }
                            else{
                                theView.setText(getString(R.string.error));
                                theView.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView theView = (TextView) findViewById(R.id.tempTextRegister);
                        String loginErrorMessage = "unable to connect to the api";
                        theView.setText(loginErrorMessage);
                        theView.setVisibility(View.VISIBLE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(submitRequest);
    }

    public void successfulRegister(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}