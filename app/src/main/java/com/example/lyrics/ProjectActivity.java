package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.media.MediaRecorder;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProjectActivity extends AppCompatActivity {

    private static String uploadAudio_URL = "https://studev.groept.be/api/a22pt108/saveAudio/";
    private static String selectAudio_URL = "https://studev.groept.be/api/a22pt108/selectAudio/";
    private static int MICROPHONE_PERMISSION_CODE = 200;
    private static final int SAMPLE_RATE = 44100; // Sample rate (Hz)
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO; // Mono channel
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT; // 16-bit PCM encoding
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioRecord audioRecord;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private byte[] audio;
    private MediaPlayer mediaPlayer;
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
    ArrayList<String> sentences;
    ArrayList<String> blockTypesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //import all data from Project
        int projectID = getIntent().getIntExtra("projectID", -1);
        setProjectID(projectID);
        Log.d("ProjectActivity", "ProjectID: " + String.valueOf(projectID));
        requestProject();

        recyclerView = findViewById(R.id.ProjectView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (checkForMic()) {
            getMicPermission();
        }
        requestAudio();
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

                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonobject = null;
                            try {
                                jsonobject = response.getJSONObject(i);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            int projectID;
                            String projectName;
                            String description;
                            int ownerID;
                            String blockText;
                            String blockTypes;
                            String audio;

                            try {
                                projectID = jsonobject.getInt("projectID");
                                projectName = jsonobject.getString("projectName");
                                description = jsonobject.getString("description");
                                ownerID = jsonobject.getInt("ownerID");
                                blockText = jsonobject.getString("blockText");
                                blockTypes = jsonobject.getString("blockTypes");
                                audio = jsonobject.getString("audio");

                                convertToMP4andWriteToDisk(audio);


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Project project = new Project(projectID, projectName, description, ownerID, blockText, blockTypes);

                            setProject(project);
                            Log.d("ProjectActivity", "In request: " + project.toString());
                            projectAdapter = new ProjectAdapter(context, project, project.getSentences(), project.getBlockTypesSplit());
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


    private void requestSave(String Lyrics, String Types, int projectID) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                saveURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {

                        } else {

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


    public void onBtnSaveClicked(View Caller) {
        ArrayList<String> lAS = getCurrent();
        String Lyrics = lAS.get(0);
        String sections = lAS.get(1);
        requestSave(Lyrics, sections, projectID);

    }

    public ArrayList<String> getCurrent() {

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
        Log.d("ProjectActivity", Lyrics + ", " + sections);
        return lyricsAndSections;
    }

    public void onBtnPlusClicked(View Caller) {
        ArrayList<String> lAS = getCurrent();
        String Lyrics = lAS.get(0);
        String sections = lAS.get(1);
        String l1 = Lyrics.concat("_;");
        String s1 = sections.concat("_;");
        project.setBlockListLyrics(l1);
        project.setBlockListTypes(s1);
        requestSave(l1, s1, projectID);
        recyclerView.requestLayout();
        recyclerView.invalidate();
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    public void onBtnShareClicked(View Caller) {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra("projectID", projectID);
        context.startActivity(intent);
    }


    public void onBtnRecordPressed(View Caller) {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Toast.makeText(this, "recording started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBtnStopPressed(View Caller) {
        if(isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(this, "recording stopped", Toast.LENGTH_SHORT).show();
            uploadRecordingToDB();
            isRecording = false;
        }

    }

    public void onBtnPlayPressed(View Caller) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "recording is playing", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkForMic() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMicPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath() throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getFilesDir();
        File file = new File(musicDirectory, "testrecording" + ".mp4");
        return file.getPath();
    }

    private void uploadRecordingToDB() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                uploadAudio_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ProjectActivity", "uploadRecordingToDB response: " + response.toString());
                        if (response.equals("[]")) {

                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ProjectActivity", "error kon uploadRecordingToDB api niet bereiken");
                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("audio", getAudio());
                params.put("projectid", String.valueOf(projectID));
                return params;
            }
        };

        requestQueue.add(submitRequest);
    }

    public String getAudio() {
        try {
            File file = new File(getRecordingFilePath());
            byte[] data = Files.readAllBytes(file.toPath());
            Log.d("ProjectActivity", "dataString audio: " + Arrays.toString(data));
            return Arrays.toString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ProjectActivity", "bytestring conversion failed");
        return "failed!!!";
    }


    public void convertToMP4andWriteToDisk(String dataString){
        dataString = dataString.substring(1,dataString.length() -1);
        String[] byteValues = dataString.split(", ");
        byte[] data = new byte[byteValues.length];
        for (int i = 0; i < byteValues.length; i++){
            data[i] = Byte.parseByte(byteValues[i]);
        }

        Log.d("ProjectActivity", "de data in convertToMP4andWriteToDisk: " + Arrays.toString(data));
        try {
            FileOutputStream fos = new FileOutputStream(getRecordingFilePath());
            fos.write(data);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void requestAudio() {
        String MODIFIED_URL = selectAudio_URL + projectID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                MODIFIED_URL,
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
                                try {
                                    String dataString = jsonobject.getString("audio");
                                    convertToMP4andWriteToDisk(dataString);
                                    Log.d("ProjectActivity", "Audio in the database found!");
                                    Log.d("ProjectActivity","Data from DB:" + dataString);

                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        else{
                            Log.d("ProjectActivity", "Select audio returns nothing (is there a recording yet?)");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(queueRequest);
    }

}