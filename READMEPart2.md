# CS180-Direct-Messaging-Project-Anay-Ryan-Pranav-Tanya-

## How to Run?
Use the UserTestCases and MessageTestCases files to run and test both classes. If you want to run the tests multiple times make sure to delete all the files within the files folder and make usernamesAndPasswords.txt empty. We need to do this since our program appends to some files and rewrites others and the test cases don't reset the workspace. There are multiple testcases that ensure our files are saved between programs properly.


## Who did what?
- Submitted on Vocareum - Pranav
- ApplicationServer.java - Pranav & Ryan & Tanya
- ApplicationServerRunner.java - Pranav & Ryan
- ApplicationServerInterface.java - Tanya
- ApplicationClient.java - Pranav & Ryan & Anay
- ApplicationClientInterface.java - Anay
- ReadMessageThread.java - Pranav & Ryan
- READMEPart2.md - Ryan


## Class Descriptions
ApplicationServer.java:
- This class creates the sever to run multiple clients simultaneously by making a thread each time you handle a client.
The way the server works is by taking in commands passed by the client each time the user prompts something in the terminal.
From there, those commands are processed completely on the server side so the client is never handleing anything related to database access or modification.
Because each we make a new thread everytime a client is created, we allow the server to house multiple clients which can read and write to files based on the user input for what they want to have happen in the DM software.


ApplicationServerRunner.java:
- This class is used to actaully run the server on our localhost, and it's the basis for the multi threading of the client classes within the main server.

ApplicationServerInterface.java:
- This houses all the methods we used for the ApplicationServer.java class

ApplicationClient.java:
- This is the client which is used to take user input each time a user wants to log-in or create a new account.
If the user tries to log-in and doesnt have an account, we automatically make the account for them, that way they dont have to re log-in (we also have checks in place to make sure that only 1 person can have any given username).
Once the user enters a valid username and password, they're then given 13 differnt options for how they want to proceed with using the software.
The first one allows them to set their name, which take the command from the terminal, passes it to the server, and then has the server edit the files, and let the user know back in the terminal that i was a successful or unsuccessful change.
The second allows you to set your profile description, doing this same system of sending 3 layers down, and pulling 3 layers back up. (This is the same process for every differnt selection).
The third allows for setting your profile picture, and the fourth allows you to view your entire profile (and if you havent set anything in your profile, it just returns a blank string for all of that information).
The fifth allows you to view another users profile information, as long as that user exists. If that user doesnt exist, it tells you so, and asks the user for their next actions.
The sixth is where the direct messaging comes in. The user is first given the entire message history the other specified user, and is is then prompted to start sending more messages. It starts by asking the user to give the type of message they're sending (Image or Text), as well as the message itself they're trying to send.
Each time you boot up a new dm between 2 users, the entire message history is displayed so you know where your conversation left off. 
As the other person sends more messages, and as more messages are being sent to the given user, it updates in the terminal in real time. It's a little ugly right now, and messages will come into the terminal right as theyre received, but thats beacause we can only have one terminal open instead of having multiple frames to display the messages in different areas. This is an easy fix for the GUI though, and everything else runs perfeclty fine.
Messaging also checks to see if the user you're messaging can receive messages from anyone, and if not, if the users are friends with each other before they can start messaging. 
The seventh allows the user to block another user, as long as that user exists, and the eight is there to unblock a user, still checking to make sure the user exists.
The ninth Shows the user all of their currently blocked users.
The tenth Allows the user to enter a friend to add, and add that friend as long as its a valid user.
The eleventh alllows the user to remove a friend, still checking to make sure its a valid user, and the twelfth returns your entire friend list.
The thirteenth allows you to change if you can receive messages from anyone, or just messages from people who you have added as friends.
Finally, the foruteenth allows you to exit the program and log you out, still keep the server running though.

ApplicationClientInterface.java:
- This houses all the methods we used for the ApplicationClient.java class


ReadMessageThread.java:
- This is whats used to read the messages from the file in the server, and display them back to both users as concurrent messages happen.
Basically, as 2 people are trying to message eachother at the same time, it allows for the concurrent messaging to be displayed for both users, allowing the users to see eachothers messages right as they're coming in.
This threaded class allows for the same file to be updated concurrently by the server, and continuously check to find the new messages as they're being added, printing them back out to both users.


## How to Run 
Start by running the ApplicationServerRunner.java in your first terminal. Then, in your other terminals, run as many ApplicationClient.java clients as you want. Within each of those clients, you can start DMing whoever you want, and editing each of your individual user profiles, friends, and blocked users. See the other java files explinations above for more specificity on what each of the commands can do, and how to fully use the client.




