package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateNewProjectActivity extends AppCompatActivity {

    String addProjectUrl = "https://studev.groept.be/api/a22pt108/addNewProject/";
    String getLatestProjectFromUserUrl = "https://studev.groept.be/api/a22pt108/getLatestProjectFromUser/";
    int userID;
    int ProjectID;
    Project project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_project);
        int UserID = getIntent().getIntExtra("UserID", -1);
        Log.d("CreateNewProjectActivity", "UserID: " + UserID);
        setUserID(UserID);

    }


    public void setUserID(int UserID){this.userID = UserID;}

    public void setProject(Project project){this.project = project;}

    public void onBtnCreateProjectClicked(View Caller) {

        EditText titleEditText = findViewById(R.id.newProjectNameTextInput);
        String title = titleEditText.getText().toString();
        EditText descEditText = findViewById(R.id.newProjectDescriptionInput);
        String description = descEditText.getText().toString();
        if(title.length() == 0 || description.length() == 0){
            TextView theView = (TextView) findViewById(R.id.createNewProjectErrorMsg);
            theView.setText("fields can't be empty");
            theView.setVisibility(View.VISIBLE);
        }
        else{
        Project newProject = new Project(ProjectID, title, description, userID, "", new String());
        setProject(newProject);
        addNewProject();
        }
    }

    private void addNewProject() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                addProjectUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if( response.equals("[]")){
                            //success
                            //ga verder
                            getLatestProjectFromUserUrl();
                        }
                        else {
                            Log.d("CreateNewProjectActivity", "something bad happened: " + response);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CreateNewProjectActivity", "unable to connect to the api");
                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", project.getTitle());
                params.put("description", project.getDescription());
                params.put("owner", String.valueOf(userID));
                return params;
            }
        };

        requestQueue.add(submitRequest);
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public void getLatestProjectFromUserUrl(){
        String MODIFIED_LOGIN_URL = getLatestProjectFromUserUrl + userID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_LOGIN_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if( response.length() != 0){
                            Log.d("CreateNewProjectActivity", String.valueOf(response));
                            JSONObject jsonobject = null;
                            try {
                                jsonobject = response.getJSONObject(0);
                                int projectID = jsonobject.getInt("projectID");
                                setProjectID(projectID);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        else{
                            TextView theView = (TextView) findViewById(R.id.tempText);
                            String loginErrorMessage = getString(R.string.loginErrorMessage);
                            theView.setText(loginErrorMessage);
                            theView.setVisibility(View.VISIBLE);
                        }
                        successfulAddedProject();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(queueRequest);

    }

    public int getProjectID() {
        return ProjectID;
    }

    public void successfulAddedProject(){
        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("projectID", getProjectID());
        Log.d("CreateNewProjectActivity", "passed to next intent: userid: " + userID + ", projectID: " + getProjectID());
        startActivity(intent);
    }
}

