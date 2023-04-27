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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class RegisterActivity extends AppCompatActivity {



    String REGISTER_URL = "https://studev.groept.be/api/a22pt108/registerrequest/";
    String CHECKUSERNAME_URL = "https://studev.groept.be/api/a22pt108/checkIfUsernameIsInUse/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onBtnCreateAccountClicked(View Caller) {
        EditText usernameEditText = findViewById(R.id.newUsernameInput);
        String username = usernameEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.newPasswordInput);
        String password = passwordEditText.getText().toString();
        checkIfUsernameIsInUse(username, password);
    }

    private void checkIfUsernameIsInUse(String username, String password) {
        String MODIFIED_CHECKUSERNAME_URL = CHECKUSERNAME_URL + "/" + username;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_CHECKUSERNAME_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if( response.length() != 0){
                            //user bestaat al
                            TextView theView = (TextView) findViewById(R.id.tempTextRegister);
                            String ErrorMessage = getString(R.string.usernameAlreadyExists);
                            theView.setText(ErrorMessage);
                            theView.setVisibility(View.VISIBLE);

                        }
                        else{
                            requestRegister(username, password);
                            TextView theView = (TextView) findViewById(R.id.tempTextRegister);
                            String ErrorMessage = "username bestaat nog niet";
                            theView.setText(ErrorMessage);
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


    private void requestRegister(String username, String password) {
        String MODIFIED_REGISTER_URL = REGISTER_URL + username + "/" + password;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.POST,
                MODIFIED_REGISTER_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        successfulRegister();
                        TextView theView = (TextView) findViewById(R.id.tempTextRegister);
                        String ErrorMessage = "geregistreerd";
                        theView.setText(ErrorMessage);
                        theView.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(queueRequest);
    }

    public void successfulRegister(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}