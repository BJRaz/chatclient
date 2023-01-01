package tfud.client;

import java.io.*;
import java.net.*;

/**
 *
 * @author BJR
 *
 * Abstract Baseclass for all Clients - override handleConnection() in
 * subclasses
 */
public abstract class Client  {

    protected Socket server;
    protected OutputStream out;
    protected InputStream in;

    /**
     * Method Client creates socket to server, and initializes output- and
     * inputstreams
     *
     * @param serveraddress
     * @param port
     * @throws IOException, UnknownHostExceotion
     * @throws java.net.UnknownHostException
     */
    public Client(String serveraddress, int port) throws IOException, UnknownHostException {

        server = new Socket(serveraddress, port);

        out = server.getOutputStream();
        in = server.getInputStream();

    }

    public Client() 
    {
        
    }
    
    /**
     * Abstract Method handleConnection Must be overriden i subclass -
     * implemented as Template Method Pattern
     */
    protected abstract void handleConnection();

}
