package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ProjectsListActivity extends AppCompatActivity {
    String ProjectTitles[], ProjectDescriptions[];
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_list);

        ProjectTitles = getResources().getStringArray(R.array.projectTitles);
        ProjectDescriptions = getResources().getStringArray(R.array.projectDescriptions);
        recyclerView = findViewById(R.id.ProjectListView);

        ProjectsListAdapter projectsListAdapter = new ProjectsListAdapter(this, ProjectTitles, ProjectDescriptions);
        recyclerView.setAdapter(projectsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}