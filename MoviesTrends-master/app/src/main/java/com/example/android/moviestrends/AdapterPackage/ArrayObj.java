/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.moviestrends.AdapterPackage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by praveena on 8/10/2015.
 * This Class implements parcelable which is used to store the data from the api and save it in SaveInstanceState for further usage.
 */

public class ArrayObj implements Parcelable{
    // public String thumb;
    public String title;
    public String adult;
    public String backdrop_path;

    public String overview;
    public String original_language;
    public String original_title;
    public String release_date;
    public String poster_path;
    public Double popularity;
    public String video;
    public Double vote_average;
    public Double vote_count;

/*
  the constructor1 which is called when the parcel has to be decoded.
  @Param in is type parcel which is then copied to individual parameter
   */
        private ArrayObj(Parcel in) {

            title = in.readString();
            adult = in.readString();
            backdrop_path = in.readString();
            overview = in.readString();
            original_language = in.readString();
            original_title = in.readString();
            release_date = in.readString();
            poster_path = in.readString();
            popularity = in.readDouble();
            video = in.readString();
            vote_average = in.readDouble();
            vote_count = in.readDouble();
        }

    /*
     The constructor2 is a default constructor which initializes all the variables.
   */
    public ArrayObj(){
        this.poster_path =" ";
        this.title=" ";
        this.adult=" ";
        this.backdrop_path=" ";
        this.overview=" ";
        this.original_title=" ";
        this.original_language= " ";
        this.release_date=" ";
        this.popularity= 0.0;
        this.video =" ";
        this.vote_average=0.0;
        this.vote_count=0.0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(adult);
        parcel.writeString(backdrop_path);
        parcel.writeString(overview);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(poster_path);
        parcel.writeDouble(popularity);
        parcel.writeString(video);
        parcel.writeDouble(vote_average);
        parcel.writeDouble(vote_count);
    }

    public final Parcelable.Creator<ArrayObj> CREATOR = new Parcelable.Creator<ArrayObj>() {
        @Override
        public ArrayObj createFromParcel(Parcel parcel) {
            return new ArrayObj(parcel);
        }

        @Override
        public ArrayObj[] newArray(int i) {
            return new ArrayObj[i];
        }

    };
}