
package com.example.android.moviestrends.AdapterPackage;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by praveena on 9/19/2015.
 */

public class ReviewsObj implements Parcelable {

    public String author;
    public String content;



/*
      the constructor1 which is called when the parcel has to be decoded.
      @Param in is type parcel which is then copied to individual parameter
       */

    private ReviewsObj(Parcel in) {
        author = in.readString();
        content = in.readString();

    }

/*
     The constructor2 is a default constructor which initializes all the variables.
   */

    public ReviewsObj(){
        this.author="No Reviews";
        this.content =" ";

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);

    }

    public static final Parcelable.Creator<ReviewsObj> CREATOR = new Parcelable.Creator<ReviewsObj>() {
        @Override
        public ReviewsObj createFromParcel(Parcel parcel) {
            return new ReviewsObj(parcel);
        }

        @Override
        public ReviewsObj[] newArray(int i) {
            return new ReviewsObj[i];
        }

    };
}


