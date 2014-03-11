package com.ryanddawkins.glowing_smote;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Settings class to interact with the prefences object for the user
 *
 * @author Ryan Dawkins
 * @package com.ryanddawkins.glowing_smote
 * @since 0.1
 */
public class Settings
{

    public static final int DEFAULT_PORT = 13928;

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;

    public Settings(Context context)
    {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.server_preferences), Context.MODE_PRIVATE);
        this.preferenceEditor = this.sharedPreferences.edit();
    }

    /**
     * Getter method to grab the ip address from shared preferences using the key in the strings.xml file
     *
     * @return String ipAddress
     */
    public String getIpAddress()
    {
        String ipAddress = sharedPreferences.getString(this.context.getString(R.string.pref_ip_address), "127.0.0.1");
        return ipAddress;
    }

    /**
     * Chainable method to set the ip address to the shared preferences
     *
     * @param ipAddress
     * @return Settings this
     */
    public Settings setIpAddress(String ipAddress)
    {
        this.preferenceEditor.putString(this.context.getString(R.string.pref_ip_address), ipAddress);
        this.preferenceEditor.commit();
        return this;
    }

    /**
     * Grabs the port number by the string stored in strings.xml
     *
     * @return int port
     */
    public int getPortNumber()
    {
        int port = sharedPreferences.getInt(this.context.getString(R.string.pref_port_number), DEFAULT_PORT);
        return port;
    }

    /**
     * Sets the port number in the user preferences
     *
     * @param port
     * @return Settings this
     */
    public Settings setPortNumber(int port)
    {
        this.preferenceEditor.putInt(this.context.getString(R.string.pref_port_number), port);
        this.preferenceEditor.commit();
        return this;
    }

    /**
     * Method to check if it is the users first time
     *
     * @return boolean isFirstTime
     */
    public boolean isFirstTime()
    {
        // If returns false we set it to false and return true
        if(!this.sharedPreferences.contains(this.context.getString(R.string.pref_is_first_time)))
        {
            this.preferenceEditor.putBoolean(this.context.getString(R.string.pref_is_first_time), false);
            this.preferenceEditor.commit();
            Log.d("glowing-smote", "Is first time..");
            return true;
        }
        Log.d("glowing-smote", "Isn't first time.");
        return false;
    }

}
