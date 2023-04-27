package com.example.lyrics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.lblTitle);
            myText2 = itemView.findViewById(R.id.lblDescription);

        }
    }
}
