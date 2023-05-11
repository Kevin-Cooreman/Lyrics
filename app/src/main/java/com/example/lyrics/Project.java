package com.example.lyrics;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Project implements Parcelable {

    private String title;
    private String description;
    private ArrayList<String> blockListTypes;
    private ArrayList<String> blockListLyrics;
    private int projectID;
    private int Blocks;
    private int ownerID;
    private ArrayList<String> sentences;
    private ArrayList<String> blockTypesSplit;


    //constructor
    public Project(int ProjectID, String title, String description,int ownerID, String blockText, String blockTypes){
        this.title = title;
        this.description = description;
        this.projectID = ProjectID;
        this.ownerID = ownerID;
        sentences= new ArrayList<>();
        blockTypesSplit = new ArrayList<>();
        //seperate all textBlocks
        String[] text = blockText.split(";");
        Collections.addAll(sentences, text);
        String[] types = blockTypes.split(";");
        Collections.addAll(blockTypesSplit, types);


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

    public int getBlocks() {
        return Blocks;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public ArrayList<String> getSentences() {
        if( sentences.size() == 0){
            ArrayList<String> loading = new ArrayList<>();
            loading.add("loading...");
            return loading;
        }
        else {
            return sentences;
        }
    }

    public ArrayList<String> getBlockTypesSplit() {
        if( sentences.size() == 0){
            ArrayList<String> loading = new ArrayList<>();
            loading.add("loading...");
            return loading;
        }
        else {
            return blockTypesSplit;
        }
    }

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
        return "projectID: " + projectID+ "projectName: " + title + ", description: " + description + ", ownerID: "+ ownerID + ", blockText : " + sentences + ", BlockTypes: " + blockTypesSplit;
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
