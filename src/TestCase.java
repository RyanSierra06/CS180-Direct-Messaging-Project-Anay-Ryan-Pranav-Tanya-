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
    User test1 = new User("testUser", "testPassword");
    if (test1.getUsername().equals("testUser") && test1.getPassword().equals("testPassword")) {
      System.out.println("test passed, user creation successful, username and password stored");
    } else {
      System.out.println("test failed, username or password does not match");
    }

    // test for ability to set and get profile description
    test1.setProfileDescription("test profile");
    if (test1.getProfileDescription().equals("test profile")) {
      System.out.println("test passed, get and set profile description successful");
    } else {
      System.out.println("test failed, get and set profile description does not match");
    }

    //test for ability to add friends
    test1.addFriend("friendUser");
    if (test1.getFriends().contains("friendUser")) {
      System.out.println("test passed, friend successfully added");
    } else {
      System.out.println("test failed, friend not added");
    }

    // test for ability to remove friends
    test1.removeFriend("friendUser");
    if (!test1.getFriends().contains("friendUser")) {
      System.out.println("test passed, friend successfully removed");
    } else {
      System.out.println("test failed, friend not removed");
    }

    // test for ability to block a friend
    test1.addFriend("blockFriendUser");
    test1.blockFriend("blockFriendUser");
    if (test1.getBlockedFriends().contains("blockFriendUser") && !test1.getFriends().contains("blockFriendUser")) {
      System.out.println("test passed, friend successfully blocked");
    } else {
      System.out.println("test failed, friend not blocked / friend still in friend list");
    }
  }

  private static void testMessageClass() {
    System.out.println("Testing Message Class: ");
    
  }

  private static void testDatabaseClass() {
    System.out.println("Testing Database Class: ");
    
  }
}
