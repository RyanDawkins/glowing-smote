package com.ryanddawkins.glowing_smote;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ryanddawkins.glowing_smote.tasks.SimpleControllerTask;

/**
 * Created by ryan on 2/15/14.
 */
public class RemoteFragment extends Fragment
{

    private Command command;
    private Settings settings;
    private String movieTitle;
    private String filePath;
    private boolean isPlaying;
    private boolean loadedToVlc;
    private Button playPauseButton;


    public RemoteFragment(String movieTitle)
    {
        this.movieTitle = movieTitle;
        this.isPlaying = false;
        this.loadedToVlc = false;
    }

    public RemoteFragment setFilePath(String filePath)
    {
        this.filePath = filePath;
        return this;
    }

    public RemoteFragment setLoadedToVlc(boolean loadedToVlc)
    {
        this.loadedToVlc = loadedToVlc;
        return this;
    }

    public RemoteFragment setIsPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
        return this;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    public String getFilePath()
    {
        return this.filePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remote, container, false);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.settings = new Settings(getActivity());

        TextView movieTitleView = (TextView) getActivity().findViewById(R.id.movie_title);
        movieTitleView.setText(this.movieTitle);

        playPauseButton = (Button) getActivity().findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new PlayerButtonListener());

        Button nextChapter = (Button) getActivity().findViewById(R.id.next_chapter_button);
        nextChapter.setOnClickListener(new PlayerButtonListener());
        Button previousChapter = (Button) getActivity().findViewById(R.id.previous_chapter_button);
        previousChapter.setOnClickListener(new PlayerButtonListener());
        Button fastForward = (Button) getActivity().findViewById(R.id.fast_forward_button);
        fastForward.setOnClickListener(new PlayerButtonListener());
        Button rewind = (Button) getActivity().findViewById(R.id.rewind_button);
        rewind.setOnClickListener(new PlayerButtonListener());


        if(this.loadedToVlc)
        {
            if(this.isPlaying())
            {
                switchToPause();
            }
            else
            {
                switchToPlaying();
            }
        }
        else
        {
            this.isPlaying = false;
        }

    }

    private class PlayerButtonListener implements View.OnClickListener{
        public void onClick(View v)
        {
            playerButton(v);
        }
    }
    public void playerButton(View view)
    {
        int id = view.getId();
        switch(id)
        {
            case R.id.play_pause_button:
                if(!this.loadedToVlc)
                {
                    this.isPlaying = true;
                    this.loadedToVlc = true;
                    Toast.makeText(getActivity(), "Starting", Toast.LENGTH_SHORT).show();
                    this.playPauseButton.setText(getActivity().getString(R.string.movie_pause));
                    Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_action_pause);
                    this.playPauseButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    new PlayMoviesTask().execute(this.filePath);
                }
                else if(this.isPlaying)
                {
                    Toast.makeText(getActivity(), "Pausing", Toast.LENGTH_SHORT).show();
                    this.isPlaying = false;
                    new PauseMovieTask().execute();
                }
                else
                {
                    Toast.makeText(getActivity(), "Playing", Toast.LENGTH_SHORT).show();
                    this.isPlaying = true;
                    new PauseMovieTask().execute();
                }
                break;
            case R.id.next_chapter_button:
                Toast.makeText(getActivity(), "Next Chapter", Toast.LENGTH_SHORT).show();
                new SimpleControllerTask().setActivity(getActivity()).setSettings(this.settings).execute(id);
                break;
            case R.id.previous_chapter_button:
                Toast.makeText(getActivity(), "Previous Chapter", Toast.LENGTH_SHORT).show();
                new SimpleControllerTask().setActivity(getActivity()).setSettings(this.settings).execute(id);
                break;
            case R.id.fast_forward_button:
                Toast.makeText(getActivity(), "Fast forward", Toast.LENGTH_SHORT).show();
                new SimpleControllerTask().setActivity(getActivity()).setSettings(this.settings).execute(id);
                break;
            case R.id.rewind_button:
                Toast.makeText(getActivity(), "Rewind", Toast.LENGTH_SHORT).show();
                new SimpleControllerTask().setActivity(getActivity()).setSettings(this.settings).execute(id);
                break;
        }
    }

    private void switchToPlaying()
    {
        playPauseButton.setText(getActivity().getString(R.string.movie_play));
        Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_action_play);
        playPauseButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
    }
    private void switchToPause()
    {
        playPauseButton.setText(getActivity().getString(R.string.movie_pause));
        Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_action_pause);
        playPauseButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
    }

    private class PauseMovieTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params)
        {
            command = Command.getConnection(settings.getIpAddress(), settings.getPortNumber(), getActivity());
            command.simpleCommand(Command.PAUSE_MEDIA);
            return null;
        }
        protected void onPostExecute(Void returned)
        {
            if(isPlaying)
            {
                switchToPause();
            }
            else
            {
                switchToPlaying();
            }
        }
    }

    private class PlayMoviesTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params)
        {
            command = Command.getConnection(settings.getIpAddress(), settings.getPortNumber(), getActivity());
            command.playMovie(params[0]);

            return null;
        }
    }

}
