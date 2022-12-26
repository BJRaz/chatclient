package tfud.client.test;

import java.io.IOException;
import tfud.client.ChatClient;
import tfud.events.EventType;

public class TestClient {

    //private tfud.client.ChatClientFrame client;

    public TestClient() throws IOException, InterruptedException {
        ChatClient t = new ChatClient("localhost", 8900);

        t.startClient();
        t.setMessage(EventType.DUMMY, "test");
        //client = new tfud.client.ChatClientFrame(t);
    }

//    public static void main(String[] args) throws IOException {
//        try {
//            TestClient tc = new TestClient();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
