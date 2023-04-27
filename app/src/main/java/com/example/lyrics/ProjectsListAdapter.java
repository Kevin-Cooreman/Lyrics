package com.example.lyrics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.MyViewHolder> {

    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();

    Context context;

    public ProjectsListAdapter(Context ct, ArrayList<String> titles, ArrayList<String> descriptions){
        context = ct;
        title = titles;
        description = descriptions;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.project_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(title.get(position));
        holder.myText2.setText(description.get(position));

        holder.ProjectListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProjectActivity.class);
                intent.putExtra("title",title.get(position));
                intent.putExtra("description",description.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

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
