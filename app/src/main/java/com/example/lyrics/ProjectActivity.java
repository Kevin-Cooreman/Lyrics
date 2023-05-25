package com.example.lyrics;

import static java.util.Base64.getEncoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Base64;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectActivity extends AppCompatActivity {

    private static String uploadAudio_URL = "https://studev.groept.be/api/a22pt108/saveAudio/";
    private static int MICROPHONE_PERMISSION_CODE = 200;
    private static final int SAMPLE_RATE = 44100; // Sample rate (Hz)
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO; // Mono channel
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT; // 16-bit PCM encoding
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioRecord audioRecord;
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
        Project project = getIntent().getParcelableExtra("Project");
        int projectID = getIntent().getIntExtra("projectID", -1);
        setProjectID(projectID);
        Log.d("ProjectActivity", "ProjectID: " + String.valueOf(projectID));
        requestProject();
        recyclerView = findViewById(R.id.ProjectView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (checkMic()) {
            getMicPermission();
        }


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

                            try {
                                projectID = jsonobject.getInt("projectID");
                                projectName = jsonobject.getString("projectName");
                                description = jsonobject.getString("description");
                                ownerID = jsonobject.getInt("ownerID");
                                blockText = jsonobject.getString("blockText");
                                blockTypes = jsonobject.getString("blockTypes");

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

    private static void uploadAudio(byte[] audioData, int projectID) throws IOException {
        URL url = new URL(uploadAudio_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        // Set additional request parameters
        String query = "projectid=" + URLEncoder.encode(String.valueOf(projectID), "UTF-8");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(audioData);
            outputStream.write("&".getBytes());
            outputStream.write(query.getBytes());
        }
        Log.d("ProjectActivity", "In request: " +audioData.toString());

        int responseCode = connection.getResponseCode();
        // Handle the response code or any other response as needed

        connection.disconnect();
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkMic();
            getMicPermission();
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        audioRecord.startRecording();
        isRecording = true;

        Thread recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];

                try {
                    while (isRecording) {
                        int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
                        if (bytesRead != AudioRecord.ERROR_INVALID_OPERATION) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                } finally {
                    try {
                        outputStream.close();
                        byte[] audioData = outputStream.toByteArray();
                        setAudio(audioData);
                        Log.d("ProjectActivity", "byte[]: " + Arrays.toString(audioData));
                        String audioBase64 = getEncoder().encodeToString(audioData);
                        uploadAudio(audioData,projectID);
                        Log.d("ProjectActivity", "Audio: " + Arrays.toString(audioData));
                        Log.d("ProjectActivity", "Audio base64: " + audioBase64);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        recordingThread.start();
    }
    public void onBtnStopPressed(View Caller){
        isRecording = false;

        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }

    }
    public void onBtnPlayPressed(View Caller){

        try {
            // Create a temporary audio file
            File tempAudioFile = File.createTempFile("temp_audio", ".wav", getCacheDir());

            // Write the byte array to the temporary audio file
            FileOutputStream fileOutputStream = new FileOutputStream(tempAudioFile);
            fileOutputStream.write(audio);
            fileOutputStream.close();
            mediaPlayer = new MediaPlayer();
            // Reset the MediaPlayer
            mediaPlayer.reset();

            // Set the audio file as the data source for the MediaPlayer
            mediaPlayer.setDataSource(tempAudioFile.getAbsolutePath());
            Log.d("projectActivity", "sound file path: " + tempAudioFile.getAbsolutePath());

            // Set the audio attributes for playback
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);

            // Prepare the MediaPlayer asynchronously
            mediaPlayer.prepareAsync();

            // Set the onPreparedListener to start playback when prepared
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    private boolean checkMic(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }

    private void getMicPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    MICROPHONE_PERMISSION_CODE);
        }
    }
}