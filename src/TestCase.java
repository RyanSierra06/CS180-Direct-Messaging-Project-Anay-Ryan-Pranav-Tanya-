import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 1 Test Class for Team Project
 * 
 * tests the functionality of the User and Message classes
 * 
 * @author Tanya Jain, lab sec 12
 * 
 * @version November 2, 2024
 *
 */

public class TestCase {
  public static void main(String[] args) {
        testUserClass();
        testMessageClass();
  }

  // TEST 1: testing user class
  private static void testUserClass() {
    System.out.println("testing User class: ");

    @Test
    // test for ability to create a new user storing the username and pw correctly
    public void testUserCreation() {
      User userTest1 = new User("testUser", "testPassword");
      assertEquals("testUser", userTest1.getUsername(), "test failed, username does not match");
      assertEquals("testPassword", userTest1.getPassword(), "test failed, password does not match");
    }

    @Test
    // test for ability to set and get profile description
    public void testSetandGetProfileDescription() {
      User userTest1 = new User("testUser", "testPassword");
      userTest1.setProfileDescription("test profile");
      assertEquals("test profile", userTest1.getProfileDescription(), "test failed, get and set profile description does not match");
    }

    @Test
    //test for ability to add friends
    public void testAddFriend() {
      User userTest1 = new User("testUser", "testPassword");
      userTest1.addFriend("friendUser");
      assertTrue(userTest1.getFriends().contains("friendUser"), "test failed, friend not added");
    }

    @Test
    // test for ability to remove friends
    public void testRemoveFriend() {
      User userTest1 = new User("testUser", "testPassword");
      userTest1.addFriend("friendUser");
      userTest1.removeFriend("friendUser");
      assertFalse(userTest1.getFriends().contains("friendUser"), "test failed, friend not removed");
    }

    @Test
    // test for ability to block a friend
    public void testBlockFriend() {
      User userTest1 = new User("testUser", "testPassword");
      userTest1.addFriend("blockFriendUser");
      userTest1.removeFriend("blockFriendUser");
      assertTrue(userTest1.getBlockedFriends().contains("blockFriendUser"), "test failed, friend not blocked");
      assertFalse(userTest1.getFriends().contains("blockFriendUser"), "test failed, blocked friend still in friend list");
    }
  }

  // TEST 2: testing message class
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
