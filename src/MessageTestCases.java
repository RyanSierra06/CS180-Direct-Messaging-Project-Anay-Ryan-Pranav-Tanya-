import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run public test cases for PJ4.
 *
 * <p>Purdue University -- CS18000 -- Spring 2021</p>
 *
 * @author J Morris Purdue CS
 * @version Mar 08, 2024
 */

 private static void testMessageClass() {
    System.out.println("testing Message class: ");

    @Test
    // test for ability to create message object with correctly set text and type
    public void testCreateTextMessage() {
      User userTest2 = new User("testUser", "testPassword");
      Message messageTest1 = new Message(userTest2, "text", "hello");
      assertEquals("hello", messageTest1.getMessageText(), "test failed, text message does not match");
      assertEquals("text", messageTest1.getType(), "test failed, text message type is not 'text'");
      assertTrue(messageTest1.getMessageImage().isEmpty(), "test failed, message image not empty");
    }

    @Test
    // test for ability to create message object with correctly set image, image type, and verification of empty text
    public void testCreateImageMessage() {
      User userTest2 = new User("testUser", "testPassword");
      Message messageTest2 = new Message(userTest2, "image", "imageLink.png");
      assertEquals("imageLink.png", messageTest2.getMessageImage(), "test failed, image message does not match");
      assertEquals("image", messageTest2.getType(), "test failed, text message type is not 'image'");
      assertTrue(messageTest2.getMessageText().isEmpty(), "test failed, message text not empty");
    }

    @Test
    // test for ability to verify an invalid message type
    public void testInvalidMessageType() {
      User userTest2 = new User("testUser", "testPassword");
      Message messageTest3 = new Message(userTest2, "audio", "soundLink.mp3");
      assertEquals("invalid message type", messageTest3.getType(), "test failed, invalid message type incorrectly handled");
      assertTrue("messageTest3.getMessageText().isEmpty(), "test failed, message text should be empty for invalid type");
      assertTrue("messageTest3.getMessageImage().isEmpty(), "test failed, message image should be empty for invalid type");
    }
  }
}
