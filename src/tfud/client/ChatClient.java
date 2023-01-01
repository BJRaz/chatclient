package tfud.client;

import tfud.events.EventType;
import tfud.events.MessageEventHandler;
import tfud.events.ConnectionEventHandler;
import java.io.*;
import java.net.Socket;
import java.util.*;
import tfud.events.*;
import tfud.communication.*;

/**
 * @author BJR
 */
public class ChatClient extends Client implements Runnable, MessageEventHandler, ConnectionEventHandler {

    protected final static int MAXBUFFERLENGTH = 50;
    protected Vector outBuffer;

    protected String serverhost;

    protected int id;
    protected String handle;

    protected boolean finished;

    protected String username = "";
    protected String password = "";

    /* Client/Server protocol based on ObjectStreams */
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    protected MessageHandler handler;
    protected ConnectionHandler connhandler;
    
    private final Thread t = new Thread(this);
    private final int port;

    /**
     * @param	serveraddress string the domainaddress or TCP/IP address of server
     * @param	port	int of port to connect to
     * @throws java.io.IOException
     */
    public ChatClient(String serveraddress, int port) {

        super();

        

        this.serverhost = serveraddress;
        this.port = port;

        this.id = 0;
        this.handle = "";

        this.outBuffer = new Vector(MAXBUFFERLENGTH);
        this.handler = new MessageHandler();
        this.connhandler = new ConnectionHandler();

        // not running
        finished = true;

    }

    /**
     * Adds a message to the output buffer to be sent to server.
     * 
     * @param event
     * @param data
     * @throws InterruptedException
     */
    public synchronized void setMessage(EventType event, String data) throws InterruptedException {
        if (!isStopped()) {
            while (outBuffer.size() == MAXBUFFERLENGTH) // if vector full wait
            {
                wait();
            }
            outBuffer.addElement(new DataPackage(this.id, 0, this.handle, event, data));
            //releaseLock();
        }
    }

    public synchronized void setMessage(int target, EventType event, String data) throws InterruptedException {
        if (!isStopped()) {
            while (outBuffer.size() == MAXBUFFERLENGTH) {
                wait();
            }
            outBuffer.addElement(new DataPackage(this.id, target, this.handle, event, data));
            //releaseLock();
        }
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void startClient() throws IOException {
        if (finished) {
            server = new Socket(serverhost, port);
            out = server.getOutputStream();
            in = server.getInputStream();
            /* set object streams */
            output = new ObjectOutputStream(out);
            input = new ObjectInputStream(in);
            t.start();
            finished = false;
        }
    }

    public void stopClient() {
        if (!finished) {
            finished = true;
        }

        try {

            closeIO();

        } catch (IOException io) {
            System.out.println("STOP: IO close failed: " + io.getMessage());
        }

    }

    public boolean isStopped() {
        return finished;
    }

    /**
     * Method setUsername
     *
     *
     * @param username
     *
     */
    public void setUsername(String username) {
        // TODO: Add your code here
        this.username = username;
    }

    /**
     * Method setPassword
     *
     *
     * @param password
     *
     */
    public void setPassword(String password) {
        // TODO: Add your code here
        this.password = password;
    }

    @Override
    protected void handleConnection() {
        try {

            Object res;

            /**
             * INIT 
             */
            
            Object[] initialData = new Object[2];
            initialData[0] = username;
            initialData[1] = password;

            output.writeObject(new DataPackage(this.id, 0, this.handle, EventType.ONLINE, initialData));
            
            DataPackage data = (DataPackage) input.readObject();		// USERLIST, NOT ALLOWED TO LOGIN etc...

            this.id = data.getID();
            this.handler.handleMessage(data);

            output.writeObject(new DataPackage());

            /**
             * END INIT 
             */
            
            while (!finished) {

                /**
                 * READ INPUT
                 */
                // This reads messages from server ...
                res = input.readObject();

                if (res instanceof DataPackage) {
                    // can cause null exception - (res can be null is connection is down)
                    // Alert message listeners
                    /*
                    *	Here insert a handler of messages 
                    *	handler should parse message - and raise events respectively
                    */

                    data = (DataPackage) res;

                    if (data.getData() != null && !data.getData().toString().equals("")) {
                        this.handler.handleMessage(data);
                    }

                }

                /** 	
                *   SET OUTPUT
                *   for messages from this client to the server 
                */
                if(!outBuffer.isEmpty()) {
                    // removes first element in Lists
                    // - in effect simulates an Queue but not that efficient
                    data = (DataPackage) outBuffer.remove(0);

                    // Release lock as soon there's room in the List
                    // NOTICE - must be called from releaseLock function (sync) - otherwise 
                    //	Thread-Not-Owner Exception thrown
                    // releaseLock();

                    output.writeObject(data);
                } else
                    output.writeObject(new DataPackage());
                   

            }

        } catch (java.security.AccessControlException a) {
            this.connhandler.fireConnectionUpdated("Access denied while connecting - SET serverhostname in <PARAM .. >\n" + a.getMessage());
        } catch (IOException io) {
            this.connhandler.fireConnectionUpdated(io.getMessage());
        } catch (Exception e) {
            this.connhandler.fireConnectionUpdated("General error: " + e.getMessage());
        } finally {
            try {

                closeIO();

            } catch (IOException o) {
                System.out.println("IO Error in closing I/O ; " + o.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("General Closing IO; " + e.getMessage() + "\n");
            } finally {
                outBuffer.clear();
                finished = true;
                System.out.println("ChatClient ended");
            }
        }
    }

    private void closeIO() throws IOException {
        output.close();
        input.close();
        server.close();
    }

    private synchronized void releaseLock() {
        notifyAll();
    }

    @Override
    public void run() {
        handleConnection();
    }

    @Override
    public void addMessageListener(MessageListener m) {
        this.handler.addMessageListener(m);
    }

    @Override
    public void addConnectionListener(ConnectionListener c) {
        this.connhandler.addConnectionListener(c);
    }


}
