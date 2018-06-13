package com.example.user.dreamtreeapp;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.widget.EditText;

import java.io.Serializable;

public class Post implements Serializable {

    private String title;
    private String content;
    private String date;

    public Post() {}

    public Post(String title, String content,String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    /*
    protected Post(Parcel in) {
        title = in.readString();
        content = in.readString();
        date = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}