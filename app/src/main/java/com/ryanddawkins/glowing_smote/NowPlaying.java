package com.ryanddawkins.glowing_smote;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Created by ryan on 3/15/14.
 */
public class NowPlaying {

    public static NowPlaying forge(String response)
    {

        JsonElement root;
        try
        {
            root = new JsonParser().parse(response);
        }
        catch(JsonParseException e){ return null; }

        if(!root.isJsonObject() || root.isJsonNull())
        {
            return null;
        }

        JsonObject rootObject = root.getAsJsonObject();
        NowPlaying nowPlaying;
        if(rootObject.has("movie") && rootObject.get("movie").isJsonPrimitive())
        {
            nowPlaying = new NowPlaying(rootObject.get("movie").getAsString());
        }
        else
        {
            nowPlaying = new NowPlaying("");
        }

        if(rootObject.has("isPlaying"))
        {
            nowPlaying.setIsPlaying(rootObject.get("isPlaying").getAsBoolean());
        }

        if(rootObject.has("isPaused"))
        {
            nowPlaying.setIsPaused(rootObject.get("isPaused").getAsBoolean());
        }

        return nowPlaying;
    }

    private Movie movie;
    private String movieName;
    private boolean isPlaying;
    private boolean isPaused;

    public NowPlaying(Movie movie)
    {
        this.movie = movie;
        this.isPlaying = false;
        this.isPaused = true;
        this.movieName = movie.getName();
    }

    public NowPlaying(String movieName)
    {
        this.movieName = movieName;
        this.movie = new Movie(movieName);
        this.movie.setName(movieName);
        this.isPlaying = false;
        this.isPaused = true;
    }

    public NowPlaying setIsPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
        return this;
    }

    public NowPlaying setIsPaused(boolean isPlaying)
    {
        this.isPaused = !isPlaying;
        return this;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    public boolean isPaused()
    {
        return this.isPaused;
    }

    public Movie getMovie()
    {
        return this.movie;
    }

}
