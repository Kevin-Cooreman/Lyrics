package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectActivity extends AppCompatActivity {

    TextView title, description;
    String projectTitle, projectDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        title = findViewById(R.id.ProjectTitleView);
        description = findViewById(R.id.ProjectDescriptionView);

        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("projectName") && getIntent().hasExtra("description")){
            projectTitle = getIntent().getStringExtra("projectName");
            projectDescription = getIntent().getStringExtra("description");

        }
        else{
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        title.setText(projectTitle);
        description.setText(projectDescription);
    }
}