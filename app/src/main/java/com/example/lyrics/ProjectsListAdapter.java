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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.MyViewHolder> {

    ArrayList<String> titles;
    ArrayList<String> descriptions;
    ArrayList<Integer> projectIDs;
    Context context;

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
                //intent.putExtra("projectID", id);

                Log.d("ProjectListAdapter", "The id of the clicked project is: " +id);

                Project project = new Project(id, view.getContext());
                intent.putExtra("Project", project);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //viewHolder for projectList

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
