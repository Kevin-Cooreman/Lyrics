package com.example.lyrics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    Context context;
    Project project;
    ArrayList<String> textBlocks;
    ArrayList<String> types;

    public ProjectAdapter(Context context, Project project, ArrayList<String> Lyrics, ArrayList<String> sections){
        this.context = context;
        this.project = project;
        this.textBlocks = Lyrics;
        this.types = sections;


    }
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String lyric = textBlocks.get(position).replace("_"," ").replace(";","");
        String Type = types.get(position).replace(";","");


        holder.myText.setText(lyric);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.Sections, android.R.layout.simple_spinner_item);
        holder.Types.setAdapter(adapter);

        int index = adapter.getPosition(Type);
        holder.Types.setSelection(index);
    }

    @Override
    public int getItemCount() {
        if (textBlocks.size() == 0){
            return 1;
        }
        else{
            return textBlocks.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ProjectLayout;
        TextInputEditText myText;
        Spinner Types;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ProjectLayout = itemView.findViewById(R.id.ProjectView);
            myText = itemView.findViewById(R.id.LyricsTxt);
            Types = itemView.findViewById(R.id.SectionsSp);
        }
    }
}
