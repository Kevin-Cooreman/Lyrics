package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

public class ProjectsListActivity extends AppCompatActivity {
    ArrayList<Integer> ProjectID = new ArrayList<>();
    ArrayList<String> ProjectTitles = new ArrayList<>();
    ArrayList<String> ProjectDescriptions = new ArrayList<>();

    String ProjectsURL = "https://studev.groept.be/api/a22pt108/selectAllProjectsFromUser/";
    String sharedProjectsURL = "https://studev.groept.be/api/a22pt108/getSharedProjects/";
    int UserID;
    RecyclerView recyclerView;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_list);
        //get the userID from the loginActivity
        int UserID = getIntent().getIntExtra("UserID", -1);
        Log.d("ProjectsListActivity", "UserID: " + UserID);
        setUserID(UserID);

        requestProjects();
        requestSharedProjects();

        recyclerView = findViewById(R.id.ProjectListView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void setUserID(int UserID){this.UserID = UserID;}

    private void requestProjects() {
        String MODIFIED_Projects_URL = ProjectsURL + UserID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_Projects_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ProjectsListActivity", " response: " + response.toString());

                        for (int i=0 ;i<response.length(); i++) {

                            JSONObject jsonobject = null;
                            try {
                                jsonobject = response.getJSONObject(i);
                            }
                            catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            String name;
                            int id;
                            String description;
                            try {
                                id = jsonobject.getInt("projectID");
                                name = jsonobject.getString("projectName");
                                description = jsonobject.getString("description");

                                Log.d("ProjectsListActivity", "ProjectList of user: " + id + ", " + name + ", " + description);
                            }
                            catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            ProjectTitles.add(name);
                            ProjectDescriptions.add(description);
                            ProjectID.add(id);

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

    private void requestSharedProjects() {
        String MODIFIED_sharedProjects_URL = sharedProjectsURL + UserID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_sharedProjects_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ProjectsListActivity", " response: " + response.toString());

                        for (int i=0 ;i<response.length(); i++) {

                            JSONObject jsonobject = null;
                            try {
                                jsonobject = response.getJSONObject(i);
                            }
                            catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            String name;
                            int id;
                            String description;
                            try {
                                id = jsonobject.getInt("projectID");
                                name = jsonobject.getString("projectName");
                                description = jsonobject.getString("description");

                                Log.d("ProjectsListActivity", "ProjectList of user: " + id + ", " + name + ", " + description);
                            }
                            catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            ProjectTitles.add(name);
                            ProjectDescriptions.add(description);
                            ProjectID.add(id);

                        }
                        ProjectsListAdapter projectsListAdapter = new ProjectsListAdapter(context, ProjectTitles, ProjectDescriptions, ProjectID);
                        recyclerView.setAdapter(projectsListAdapter);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(queueRequest);
    }

    public void onBtnCreateNewProject(View Caller) {
        Intent intent = new Intent(this, CreateNewProjectActivity.class);
        intent.putExtra("UserID", UserID);
        startActivity(intent);
    }
}