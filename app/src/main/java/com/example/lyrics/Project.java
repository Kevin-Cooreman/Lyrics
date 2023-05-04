package com.example.lyrics;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Project implements Parcelable {

    private String title;
    private String description;
    private ArrayList<String> blockListTypes;
    private ArrayList<String> blockListLyrics;

    private int AmntOfBlocks;

    //constructor
    public Project(String title, String description){
        this.title = title;
        this.description = description;
        AmntOfBlocks = 0;
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

    public void setAmntOfBlocks(int x){
        AmntOfBlocks = x;
    }

    public void IncrementAmntOfBlocks(){
        AmntOfBlocks++;
    }

    public int getAmntOfBlocks(){
        return AmntOfBlocks;
    }



    @NonNull
    @Override
    public String toString() {
        return "title: " + title + ", description: " + description;
    }

    //all things necessary for Parcelable
    protected Project(Parcel in) {
        title = in.readString();
        description = in.readString();
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
