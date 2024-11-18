import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run UserTestCases
 *
 * This is the User Test Cases 
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 3, 2024
 *
 */

@RunWith(Enclosed.class) // Ensure nested test classes are executed
public class UserTestCases {
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

    /**
     * A set of public test cases to run
     *
     * This is the User Test Cases 
     *
     * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
     *
     * @version Nov 3, 2024
     *
     */
    public static class TestCase {

        @Test(timeout = 1000)
        public void programTestCase() {

            User initialUser = new User("user1", "user1");
            initialUser.setName("Dan");
            initialUser.setProfileDescription("Hi I'm Dan");
            initialUser.setProfilePicture("./files/Danpfp.jpg");
            initialUser.setReceiveAnyone(true);

            User secondUser = new User("user2", "user2");
            secondUser.setName("Ben");
            secondUser.setProfileDescription("Hi I'm ben");
            secondUser.setProfilePicture("./files/benpfp.jpg");
            secondUser.setReceiveAnyone(false);

            Message msg = new Message(initialUser, "text", "hehehe");
            boolean meantToBreak = initialUser.sendMessage(msg, secondUser.getUsername());

            boolean addedFriend = secondUser.addFriend(initialUser.getUsername());
            boolean addedFriend2 = initialUser.addFriend(secondUser.getUsername());
            boolean meantToWork = initialUser.sendMessage(msg, secondUser.getUsername());


            String user1Friends = initialUser.getFriends();

            User thirdUser = new User("user3", "user3");
            thirdUser.setName("Son");
            thirdUser.setProfileDescription("Hi I'm son");
            thirdUser.setProfilePicture("./files/sonpfp.jpg");
            thirdUser.setReceiveAnyone(false);


            boolean ableToBlock = initialUser.blockUser(thirdUser.getUsername());
            String userOneBlocks = initialUser.getBlockedUsers();
            boolean removedBlock = initialUser.unblockUser(thirdUser.getUsername());
            String userOnePostBlocks = initialUser.getBlockedUsers();

            boolean nonExistentUser = initialUser.blockUser("nonexistent");

            boolean removedFriend = initialUser.removeFriend(secondUser.getUsername());
            String msgRead = initialUser.readMessages(secondUser.getUsername());
            boolean msgRemoved = initialUser.deleteMessage(secondUser.getUsername(), msg);
            String userFinder = initialUser.findUser(thirdUser.getUsername());

            Assert.assertEquals("1", meantToBreak, false);
            Assert.assertEquals("2", addedFriend, true);
            Assert.assertEquals("3", addedFriend2, true);

            Assert.assertEquals("4", meantToWork, true);
            Assert.assertEquals("5", user1Friends, secondUser.getUsername());

            Assert.assertEquals("6", ableToBlock, true);
            Assert.assertEquals("7", userOneBlocks, thirdUser.getUsername());
            Assert.assertEquals("8", removedBlock, true);
            Assert.assertEquals("9", userOnePostBlocks, "");


            Assert.assertEquals("10", nonExistentUser, false);
            Assert.assertEquals("11", removedFriend, true);
            Assert.assertEquals("12", msgRead, "user1-user2-hehehe\n");
            Assert.assertEquals("13", msgRemoved, true);
            Assert.assertEquals("14", userFinder, "user3-Son-Hi I'm son-./files/sonpfp.jpg-false");

            initialUser.blockUser(thirdUser.getUsername());
            initialUser.addFriend(secondUser.getUsername());
        }

        @Test(timeout = 1000)
        public void runDatabaseTestCases() {
            User initialUser = new User("user1", "user1");
            String initialUserName = initialUser.getName();
            String initialUserPass = initialUser.getPassword();
            String initialUserProfDes = initialUser.getProfileDescription();
            String initialUserProfPic = initialUser.getProfilePicture();
            String initialUserFriends = initialUser.getFriends();
            String initialUserBlocked = initialUser.getBlockedUsers();

            User secondUser = new User("user2", "user2");


            String msg = initialUser.readMessages(secondUser.getUsername());

            Assert.assertEquals("15", initialUserName, "Dan");
            Assert.assertEquals("16", initialUserPass, "user1");
            Assert.assertEquals("17", initialUserProfDes, "Hi I'm Dan");
            Assert.assertEquals("18", initialUserProfPic, "./files/Danpfp.jpg");
            Assert.assertEquals("19", initialUserFriends, secondUser.getUsername());
            Assert.assertEquals("20", initialUserBlocked, "user3");
            Assert.assertEquals("21", msg, "");

            initialUser.setReceiveAnyone(true);
            initialUser.addFriend(secondUser.getUsername());
            secondUser.addFriend(initialUser.getUsername());
            boolean bool = User.checkCanReceiveAnyone(initialUser.getUsername());
            boolean bool2 = User.checkIsFriend(initialUser.getUsername(), secondUser.getUsername());
            
            Assert.assertEquals("22", true, bool);
            Assert.assertEquals("23", true, bool2);

            initialUser.blockUser(secondUser.getUsername());
            boolean bool3 = initialUser.isBlocked(initialUserName, secondUser.getUsername());
            Assert.assertEquals("24", true, bool3);
        }
    }
}