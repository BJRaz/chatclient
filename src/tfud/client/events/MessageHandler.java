package tfud.client.events;

import java.util.*;
import tfud.communication.DataPackage;
import tfud.events.MessageEventHandler;
import tfud.events.MessageListener;

/**
 * @author BJR
 */
public class MessageHandler implements MessageEventHandler {

    private final List<MessageListener> messageListeners;

    public MessageHandler() {
        messageListeners = new ArrayList<>();
    }

    @Override
    public void addMessageListener(MessageListener m) {
        messageListeners.add(m);
    }

    public void handleMessage(DataPackage p) {
        String eventType = p.getEventType();
        switch (eventType) {
            case "Online":
                fireOnlineReceived(p);
                break;
            case "Offline":
                fireOfflineReceived(p);
                break;
            case "Away":
                fireAwayReceived(p);
                break;
            case "Message":
                fireMessageReceived(p);
                break;
            case "PrivateMessage":
                firePrivateMessageReceived(p);
                break;
            case "UserList":
                fireUserListReceived(p);
                break;
            case "ChangeRoom":
                fireChangeRoomReceived(p);
                /* NOTICE THIS MUST BE GIVEN MORE THOUGHTS */
                break;
            case "Login":
                fireLoginMessageReceived(p);
                /* NOTICE THIS MUST BE GIVEN MORE THOUGHTS */
                break;
            case "ServerMessage":
                fireServerMessageReceived(p);
                /* NOTICE THIS MUST BE GIVEN MORE THOUGHTS */
                break;
            default:
                fireMessageReceived(p);
                break;
        }
    }

    public void fireAwayReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.awayMessageReceived(this, dp);
        });
    }

    public void fireMessageReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.messageReceived(this, dp);
        });
    }

    public void firePrivateMessageReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.privateMessageReceived(this, dp);
        });

    }

    public void fireOfflineReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.offlineMessageReceived(this, dp);
        });

    }

    public void fireOnlineReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.onlineMessageReceived(this, dp);
        });

    }

    public void fireUserListReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.userListReceived(this, dp);
        });

    }

    /* NOTICE THIS MUST BE GIVEN MORE THOUGHTS */
    public void fireChangeRoomReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.changeRoomReceived(this, dp);
        });

    }

    /* NOTICE THIS MUST BE GIVEN MORE THOUGHTS */
    public void fireLoginMessageReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.loginMessageReceived(this, dp);
        });

    }

    public void fireServerMessageReceived(DataPackage dp) {
        messageListeners.forEach((m) -> {
            m.serverMessageReceived(this, dp);
        });

    }
}
