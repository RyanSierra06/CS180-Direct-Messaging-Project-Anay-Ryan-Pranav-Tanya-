/**
 * Phase 1 Test Class for Team Project
 * 
 * tests the functionality of the User, Message, and Database classes
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
        testDatabaseClass();
  }

  // TEST 1: testing user class
  private static void testUserClass() {
    System.out.println("testing User class: ");
    
    // test for ability to create a new user storing the username and pw correctly
    User userTest1 = new User("testUser", "testPassword");
    if (userTest1.getUsername().equals("testUser") && userTest1.getPassword().equals("testPassword")) {
      System.out.println("test passed, user creation successful, username and password stored");
    } else {
      System.out.println("test failed, username or password does not match");
    }

    // test for ability to set and get profile description
    userTest1.setProfileDescription("test profile");
    if (userTest1.getProfileDescription().equals("test profile")) {
      System.out.println("test passed, get and set profile description successful");
    } else {
      System.out.println("test failed, get and set profile description does not match");
    }

    //test for ability to add friends
    userTest1.addFriend("friendUser");
    if (userTest1.getFriends().contains("friendUser")) {
      System.out.println("test passed, friend successfully added");
    } else {
      System.out.println("test failed, friend not added");
    }

    // test for ability to remove friends
    userTest1.removeFriend("friendUser");
    if (!userTest1.getFriends().contains("friendUser")) {
      System.out.println("test passed, friend successfully removed");
    } else {
      System.out.println("test failed, friend not removed");
    }

    // test for ability to block a friend
    userTest1.addFriend("blockFriendUser");
    userTest1.blockFriend("blockFriendUser");
    if (userTest1.getBlockedFriends().contains("blockFriendUser") && !userTest1.getFriends().contains("blockFriendUser")) {
      System.out.println("test passed, friend successfully blocked");
    } else {
      System.out.println("test failed, friend not blocked / friend still in friend list");
    }
  }

  // TEST 2: testing message class
  private static void testMessageClass() {
    System.out.println("testing Message class: ");

    // test for ability to create message object with correctly set text and type
    User userTest2 = new User("testUser", "testPassword");
    Message messageTest1 = new Message(userTest2, "text", "hello world!");
    if (messageTest1.getMessageText().equals("hello world!") && messageTest1.getType().equals("text") && messageTest1.getMessageImage().isEmpty()) {
      System.out.println("test passed, text message successfully created");
    } else {
      System.out.println("test failed, text message not created");
    }

    // test for ability to create message object with correctly set image, image type, and verification of empty text
    Message messageTest2 = new Message(userTest2, "image", "imageLink.png");
    if (messageTest2.getMessageImage().equals("imageLink.png") && messageTest2.getType().equals("image") && messageTest2.getMessageTest().isEmpty()) {
      System.out.println("test passed, image message successfully created");
    } else {
      System.out.println("test failed, image message not created");
    }

    // test for ability to verify an invalid message type
    Message messageTest3 = new Message(userTest2, "audio", "soundLink.mp3");
    if (messageTest3.getType().equals("invalid message type") && messageTest3.getMessageTest().isEmpty() && messageTest3.getMessageImage().isEmpty()) {
      System.out.println("test passed, invalid message type correctly handled");
    } else {
      System.out.println("test failed, invalid message type incorrectly handled");
    }
  }

  // TEST 3: testing database class
  private static void testDatabaseClass() {
    System.out.println("testing Database class: ");
    
  }
}
