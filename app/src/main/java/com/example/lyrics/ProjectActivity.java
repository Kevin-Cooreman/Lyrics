package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectActivity extends AppCompatActivity {

    private TextView title;
    private int projectID;
    private String selectProjectURL = "https://studev.groept.be/api/a22pt108/selectProjectWithID/";
    private Context context = this;
    private Project project;
    private String saveURL = "https://studev.groept.be/api/a22pt108/UpdateBlocks";
    private ProjectAdapter projectAdapter;


    public void setProject(Project project) {
        this.project = project;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    RecyclerView recyclerView;
    ArrayList<String> sentences ;
    ArrayList<String> blockTypesArray;

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

                            setProject(project);
                            Log.d("ProjectActivity", "In request: "+ project.toString());
                            projectAdapter = new ProjectAdapter(context, project, project.getSentences() , project.getBlockTypesSplit());
                            recyclerView.setAdapter(projectAdapter);
                            title = findViewById(R.id.ProjectTitleView);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            title.setText(projectName);
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




    private void requestSave(String Lyrics, String Types, int projectID){
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
        ArrayList<String> lAS = getCurrent();
        String Lyrics = lAS.get(0);
        String sections = lAS.get(1);
        requestSave(Lyrics,sections,projectID);

    }

    public ArrayList<String> getCurrent(){

        StringBuilder lyricsBuilder = new StringBuilder();
        StringBuilder sectionBuilder = new StringBuilder();

        for (int i = 0; i < projectAdapter.getItemCount(); i++) {
            View itemView = recyclerView.getLayoutManager().findViewByPosition(i);
            if (itemView != null) {

                TextInputEditText lyricsEditText = itemView.findViewById(R.id.LyricsTxt); // Replace R.id.LyricsTxt with the ID of your EditText in each row
                Spinner sectionsSpinner = itemView.findViewById(R.id.SectionsSp); // Replace R.id.SectionsSp with the ID of your Spinner in each row

                String lyricsText = String.valueOf(lyricsEditText.getText());
                String modifiedText = lyricsText.replace(" ", "_") + ";";
                String section = sectionsSpinner.getSelectedItem().toString() + ";";

                lyricsBuilder.append(modifiedText);
                sectionBuilder.append(section);
            }
        }
        String Lyrics = lyricsBuilder.toString();
        String sections = sectionBuilder.toString();
        ArrayList<String> lyricsAndSections = new ArrayList<>();
        lyricsAndSections.add(Lyrics);
        lyricsAndSections.add(sections);
        Log.d("ProjectActivity", Lyrics+ ", " + sections);
        return lyricsAndSections;
    }

    public void onBtnPlusClicked(View Caller){
        ArrayList<String> lAS = getCurrent();
        String Lyrics = lAS.get(0);
        String sections = lAS.get(1);
        String l1 = Lyrics.concat("_;");
        String s2 = sections.concat("_;");
        requestSave(l1, s2, projectID);
        requestProject();
        //recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.requestLayout();
        recyclerView.invalidate();
        //smooth scroll werkt niet
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

}