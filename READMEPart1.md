# CS180-Direct-Messaging-Project-Anay-Ryan-Pranav-Tanya-

## How to Run?
Use the UserTestCases and MessageTestCases files to run and test both classes. If you want to run the tests multiple times make sure to delete all the files within the files folder and make usernamesAndPasswords.txt empty. We need to do this since our program appends to some files and rewrites others and the test cases don't reset the workspace. There are multiple testcases that ensure our files are saved between programs properly.


## Who did what?
- Submitted on Vocareum - Pranav
- User.java - Ryan, Pranav
- UserInterface.java - Ryan, Pranav
- Message.java - Anay
- MessageInterface.java - Anay
- MessageTestCases.java - Tanya
- UserTestCases.java - Tanya

## Class Descriptions
The user class is the central class of the program and it has a bunch of uitilty functions for the current and later versions of the program. The User class acts as the database, creating and changing files within the files directory. There are 3 different type of files the User databse creates. The first is "username.txt" which houses all the infomation about the user including their blocked list, friends list, and atributes about their profile. The second one is "usernamesAndPasswords.txt" which houses all the different users created throughout the entire use of the server and client use. The third is "firstUsername-secondUsername.txt" which houses all the messages between 2 users. 

The message class instantiates messages of either of 2 types - image (extra credit) or text- and assigns them to the user who sent them. The Database is created by the User class each time it is referenced in the main Application. The message class also creates another file which stores all of the messages that one user sends to everyone.

Finally the test cases check each and every non helper method function of the User and Message classes and if the files that comprise the database are updated/modified correctly

To actually see how the program is working, you must run the provided test cases. That is pretty much all one needs to check the work done in Phase 1.

A solid foundation has been laid for Phase 2 of the project by the work done in Phase 1. We look forward to implementing the server side of the project in the next iteration of our work!
