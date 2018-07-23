package masum.sqlitedatabaseprojectbitm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by spmas on 14-May-18.
 */

public class MovieAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieModel> movieModelArrayList = new ArrayList<>();

    public MovieAdapter(Context context, ArrayList<MovieModel> movieModelArrayList) {
        this.context = context;
        this.movieModelArrayList = movieModelArrayList;
    }

    @Override
    public int getCount() {
        return movieModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.movie_row_layout, viewGroup, false);
        MovieModel movieModel = movieModelArrayList.get(position);
        TextView movieName = convertView.findViewById(R.id.movieNameTV);
        movieName.setText(movieModel.getMovieName());
        TextView movieYear = convertView.findViewById(R.id.movieYearTV);
        movieYear.setText(movieModel.getMovieYear());

        return convertView;
    }
}


