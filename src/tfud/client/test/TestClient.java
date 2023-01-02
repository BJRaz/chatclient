package tfud.client.test;

import java.io.IOException;
import tfud.client.ChatClient;
import tfud.events.EventType;

public class TestClient {

    ChatClient t;
    
    public TestClient() {
        t = new ChatClient("localhost", 8900);
        
    }

    public void test() throws IOException, InterruptedException {
        t.setMessage(EventType.ONLINE, null);
        t.setMessage(EventType.ONLINE, null);
        t.startClient();
    }
    
    public static void main(String[] args) {
        try {
            TestClient tc = new TestClient();
            tc.test();
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
