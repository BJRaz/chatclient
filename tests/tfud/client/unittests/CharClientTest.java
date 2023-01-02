/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfud.client.unittests;

import org.junit.*;
import tfud.client.ChatClient;
import tfud.events.EventType;

/**
 *
 * @author brian
 */
public class CharClientTest {
    
  @Test(expected = NullPointerException.class)
  public void setMessageEventArgExceptionTest() {
      ChatClient c = new ChatClient();
      c.setMessage(null, null);
  }
  
  @Test(expected = NullPointerException.class)
  public void setMessageDataArgNullExceptionTest() {
      ChatClient c = new ChatClient();
      c.setMessage(EventType.AWAY, null);
  }
  
  @Test(expected = NullPointerException.class)
  public void setMessageTest() {
      ChatClient c = new ChatClient();
      c.setMessage(EventType.AWAY, null);
  }
  
  @Test
  public void test() {
    ChatClient c = new ChatClient();
    c.addConnectionListener(null);
    c.addMessageListener(null);
    c.isStopped();
    c.setHandle(null);
    c.start();
    c.stopClient();
  }
}
