package com.example.lyrics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.MyViewHolder> {

    ArrayList<String> titles;
    ArrayList<String> descriptions;
    ArrayList<Integer> projectIDs;
    Context context;
    private String requestWithIdUrl = "https://studev.groept.be/api/a22pt108/selectProjectWithID/";
    Project project;

    public ProjectsListAdapter(Context context,ArrayList<String> Titles, ArrayList<String> Descriptions, ArrayList<Integer>projectIDs){
        this.titles = Titles;
        this.descriptions = Descriptions;
        this.context = context;
        this.projectIDs = projectIDs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // holder for project list
        String title = titles.get(position);
        String description = descriptions.get(position);

        int id = projectIDs.get(position);

        holder.myText1.setText(title);
        holder.myText2.setText(description);

        holder.ProjectListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProjectActivity.class);
                //intent.putExtra("projectName",title);
                //intent.putExtra("description",description);
                intent.putExtra("projectID", id);
                //requestProjectWithId(id);

                //Project project = new Project(id, view.getContext());
                //Log.d("Project toString: ", project.toString());
                //intent.putExtra("Project", project);
                context.startActivity(intent);
            }
        });

    }

    private void setProject(Project project){
        this.project = project;
    }

    private void requestProjectWithId(int id) {
        String MODIFIED_LOGIN_URL = requestWithIdUrl + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_LOGIN_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("json response", String.valueOf(response));
                        if( response.length() != 0){
                            for (int i=0;i<response.length();i++) {
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
                                    Project project = new Project(projectID, projectName, description, ownerID, blockText, blockTypes);
                                    Log.d("dit is de project instantie in request: ", project.toString());
                                    setProject(project);


                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        }
                        else{
                            Log.d("Project","PROJECT NOT FOUND!!!");
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

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        ConstraintLayout ProjectListLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.lblTitle);
            myText2 = itemView.findViewById(R.id.lblDescription);
            ProjectListLayout = itemView.findViewById(R.id.ProjectListView);
        }
    }
}
