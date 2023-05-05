package com.example.lyrics;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

public class Project implements Parcelable {

    private String title;
    private String description;
    private int projectID;
    private int ownerID;
    private int blockText;
    private int blockTypes;
    private ArrayList<String> blockListTypes;
    private ArrayList<String> blockListLyrics;
    private String requestWithIdUrl = "https://studev.groept.be/api/a22pt108/selectProjectWithID/";

    //constructors
    public Project(String title, String description){
        this.title = title;
        this.description = description;
    }

    public Project(int id, Context context){
        //open an existing project in the database using only the projectID
        requestProjectWithId(id,context);
    }

    public void addBlock(String type){
        blockListTypes.add("type");
    }

    //getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getBlockListTypes() {
        return blockListTypes;
    }

    public ArrayList<String> getBlockListLyrics() {
        return blockListLyrics;
    }

    public int getProjectID() {
        return projectID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getBlockText() {
        return blockText;
    }

    public int getBlockTypes() {
        return blockTypes;
    }

    public String getRequestWithIdUrl() {
        return requestWithIdUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setBlockText(int blockText) {
        this.blockText = blockText;
    }

    public void setBlockTypes(int blockTypes) {
        this.blockTypes = blockTypes;
    }

    public void setBlockListTypes(ArrayList<String> blockListTypes) {
        this.blockListTypes = blockListTypes;
    }

    public void setBlockListLyrics(ArrayList<String> blockListLyrics) {
        this.blockListLyrics = blockListLyrics;
    }

    public void setRequestWithIdUrl(String requestWithIdUrl) {
        this.requestWithIdUrl = requestWithIdUrl;
    }

    private void requestProjectWithId(int id, Context context) {
        String MODIFIED_LOGIN_URL = requestWithIdUrl + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                int projectID;
                                String projectName;
                                String description;
                                int ownerID;
                                //String blockText;
                                //String blockTypes;

                                try {
                                    projectID = jsonobject.getInt("projectID");
                                    projectName = jsonobject.getString("projectName");
                                    description = jsonobject.getString("description");
                                    ownerID = jsonobject.getInt("ownerID");
                                    //blockText = jsonobject.getString("blockText");
                                    //blockTypes = jsonobject.getString("blockTypes");

                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                setProjectID(projectID);
                                setTitle(projectName);
                                setDescription(description);
                                setOwnerID(ownerID);
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

    @NonNull
    @Override
    public String toString() {
        return "title: " + title + ", description: " + description;
    }

    //all things necessary for Parcelable
    protected Project(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
    }

}
