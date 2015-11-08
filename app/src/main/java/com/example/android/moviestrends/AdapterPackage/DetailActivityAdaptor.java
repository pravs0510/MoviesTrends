package com.example.android.moviestrends.AdapterPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.moviestrends.R;

import java.util.List;

/**
 * Created by praveena on 9/13/2015.
 */
public class DetailActivityAdaptor extends ArrayAdapter {
    private Context mContext;
    private List<ReviewsObj> mReviews;
    private final String LOG_TAG = DetailActivityAdaptor.class.getSimpleName();
    private int mResource = 0;
    private String baseUrl = null;
    private LayoutInflater layoutInflater;

    public DetailActivityAdaptor(Context context,int resource ,List<ReviewsObj> moviesReviews) {
        super(context, resource, moviesReviews );
        mContext = context;
        mReviews =moviesReviews;
        mResource = resource;
        layoutInflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }
    public ReviewsObj getItem(int position) {
        return mReviews.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = layoutInflater.inflate(R.layout.review_grid, parent, false);
        }
        ReviewsObj reviewDetails = new ReviewsObj();
        reviewDetails =  getItem(position);
        TextView Review_author = (TextView)vi.findViewById(R.id.Author_text);
        Review_author.setText(reviewDetails.author);
        TextView Review_content = (TextView)vi.findViewById(R.id.Content);
        Review_content.setText(reviewDetails.content);

        return vi;
        // return super.getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void setmMovieData(List<ReviewsObj> movieReviews) {
//        mGridData.clear();
//        mGridData.addAll(gridData);
        mReviews = movieReviews;
        notifyDataSetChanged();
    }
}
