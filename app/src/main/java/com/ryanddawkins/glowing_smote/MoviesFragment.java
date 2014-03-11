package com.ryanddawkins.glowing_smote;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * MovieFragment class to deal with all the movie files
 *
 * @author Ryan Dawkins
 * @package com.ryanddawkins.glowing_smote
 * @version 0.1
 * @extends Fragment
 */
public class MoviesFragment extends ListFragment
{

    Command command;
    Settings settings;
    String filePath;

    public MoviesFragment(){
        this("/media/ryan/Passport/Videos/Movies/");
    }
    public MoviesFragment(String filePath){
        this.filePath = filePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.settings = new Settings(getActivity());

        new GetMoviesTask().execute(this.filePath);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Movie movie = (Movie) getListAdapter().getItem(position);
        if(movie.isDirectory())
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new MoviesFragment(movie.getFilePath()))
                    .addToBackStack(null)
                    .commit();
        }
        else
        {
            RemoteFragment remoteFragment = new RemoteFragment(movie.getName());
            remoteFragment.setFilePath(movie.getFilePath());
            getFragmentManager().beginTransaction()
                .replace(R.id.container, remoteFragment)
                .addToBackStack(null)
                .commit();
        }

    }

    private class GetMoviesTask extends AsyncTask<String, Void, Void> {

        ArrayList<Movie> movies;

        protected Void doInBackground(String... params) {
            command = Command.getConnection(settings.getIpAddress(), settings.getPortNumber(), getActivity());
            if(command!=null){
                this.movies = command.getMovies(params[0]);
            } else {
                this.movies = null;
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            if(this.movies != null) {
                setListAdapter(new MovieListAdapter(getActivity(), this.movies));
            } else {
                getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit();
            }
        }
    }

}
