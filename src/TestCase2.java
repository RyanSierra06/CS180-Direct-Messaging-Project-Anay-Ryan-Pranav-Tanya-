import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.lang.reflect.Modifier;

/**
 * A framework to run public test cases for PJ4.
 *
 * <p>Purdue University -- CS18000 -- Spring 2021</p>
 *
 * @author J Morris Purdue CS
 * @version Mar 08, 2024
 */

public class TestCase2 {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {
        @Test(timeout = 1000)
        public void runUserTests() {

            User initialUser = new User("user1", "user1");
            initialUser.setName("Dan");
            initialUser.setProfileDescription("Hi I'm Dan");
            initialUser.setProfilePicture("./files/Danpfp.jpg");
            initialUser.setReceiveAnyone(true);

            User secondUser = new User("user2", "user2");
            initialUser.setName("Ben");
            initialUser.setProfileDescription("Hi I'm ben");
            initialUser.setProfilePicture("./files/benpfp.jpg");
            initialUser.setReceiveAnyone(false);

            Message msg = new Message(initialUser, "text", "hehehe");
            boolean menatToBreak = initialUser.sendMessage(msg, secondUser.getUsername());

            secondUser.addFriend(initialUser.getUsername());
            initialUser.addFriend(initialUser.getUsername());
            boolean meantToWork = initialUser.sendMessage(msg, secondUser.getUsername());

            String user1Friends = initialUser.getFriends();

            User thirdUser = new User("user2", "user2");
            initialUser.setName("Ben");
            initialUser.setProfileDescription("Hi I'm ben");
            initialUser.setProfilePicture("./files/benpfp.jpg");
            initialUser.setReceiveAnyone(false);








            

            String expectedOutput = "StarterMaze\n" +
                    "Moves: 48\n" +
                    "Start\n" +
                    "6-0\n" +
                    "6-1\n" +
                    "6-2\n" +
                    "7-2\n" +
                    "8-2\n" +
                    "9-2\n" +
                    "10-2\n" +
                    "11-2\n" +
                    "12-2\n" +
                    "13-2\n" +
                    "14-2\n" +
                    "14-3\n" +
                    "14-4\n" +
                    "14-5\n" +
                    "14-6\n" +
                    "14-7\n" +
                    "14-8\n" +
                    "13-8\n" +
                    "12-8\n" +
                    "11-8\n" +
                    "10-8\n" +
                    "9-8\n" +
                    "8-8\n" +
                    "7-8\n" +
                    "6-8\n" +
                    "5-8\n" +
                    "5-7\n" +
                    "5-6\n" +
                    "6-6\n" +
                    "7-6\n" +
                    "8-6\n" +
                    "9-6\n" +
                    "9-5\n" +
                    "9-4\n" +
                    "8-4\n" +
                    "7-4\n" +
                    "6-4\n" +
                    "5-4\n" +
                    "4-4\n" +
                    "3-4\n" +
                    "2-4\n" +
                    "1-4\n" +
                    "1-5\n" +
                    "1-6\n" +
                    "1-7\n" +
                    "1-8\n" +
                    "1-9\n" +
                    "1-10\n" +
                    "End\n";

            String actualOutput = "";

            try (BufferedReader reader = new BufferedReader(new FileReader(output))) {
                String in = "";
                while ((in = reader.readLine()) != null) {
                    actualOutput += in + "\n";
                }
            } catch (IOException a) {
                Assert.assertTrue("An IO exception was encountered while reading StarterMazeSolution.txt\n" +
                        "Make sure writeSolution is writing to the given file.", false);
            } catch (Exception e) {
                Assert.assertTrue("An unknown exception was encountered", false);
            }

            Assert.assertEquals("Ensure that Maze's pathString method and MazeSolver's solveMaze are generating the correct solutions.",
                    expectedOutput.trim(), actualOutput.trim());

        }

    }

}