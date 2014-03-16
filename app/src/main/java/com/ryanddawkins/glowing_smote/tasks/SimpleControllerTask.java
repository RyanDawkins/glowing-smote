package com.ryanddawkins.glowing_smote.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.ryanddawkins.glowing_smote.Command;
import com.ryanddawkins.glowing_smote.R;
import com.ryanddawkins.glowing_smote.Settings;

/**
 * Created by ryan on 3/15/14.
 */
public class SimpleControllerTask extends AsyncTask<Integer, Void, Void> {

    private Settings settings;
    private Activity activity;

    protected Void doInBackground(Integer... params)
    {
        int id = params[0];
        Command command = Command.getConnection(this.settings.getIpAddress(), this.settings.getPortNumber(), this.activity);
        switch(id)
        {
            case R.id.next_chapter_button:
                Log.d("glowing-smote", "Next chapter");
                command.simpleCommand(Command.NEXT_CHAPTER);
                break;
            case R.id.previous_chapter_button:
                Log.d("glowing-smote","previous chapter");
                command.simpleCommand(Command.PREVIOUS_CHAPTER);
                break;
            case R.id.fast_forward_button:
                Log.d("glowing-smote","fast forward");
                command.simpleCommand(Command.SKIP_FORWARD);
                break;
            case R.id.rewind_button:
                Log.d("glowing-smote","rewind");
                command.simpleCommand(Command.SKIP_BACKWARD);
                break;
            default:
                Log.d("glowing-smote", ""+id);
                break;
        }
        return null;
    }

    public Activity getActivity() {
        return activity;
    }

    public SimpleControllerTask setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public Settings getSettings() {
        return settings;
    }

    public SimpleControllerTask setSettings(Settings settings) {
        this.settings = settings;
        return this;
    }

}
