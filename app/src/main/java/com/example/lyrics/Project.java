package com.example.lyrics;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.security.acl.Owner;
import java.util.ArrayList;

public class Project implements Parcelable {

    private String title;
    private String description;
    private ArrayList<String> blockListTypes;
    private ArrayList<String> blockListLyrics;
    private int projectID;
    private int Blocks;
    private int ownerID;
    private String BlockText;
    private String BlockTypes;


    //constructor
    public Project(int ProjectID, String title, String description,int ownerID, String BlockText, String BlockTypes){
        this.title = title;
        this.description = description;
        this.projectID = ProjectID;
        this.ownerID = ownerID;
        this.BlockText = BlockText;
        this.BlockTypes = BlockTypes;
    }

    public void addBlock(String type){
        blockListTypes.add("type");
    }

    //getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getBlockListTypes() {
        return blockListTypes;
    }

    public ArrayList<String> getBlockListLyrics() {
        return blockListLyrics;
    }
    public int getProjectID(){ return projectID;}

    /*
    public int getBlocks(){
        if (blockListTypes == null){
            return 1;
        }
        else {
            Blocks = blockListTypes.size();
            return Blocks;
        }
    }
    //setters
    public void setBlocks(int x){ Blocks = x;}
    */


    @NonNull
    @Override
    public String toString() {
        return "projectID: " + projectID+ "projectName: " + title + ", description: " + description + ", ownerID: "+ ownerID + ", blockText : " + BlockText + ", BlockTypes: " + BlockTypes ;
    }

    //all things necessary for Parcelable
    protected Project(Parcel in) {
        title = in.readString();
        description = in.readString();
        projectID = in.readInt();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
    }

}
