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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShareActivity extends AppCompatActivity {

    private int projectID;
    private final String userID_URL = "https://studev.groept.be/api/a22pt108/checkIfUsernameIsInUse/";
    private final String SHARE_URL = "https://studev.groept.be/api/a22pt108/shareProject/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView error = findViewById(R.id.shareErrormsgTxt);
        error.setVisibility(View.INVISIBLE);

        projectID = getIntent().getIntExtra("projectID", -1);
    }

    public void onBtnSharedClicked(View Caller) {
        EditText usernameEditText = findViewById(R.id.sharedUserInput);
        String username = usernameEditText.getText().toString();
        if(username.length() == 0) {
            TextView error = (TextView) findViewById(R.id.shareErrormsgTxt);
            error.setText(getString(R.string.fillUsernameFirst));
            error.setVisibility(View.VISIBLE);
        }
        else {
            requestUserID(username);
        }
    }

    private void requestUserID(String username) {
        String MODIFIED_userID_URL = userID_URL + username;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_userID_URL,
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
                                int id;
                                try {
                                    id = jsonobject.getInt("id");
                                    requestShare(id,projectID);

                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        else{
                            TextView error = (TextView) findViewById(R.id.shareErrormsgTxt);
                            String shareErrorMessage = "username not found";
                            error.setText(shareErrorMessage);
                            error.setVisibility(View.VISIBLE);
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

    private void requestShare(int userID, int projectID){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                SHARE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if( response.equals("[]")){
                            successfulShare();
                        }
                        else {
                            TextView error = findViewById(R.id.shareErrormsgTxt);
                            error.setText("Something bad happened");
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("newsharedid", String.valueOf(userID));
                params.put("projectid", String.valueOf(projectID));
                return params;
            }
        };

        requestQueue.add(submitRequest);
    }

    public void successfulShare(){
        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra("projectID", projectID);
        startActivity(intent);
    }
}