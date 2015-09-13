/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.example.android.moviestrends;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by praveena on 8/23/2015.
 */
/*
This Class implements Parcelable for detail Activity screen.
 */
public class DetailParcelable implements Parcelable {

    String originalTitle;
    String posterPath;
    String overview;
    String userRatings;
    String releaseDate;

    /*
    Constructor 1
    @Param originalTitle, posterPath,overview,userRatings,releaseDate
     */

    public DetailParcelable(String originalTitle, String posterPath, String overview, String userRatings, String releaseDate)
    {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRatings=userRatings;
        this.releaseDate=releaseDate;
    }

    /*

     */
    private DetailParcelable(Parcel in){
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        userRatings = in.readString();
        releaseDate = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(userRatings);
        parcel.writeString(releaseDate);
    }

    public final Parcelable.Creator<DetailParcelable> CREATOR = new Parcelable.Creator<DetailParcelable>() {
        @Override
        public DetailParcelable createFromParcel(Parcel parcel) {
            return new DetailParcelable(parcel);
        }

        @Override
        public DetailParcelable[] newArray(int i) {
            return new DetailParcelable[i];
        }

    };
}
