package com.ryanddawkins.glowing_smote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by ryan on 2/13/14.
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

    private Context context;
    private ArrayList<Movie> movies;

    public MovieListAdapter(Context context, ArrayList<Movie> movies)
    {
        super(context, R.layout.movie_row, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.movie_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        if(this.movies.get(position).isDirectory())
        {
            textView.setText(this.movies.get(position).getName()+"/");
        } else
        {
            textView.setText(this.movies.get(position).getName());
        }
        return rowView;
    }

}
