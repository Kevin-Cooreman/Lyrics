package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectActivity extends AppCompatActivity {

    TextView title;
    int projectID;
    String selectProjectURL = "https://studev.groept.be/api/a22pt108/selectProjectWithID/";
    Context context = this;
    Project project;
    String saveURL = "https://studev.groept.be/api/a22pt108/UpdateBlocks";


    public void setProject(Project project) {
        this.project = project;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    RecyclerView recyclerView;
    int amntOfBlocks;
    String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //import all data from Project
        Project project = getIntent().getParcelableExtra("Project");
        int projectID = getIntent().getIntExtra("projectID", -1);
        setProjectID(projectID);
        Log.d("ProjectActivity", "ProjectID: " + String.valueOf(projectID));
        requestProject();
        recyclerView = findViewById(R.id.ProjectView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ProjectAdapter projectAdapter = new ProjectAdapter(context, project,amntOfBlocks);
        recyclerView.setAdapter(projectAdapter);
        title = findViewById(R.id.ProjectTitleView);
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
                            recyclerView.getAdapter().notifyDataSetChanged();
                            title.setText(projectName);
                            // amntOfBlocks = project.getBlocks();
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

    private void requestSave(String Lyrics, String Types,int projectID){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                saveURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if( response.equals("[]")){

                        }
                        else {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("newblocktext", Lyrics);
                params.put("newblocktypes", Types);
                params.put("projectid", String.valueOf(projectID));
                return params;
            }
        };

        requestQueue.add(submitRequest);
    }

    public void onBtnSaveClicked(View Caller){

        // get Text From TextBlock
        TextInputEditText Lyrics = findViewById(R.id.LyricsTxt);
        String Text = String.valueOf(Lyrics.getText());
        String modifiedText = Text.replace(" ","_")+";";

        // get text from spinner

        Spinner type = findViewById(R.id.SectionsSp);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String Type = type.getItemAtPosition(position).toString();
                section = Type+";";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                section = "intro;";
            }
        });

        requestSave(modifiedText,section,projectID);
    }
}