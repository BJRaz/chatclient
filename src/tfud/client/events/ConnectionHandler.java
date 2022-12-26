package tfud.client.events;

import java.util.ArrayList;
import java.util.List;
import tfud.events.ConnectionEvent;
import tfud.events.ConnectionEventHandler;
import tfud.events.ConnectionListener;

/**
 * @author BJR
 */
public class ConnectionHandler implements ConnectionEventHandler {

    private final List<ConnectionListener> connectionListeners;

    /**
     * Method ConnectionHandler
     *
     *
     */
    public ConnectionHandler() {
        // TODO: Add your code here
        connectionListeners = new ArrayList<>();
    }

    @Override
    public void addConnectionListener(ConnectionListener c) {
        connectionListeners.add(c);
    }

    public void fireConnectionUpdated(String command) {
        connectionListeners.forEach((c) -> {
            ConnectionEvent cEvt = new ConnectionEvent(c, command);
            c.connectionUpdated(cEvt);
        });
    }
}
