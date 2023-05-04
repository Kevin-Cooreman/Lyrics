package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateNewProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_project);
    }

    public void onBtnLoginClicked(View Caller) {

        EditText titleEditText = findViewById(R.id.newProjectNameTextInput);
        String title = titleEditText.getText().toString();
        EditText descEditText = findViewById(R.id.newProjectDescriptionInput);
        String description = descEditText.getText().toString();

        Project newProject = new Project(title, description);

        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra("Project", newProject);
        startActivity(intent);


    }
}

