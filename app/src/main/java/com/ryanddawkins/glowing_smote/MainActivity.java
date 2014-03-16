package com.ryanddawkins.glowing_smote;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity to start the application :D
 *
 * @author Ryan Dawkins
 * @package com.ryanddawkins.glowing_smote
 * @since 0.1
 * @extends Activity
 */
public class MainActivity extends Activity {

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Settings settings = new Settings(this);

        if (savedInstanceState == null) {
            if(settings.isFirstTime())
            {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new SettingsFragment())
                        .commit();
            }
            else
            {
                getFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesFragment())
                    .commit();
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {

        this.settings = new Settings(this);
        NowPlayingTask nowPlayingTask = new NowPlayingTask().setSettings(this.settings);
        nowPlayingTask.execute();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public MainActivity getThis(){
        return this;
    }

    private class NowPlayingTask extends AsyncTask<Void, Void, Void> {

        private Settings settings;
        private String response;

        @Override
        protected Void doInBackground(Void... voids) {
            Command command = Command.getConnection(this.settings.getIpAddress(), this.settings.getPortNumber(), getThis());

            command.simpleCommand(Command.NOW_PLAYING);
            this.response = command.getJSON();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("glowing-smote", this.response);
            NowPlaying nowPlaying = NowPlaying.forge(this.response);
            if(nowPlaying != null && nowPlaying.isPlaying())
            {
                RemoteFragment remoteFragment = new RemoteFragment(nowPlaying.getMovie().getName());
                remoteFragment.setLoadedToVlc(nowPlaying.isPlaying());
                remoteFragment.setIsPlaying(nowPlaying.isPaused());
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, remoteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        public NowPlayingTask setSettings(Settings settings){
            this.settings = settings;
            return this;
        }

        public String getResponse() {
            return this.response;
        }

    }

}
