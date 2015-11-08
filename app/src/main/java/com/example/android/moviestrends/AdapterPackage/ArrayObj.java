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
    public String id;
    public String adult;
    public String backdrop_path;
    public String overview;
    public String original_language;
    public String original_title;
    public String release_date;
    public String status;
    public String runtime;
    public String poster_path;
     public String vote_average;
    public String vote_count;
    public String trailer_id;
    public String genre_id;

/*
  the constructor1 which is called when the parcel has to be decoded.
  @Param in is type parcel which is then copied to individual parameter
   */
        private ArrayObj(Parcel in) {
            id = in.readString();
            adult = in.readString();
            backdrop_path = in.readString();
            overview = in.readString();
            original_language = in.readString();
            original_title = in.readString();
            release_date = in.readString();
            runtime = in.readString();
            status=in.readString();
            poster_path = in.readString();
            vote_average = in.readString();
            vote_count = in.readString();
            trailer_id = in.readString();
            genre_id=  in.readString();

        }
    /*
     The constructor2 is a default constructor which initializes all the variables.
   */
    public ArrayObj() {
        this.id = "1";
        this.adult = " ";
        this.backdrop_path = " ";
        this.overview = " ";
        this.original_language = " ";
        this.original_title = " ";
        this.release_date = " ";
        this.runtime = " ";
        this.status = " ";
        this.poster_path = " ";
        this.vote_average = "0.0";
        this.vote_count = "0.0";
        this.trailer_id = " ";
        this.genre_id = " ";

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(adult);
        parcel.writeString(backdrop_path);
        parcel.writeString(overview);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(runtime);
        parcel.writeString(status);
        parcel.writeString(poster_path);
        parcel.writeString(vote_average);
        parcel.writeString(vote_count);
        parcel.writeString(trailer_id);
        parcel.writeString(genre_id);

    }

    public final static Parcelable.Creator<ArrayObj> CREATOR = new Parcelable.Creator<ArrayObj>() {
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