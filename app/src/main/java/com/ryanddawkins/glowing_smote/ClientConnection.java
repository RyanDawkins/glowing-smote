package com.ryanddawkins.glowing_smote;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 */
public class ClientConnection
{

    public static final String TERMINATOR = "--DONE--";

    public static Socket getSocket(String ipAddress, int portNumber, Activity context)
    {
        InetAddress host = null;
        Socket socket;
        try
        {
            host = InetAddress.getByName(ipAddress);
            Log.d("ipaddress", ipAddress);
            socket = new Socket(host, portNumber);
        }
        catch(UnknownHostException e)
        {
            Log.e("glowing-smote", "Unknown host exception", e);
            return null;
        }
        catch(ConnectException e)
        {
            Log.e("glowing-smote", "", e);
            if(portNumber != Settings.DEFAULT_PORT)
            {
                portNumber = Settings.DEFAULT_PORT;
                try
                {
                    socket = new Socket(host, portNumber);
                }
                catch(ConnectException e2)
                {
                    Log.e("glowing-smote", "IOException", e2);
                    return null;
                }
                catch(IOException e2) { socket=null; }
            }
            else
            {
                return null;
            }
        }
        catch(IOException e) {
            socket = null;
        }
        return socket;
    }

    private Socket socket;
    private String ipAddress;
    private int port;

    /**
     * Recommended to use the ClientConnection.getConnection(String ip, int port) method
     *
     * @param socket
     */
    public ClientConnection(Socket socket)
    {
        this.socket = socket;
    }

    /**
     * Method to take the outputStream from the socket object and print json data and end it with the terminator
     *
     * @param json
     * @return ClientConnection this
     */
    public ClientConnection writeJSON(String json)
    {
        try
        {
            PrintWriter writer = new PrintWriter(this.socket.getOutputStream());
            writer.println(json);
            writer.println(TERMINATOR);
            writer.flush();
        }
        catch(IOException e){
            // Do something
        }
        catch(NullPointerException e){
            // No socket
        }
        return this;
    }

    /**
     * Method to grab the json from the sockets response
     *
     * @return String json
     */
    public String getJSON()
    {
        try
        {
            String input;
            StringBuilder jsonBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while((input = reader.readLine()) != null)
            {
                if(input.equals(ClientConnection.TERMINATOR))
                {
                    break;
                }
                else
                {
                    jsonBuilder.append(input);
                }
            }
            return jsonBuilder.toString();

        }
        catch(IOException e)
        {
            // Bad. Fill later
            return null;
        }
    }

    /**
     * Chainable method to set ipAddress
     * @param ipAddress
     * @return ClientConnection this
     */
    public ClientConnection setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
        return this;
    }

    /**
     * Getter for the ip address
     *
     * @return String ipAddress
     */
    public String getIpAddress()
    {
        return this.ipAddress;
    }

    /**
     * Chainable method to set the port number
     *
     * @param portNumber
     * @return ClientConnection this
     */
    public ClientConnection setPortNumber(int portNumber)
    {
        this.port = portNumber;
        return this;
    }

    /**
     * Getter for the port number
     *
     * @return int port
     */
    public int getPortNumber()
    {
        return this.port;
    }

}
