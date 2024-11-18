import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run MessageTestCases
 * <p>
 * This is the Message Test Cases
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 3, 2024
 */

@RunWith(Enclosed.class) // Ensure nested test classes are executed
public class MessageTestCases {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("excellent - test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     * A set of public test cases to run
     * <p>
     * This is the Message Test Cases
     *
     * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
     * @version Nov 3, 2024
     */
    public static class TestCase {

        @Test(timeout = 1000)
        // test for ability to create message object with correctly set text and type
        public void testGetMessageText() {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest1 = new Message(userTest2, "text", "hello");
            Assert.assertEquals("hello", messageTest1.getMessageText());
        }

        @Test(timeout = 1000)
        // test for ability to create message object with correctly set image,
        // image type, and verification of empty text
        public void testGetImageMessage() {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest2 = new Message(userTest2, "image", "imageLink.png");
            Assert.assertEquals("imageLink.png", messageTest2.getMessageImage());
        }

        @Test(timeout = 1000)
        // test for ability to verify an invalid message type
        public void testInvalidMessageType() {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest3 = new Message(userTest2, "audio", "soundLink.mp3");
            Assert.assertEquals("Invalid Message Type", messageTest3.getType());
        }


        @Test(timeout = 1000)
        public void testAssignToUser() {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest4 = new Message(userTest2, "text", "test message");
            Assert.assertEquals(messageTest4.assignToUser(messageTest4.getMessageText(),
                    "text", userTest2.getUsername()), true);
        }

        @Test(timeout = 1000)
        public void testGetMainUser() {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest5 = new Message(userTest2, "text", "test message");
            Assert.assertEquals(userTest2, messageTest5.getMainUser());
        }
    }
}


