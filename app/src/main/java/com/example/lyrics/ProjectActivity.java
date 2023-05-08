package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectActivity extends AppCompatActivity {

    TextView title;
    int projectID;
    String selectProjectURL = "https://studev.groept.be/api/a22pt108/selectProjectWithID/";
    Context context = this;
    Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    String projectTitle;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //import all data from Project
        //Project project = (Project) getIntent().getParcelableExtra("Project");
        int projectID = getIntent().getIntExtra("projectID", -1);
        setProjectID(projectID);
        Log.d("ProjectActivity", "ProjectID: " + String.valueOf(projectID));

        recyclerView = findViewById(R.id.ProjectView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        requestProject();


    }

    private void requestProject() {
        String MODIFIED_Projects_URL = selectProjectURL + projectID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_Projects_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ProjectActivity", " response: " + response.toString());

                        for (int i=0 ;i<response.length(); i++) {

                            JSONObject jsonobject = null;
                            try {
                                jsonobject = response.getJSONObject(i);
                            }
                            catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            int projectID;
                            String projectName;
                            String description;
                            int ownerID;
                            String blockText;
                            String blockTypes;

                            try {
                                projectID = jsonobject.getInt("projectID");
                                projectName = jsonobject.getString("projectName");
                                description = jsonobject.getString("description");
                                ownerID = jsonobject.getInt("ownerID");
                                blockText = jsonobject.getString("blockText");
                                blockTypes = jsonobject.getString("blockTypes");

                            }
                            catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Project project = new Project(projectID, projectName, description, ownerID, blockText, blockTypes);
                            Log.d("ProjectActivity", "In request: "+ project.toString());
                            setProject(project);
                        }
                        ProjectAdapter projectAdapter = new ProjectAdapter(context, project);
                        recyclerView.setAdapter(projectAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(queueRequest);
    }

}