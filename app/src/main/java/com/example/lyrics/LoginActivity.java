package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private final String LOGIN_URL = "https://studev.groept.be/api/a22pt108/loginrequest/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onBtnLoginClicked(View Caller) {
        EditText usernameEditText = findViewById(R.id.usernameInput);
        String username = usernameEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.passwordInput);
        String password = passwordEditText.getText().toString();

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
                            for (int i=0;i<response.length();i++) {
                                JSONObject jsonobject = null;
                                try {
                                    jsonobject = response.getJSONObject(i);
                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    int id = jsonobject.getInt("id");
                                    Log.d("LoginAcitivty:", "userID; " + id);
                                    successfulLogin(id);
                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
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

    public void successfulLogin(int ID){
        Intent intent = new Intent(this, ProjectsListActivity.class);
        intent.putExtra("UserID", ID);
        startActivity(intent);
    }
}