package com.ryanddawkins.glowing_smote;

import java.lang.StringBuilder;

/**
 * Movie class created to manage data about movies
 *
 * @author Ryan Dawkins
 * @version 0.1
 */
public class Movie
{

	private String name;
	private String fileName;
    private boolean isDirectory;

	/**
	 * Constructor to create a movie instance and store it's "movie name"
	 *
	 * @param fileName
	 */
	public Movie(String fileName)
	{
        // Removes file extension
        if(fileName.lastIndexOf('.') > -1)
        {
            this.name = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        else
        {
            this.name = fileName;
        }
	}

    /**
     * Movie to set if is a directory
     * @param isDirectory
     * @return false
     */
    public Movie setIsDirectory(boolean isDirectory)
    {
        this.isDirectory = isDirectory;
        return this;
    }

    /**
     * Method to check if directory to make another request to grab more movie files
     *
     * @return boolean
     */
    public boolean isDirectory()
    {
        return this.isDirectory;
    }

	/**
	 * Chainable method to set file path
	 *
	 * @param filePath
	 * @return Movie this
	 */
	public Movie setFilePath(String filePath)
	{
		this.fileName = filePath;
		return this;
	}

	/**
	 * Getter for filepath
	 *
	 * @return String filePath
	 */
	public String getFilePath()
	{
		return this.fileName;
	}

	/**
	 * Sets name of the movie and returns this
	 *
	 * @param name
	 * @return Movie this
	 */
	public Movie setName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Returns name of the movie object
	 *
	 * @return String movie
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets file name and returns this for chainability
	 *
	 * @param fileName
	 * @return Movie this
	 */
	public Movie setFileName(String fileName)
	{
		this.fileName = fileName;
		return this;
	}

	/**
	 * Returns fileName
	 *
	 * @return String fileName
	 */
	public String getFileName()
	{
		return this.fileName;
	}

	/**
	 * Calls the static Movie.toJSON() method
	 *
	 * @return String json
	 */
	public String toJSON()
	{
		return Movie.toJSON(this);
	}

	/**
	 * Returns a JSON encoded string of the object Movie
	 *
	 * @param m
	 * @return String json
	 */
    public static String toJSON(Movie m)
    {
        // Not synchronized, but has better runtime
        StringBuilder json = new StringBuilder();
        json.append("{");
            json.append("\"movie\":{");
                json.append("\"name\":\"").append(m.name).append("\",");
                json.append("\"fileName\":\"").append(m.fileName).append("\"");
                json.append("\"isDirectory\":\"").append(m.isDirectory()).append("\"");
            json.append("}");
        json.append("}");
        return json.toString();
    }

}