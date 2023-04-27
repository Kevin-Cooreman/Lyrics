package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class LoginActivity extends AppCompatActivity {

    String LOGIN_URL = "https://studev.groept.be/api/a22pt108/loginrequest/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onBtnLoginClicked(View Caller) {
        //Intent intent = new Intent(this, ProjectsListActivity.class);
        //startActivity(intent);
        EditText usernameEditText = findViewById(R.id.usernameInput);
        String username = usernameEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.passwordInput);
        String password = passwordEditText.getText().toString();


        //username & password debug
        //TextView usernameView = (TextView) findViewById(R.id.usernameView);
        //usernameView.setText(username);
        //TextView passwordView = (TextView) findViewById(R.id.passwordView);
        //passwordView.setText(password);
        //

        requestLogin(username, password);

    }

    public void onBtnRegisterClicked(View Caller) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void requestLogin(String username, String password) {
        String MODIFIED_LOGIN_URL = LOGIN_URL + username + "/" + password;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_LOGIN_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if( response.length() != 0){
                            successfulLogin();

                        }
                        else{
                            TextView theView = (TextView) findViewById(R.id.tempText);
                            String loginErrorMessage = getString(R.string.loginErrorMessage);
                            theView.setText(loginErrorMessage);
                            theView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(queueRequest);
    }

    public void successfulLogin(){
        Intent intent = new Intent(this, ProjectsListActivity.class);
        startActivity(intent);
    }
}