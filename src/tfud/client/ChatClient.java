package tfud.client;

import tfud.events.EventType;
import tfud.events.MessageEventHandler;
import tfud.events.ConnectionEventHandler;
import java.io.*;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.*;
import tfud.events.*;
import tfud.communication.*;

/**
 * @author BJR
 */
public class ChatClient extends Client implements Runnable, MessageEventHandler, ConnectionEventHandler {

    protected final static int MAXBUFFERLENGTH = 50;
    protected List<DataPackage> outBuffer;

    protected boolean finished;

    protected final String serverhost;
    protected final int port;
    protected int id;
    protected String handle;
    protected String username = "";
    protected String password = "";

    /* Client/Server protocol based on ObjectStreams */
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    protected MessageHandler handler;
    protected ConnectionHandler connhandler;

    private Thread t;
    private Object res;
    private DataPackage data;

    /**
     * @param	serveraddress string the domainaddress or TCP/IP address of server
     * @param	port	int of port to connect to
     */
    public ChatClient(String serveraddress, int port) {

        super();

        finished = true;

        id = 0;
        handle = "";
        serverhost = serveraddress;
        this.port = port;

        outBuffer = new ArrayList<>(MAXBUFFERLENGTH);
        handler = new MessageHandler();                // hard dependency
        connhandler = new ConnectionHandler();         // hard dependency
    }

    public ChatClient() {
        port = 0;
        outBuffer = new ArrayList<>(MAXBUFFERLENGTH);
        serverhost = "localhost";
    }

    /**
     * Adds a message to the output buffer to be sent to server.
     *
     * @param event
     * @param data
     */
    public synchronized void setMessage(EventType event, String data) {
        if (event == null || data == null) {
            throw new NullPointerException("event arg must not be null");
        }
        if (!isStopped()) {
//            while (outBuffer.size() == MAXBUFFERLENGTH) // if vector full wait
//            {
//                wait();
//            }
            outBuffer.add(new DataPackage(this.id, 0, this.handle, event, data));
            //releaseLock();
        }
    }

