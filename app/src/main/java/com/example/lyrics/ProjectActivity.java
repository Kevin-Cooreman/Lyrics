package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectActivity extends AppCompatActivity {

    TextView title;
    String projectTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //import all data from Project
        Project project = (Project) getIntent().getParcelableExtra("Project");

        title =  (TextView) findViewById(R.id.ProjectTitleView);
        title.setText(project.toString());
        Log.d("dit is de project instantie: ", project.toString());
        title.setVisibility(View.VISIBLE);


        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("projectName") && getIntent().hasExtra("description")){
            projectTitle = getIntent().getStringExtra("projectName");

        }
        else{
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        title.setText(projectTitle);
    }
}