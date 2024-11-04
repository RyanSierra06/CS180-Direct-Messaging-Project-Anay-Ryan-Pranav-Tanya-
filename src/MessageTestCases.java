import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A framework to run Message test cases.
 *
 * <p>Purdue University -- CS18000 -- Spring 2021</p>
 *
 * @author Tanya Jain
 * @version November 3, 2024
 */
@RunWith(Enclosed.class)
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

    public static class TestCase {

        @Test(timeout = 1000)
        // test for ability to create message object with correctly set text and type
        public void testCreateTextMessage () {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest1 = new Message(userTest2, "text", "hello");
            Assert.assertEquals("hello", messageTest1.getMessageText());
            Assert.assertEquals("text",  messageTest1.getType());
            Assert.assertTrue(messageTest1.getMessageImage().isEmpty());
        }

        @Test(timeout = 1000)
        // test for ability to create message object with correctly set image, image type, and verification of empty text
        public void testCreateImageMessage () {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest2 = new Message(userTest2, "image", "imageLink.png");
            Assert.assertEquals("imageLink.png", messageTest2.getMessageImage());
            Assert.assertEquals("image", messageTest2.getType());
            Assert.assertTrue(messageTest2.getMessageImage().isEmpty());
        }

        @Test(timeout = 1000)
        // test for ability to verify an invalid message type
        public void testInvalidMessageType () {
            User userTest2 = new User("testUser", "testPassword");
            Message messageTest3 = new Message(userTest2, "audio", "soundLink.mp3");
            Assert.assertEquals("Invalid Message Type", messageTest3.getType());
            Assert.assertTrue(messageTest3.getMessageImage().isEmpty());
        }
    }
}


