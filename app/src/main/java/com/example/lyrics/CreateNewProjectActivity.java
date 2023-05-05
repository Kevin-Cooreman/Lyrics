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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateNewProjectActivity extends AppCompatActivity {

    String addProjectUrl = "https://studev.groept.be/api/a22pt108/addNewProject/";
    int userID;
    Project project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_project);
        int UserID = getIntent().getIntExtra("UserID", -1);
        Log.d("CreateNewProjectActivity", "UserID: " + UserID);
        setUserID(UserID);

    }

    /*
    //TEMP RECORD BUTTON
    //
    boolean recording = false;
    MediaRecorder mediaRecorder = new MediaRecorder();

    public void onBtnRecordClicked(View Caller){
        if (recording){
            //stop recording
            stopRecording(mediaRecorder);
            recording = false;
        }
        else{
            //start recording
            startRecording(mediaRecorder);
            recording = true;
        }
    }

    public void startRecording(MediaRecorder mediaRecorder){


        // Set audio source and output format
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // Set output file name and path
        String fileName = "record_" + System.currentTimeMillis() + ".3gp";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
        mediaRecorder.setOutputFile(filePath);

        // Set audio encoder and quality
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setAudioEncodingBitRate(16 * 44100);
        mediaRecorder.setAudioSamplingRate(44100);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
        Button recordButton = findViewById(R.id.recordButton);
        recordButton.setText("Stop Recording");
    }
    public void stopRecording(MediaRecorder mediaRecorder){
        mediaRecorder.stop();
        mediaRecorder.release();
        Button recordButton = findViewById(R.id.recordButton);
        recordButton.setText("Record Audio");
    }
    */
    //
    //
    //

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
        Project newProject = new Project(title, description);
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
                            successfulAddedProject();
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

    public void successfulAddedProject(){
        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra("Project", project);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}