    public synchronized void setMessage(int target, EventType event, String data) {
        if (!isStopped()) {
//            while (outBuffer.size() == MAXBUFFERLENGTH) {
//                wait();
//            }
            outBuffer.add(new DataPackage(this.id, target, this.handle, event, data));
            //releaseLock();
        }
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void startClient() {
        if (isStopped()) {
            t = new Thread(this); 
            switch(t.getState()) {
            
                case NEW:
                    t.start();
                    break;
                case RUNNABLE:
                    System.out.print(1);
                    break;
                case BLOCKED:
                    System.out.print(2);
                    break;
                case WAITING:
                    System.out.print(3);
                    break;
                case TIMED_WAITING:
                    System.out.print(4);
                    break;
                case TERMINATED:
                    System.out.print(5);
                    break;
            
            }
            
            finished = false;
        }
    }

    public void stopClient() {
        if (!isStopped()) {
            finished = true;
            t = null;
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
        this.password = password;
    }

    protected void closeIO() {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (server != null) {
                server.close();
            }
        } catch (IOException o) {
            this.connhandler.fireConnectionUpdated(new ConnectionEvent(this, "IO Error in closing I/O ; " + o.getMessage()));
        }
    }

    private synchronized void releaseLock() {
        notifyAll();
    }

    @Override
    public void run() {
        execute();
    }

    @Override
    public void addMessageListener(MessageListener m) {
        handler.addMessageListener(m);
    }

    @Override
    public void addConnectionListener(ConnectionListener c) {
        connhandler.addConnectionListener(c);
    }

    class INPUT implements Runnable {

        @Override
        public synchronized void run() {
            /**
             * READ INPUT
             */
            // This reads messages from server ...

            try {
                while (!isStopped()) {
                    res = input.readObject();
                    if (res == null) {
                        wait();
                    }

                    if (res instanceof DataPackage) {
                        // can cause null exception - (res can be null is connection is down)
                        // Alert message listeners
                        /*
                        *	Here insert a handler of messages 
                        *	handler should parse message - and raise events respectively
                         */

                        data = (DataPackage) res;

                        if (data.getData() != null && !data.getData().toString().equals("")) {
                            handler.handleMessage(data);
                        }
                    }
                }
            } catch (IOException e) {
                connhandler.fireConnectionUpdated(new ConnectionEvent(this, e.getMessage()));
            } catch (ClassNotFoundException e) {
                connhandler.fireConnectionUpdated(new ConnectionEvent(this, e.getMessage()));
            } catch (InterruptedException e) {
                connhandler.fireConnectionUpdated(new ConnectionEvent(this, e.getMessage()));
            }

        }
    }

    class OUTPUT implements Runnable {

        @Override
        public void run() {
            /**
             * READ INPUT
             */
            // This reads messages from server ...

            //            try {
            //                while (!isStopped()) {
            //
            //                }
            //            } catch (IOException e) {
            //                connhandler.fireConnectionUpdated(e.getMessage());
            //            } catch (InterruptedException e) {
            //                connhandler.fireConnectionUpdated(e.getMessage());
            //            }
        }
    }

    @Override
    protected void initiateConnection() {

        Object[] initialData = new Object[2];
        initialData[0] = username;
        initialData[1] = password;

        try {

            /**
             * PROTOCOL:
             *
             * 1) send a ONLINE event to server 2) read LOGIN event response
             * from server. 3) read USERLIST event response from server.
             *
             */
            server = new Socket(serverhost, port);
            out = server.getOutputStream();
            in = server.getInputStream();

            /* set object streams */
            output = new ObjectOutputStream(out);
            input = new ObjectInputStream(in);

            output.writeObject(new DataPackage(this.id, 0, this.handle, EventType.ONLINE, initialData));    // 1) send ONLINE event

            data = (DataPackage) input.readObject();                                                        // 2) receive LOGIN event

            id = data.getID();                                                                              // - get ID from server (?)
            handler.handleMessage(data);                                                                    // - handle the response from server

            output.writeObject(new DataPackage());                                                          // send DUMMY event to server 
            data = (DataPackage) input.readObject();                                                        // 3) receive USERLIST event
            handler.handleMessage(data);                                                                    // - handle the response from server
            
        } catch (IOException | ClassNotFoundException e) {
            this.connhandler.fireConnectionUpdated(new ConnectionEvent(this, e.getMessage()));
            finished = true;
           
        }

    }

    @Override
    protected void handleConnection() {
        try {
            //            Thread in = new Thread(new INPUT());
            //            in.execute();
            //
            //            Thread out = new Thread(new OUTPUT());
            //            out.execute();
            while (!isStopped()) {
                res = input.readObject();

                if (res instanceof DataPackage) {
                    // can cause null exception - (res can be null is connection is down)
                    // Alert message listeners
                    /*
                    *	Here insert a handler of messages 
                    *	handler should parse message - and raise events respectively
                     */

                    data = (DataPackage) res;

                    if (data.getEventType() != EventType.DUMMY) {
                        handler.handleMessage(data);
                    }
                }

                /**
                 * SET OUTPUT for messages from this client to the server
                 */
                if (!outBuffer.isEmpty()) {

                    // removes first element in Lists
                    // - in effect simulates an Queue but not that efficient
                    data = outBuffer.remove(0);

                    // Release lock as soon there's room in the List
                    // NOTICE - must be called from releaseLock function (sync) - otherwise 
                    //	Thread-Not-Owner Exception thrown
                    // releaseLock();
                    output.writeObject(data);
                } else {
                    output.writeObject(new DataPackage());
                }
            }
        } catch (java.security.AccessControlException e) {
            this.connhandler.fireConnectionUpdated(new AccessErrorEvent(this, "Access denied while connecting - SET serverhostname in <PARAM .. >\n" + e.getMessage()));
        } catch (IOException e) {
            this.connhandler.fireConnectionUpdated(new ConnectionEvent(this, e.getMessage()));
        } catch (Exception e) {
            this.connhandler.fireConnectionUpdated(new ConnectionEvent(this, e.getMessage()));
        }
    }

    @Override
    protected void closeConnection() {
        closeIO();
        outBuffer.clear();
        finished = true;
        this.connhandler.fireConnectionUpdated(new ConnectionEvent(this, "Connection ended"));
    }
}
