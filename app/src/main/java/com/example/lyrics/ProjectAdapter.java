package com.example.lyrics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectAdapter extends RecyclerView.Adapter {
    Context context;
    int AmountOfRows;
    Project project;

    public ProjectAdapter(Context context, Project project, String[] sentences, String[] types){
        this.context = context;
        this.project = project;

    }
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_row, parent, false);
        return new ProjectAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        if (AmountOfRows == 0){
            return 1;
        }
        else{
            return AmountOfRows;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ProjectListLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ProjectListLayout = itemView.findViewById(R.id.ProjectView);
        }
    }
}
