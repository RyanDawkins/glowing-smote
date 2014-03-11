package com.ryanddawkins.glowing_smote;

import android.app.Activity;
import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;

/**
 * Class to write commands to the socket connection given
 *
 * @author Ryan Dawkins
 * @package com.ryanddawkins.glowing_smote
 * @version 0.1
 * @extends ClientConnection
 */
public class Command extends ClientConnection
{

    public static Command getConnection(String ipAddress, int port, Activity context)
    {
        Socket socket = getSocket(ipAddress, port, context);
        if(socket==null){ return null; }
        Command command = new Command(socket);
        command.setIpAddress(ipAddress);
        command.setPortNumber(port);
        return command;
    }

    public static final String GET_MOVIES = "GET_MOVIES";
    public static final String PLAY_MOVIE = "PLAY_MOVIE";
    public static final String PAUSE_MEDIA = "PAUSE_MEDIA";
    public static final String NEXT_CHAPTER = "NEXT_CHAPTER";
    public static final String PREVIOUS_CHAPTER = "PREVIOUS_CHAPTER";
    public static final String SKIP_FORWARD = "SKIP_FORWARD";
    public static final String SKIP_BACKWARD = "SKIP_BACKWARD";

    public Command(Socket socket)
    {
        super(socket);
    }

    public ArrayList<Movie> getMovies(String directory)
    {
        StringBuilder json = new StringBuilder();

        json.append("{");
            json.append("\"command\":");
                json.append("\"").append(GET_MOVIES).append("\"");
            json.append(",");
            json.append("\"directory\":");
                json.append("\"").append(directory).append("\"");
        json.append("}");

        ArrayList<Movie> movies = new ArrayList<Movie>();
        String response = writeJSON(json.toString()).getJSON();
        JsonElement jsonElement;
        Log.d("glowing-smote", response);
        try{
            jsonElement = new JsonParser().parse(response);
        } catch(JsonParseException e){
            Log.e("glowing-smote", "Bad gson", e);
            jsonElement = new JsonParser().parse("{}");
        }
        if(jsonElement.isJsonObject() && !jsonElement.isJsonNull())
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if(jsonObject.has("movies"))
            {
                JsonArray jsonArray = jsonObject.getAsJsonArray("movies");
                for(int i = 0; i < jsonArray.size(); i++)
                {
                    JsonObject movieObject = jsonArray.get(i).getAsJsonObject().get("movie").getAsJsonObject();
                    if(movieObject.has("name") && movieObject.has("fileName"))
                    {
                        Movie movie = new Movie(movieObject.get("name").getAsString());
                        movie.setFileName(movieObject.get("fileName").getAsString());
                        movie.setIsDirectory(Boolean.parseBoolean(movieObject.get("isDirectory").getAsString()));
                        Log.d("glowing-smote", movie.getFileName());
                        movies.add(movie);
                    }
                }
            }
        }
        return movies;
    }

    public Command playMovie(String filePath)
    {
        StringBuilder json = new StringBuilder();

        json.append("{");
            json.append("\"command\":").append("\"").append(PLAY_MOVIE).append("\"");
            json.append(",").append("\"filePath\":").append("\"").append(filePath).append("\"");
        json.append("}");

        writeJSON(json.toString());
        return this;
    }

    public Command simpleCommand(String simpleCommand)
    {
        StringBuilder json = new StringBuilder();

        json.append("{");
        json.append("\"command\":").append("\"").append(simpleCommand                       ).append("\"");
        json.append("}");

        writeJSON(json.toString());

        return this;
    }

}
