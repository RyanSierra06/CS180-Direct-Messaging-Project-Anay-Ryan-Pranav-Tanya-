# CS180-Direct-Messaging-Project-Anay-Ryan-Pranav-Tanya-

## How to Run?
Use the UserTestCases and MessageTestCases files to run and test both classes. If you want to run the tests multiple times make sure to delete all the files within the files folder and make usernamesAndPasswords.txt empty. We need to do this since our program appends to some files and rewrites others and the test cases don't reset the workspace. There are multiple test cases that ensure our files are saved between programs properly.


## Who did what?
- Submitted on Vocareum - Pranav
- GraphicalServer.java - Pranav & Ryan & Tanya
- GraphicalServerRunner.java - Pranav & Ryan
- GraphicalServerInterface.java - Tanya
- GraphicalClient.java - Pranav & Ryan & Anay
- GraphicalClientnterface.java - Anay
- ReadMessageThreadGraphical.java - Ryan
- GraphicalClientReader.java - Pranav
- READMEPart3.md - Ryan


## Class Descriptions
GraphicalServer.java:
- This class creates the server to run multiple clients simultaneously by making a thread each time you handle a client.
The way the server works is by taking in commands passed by the client each time the user presses a button on the screen or sends a message to a user. These commands are then used to change an attribute about the user, or, if the user is messaging someone else, send or delete a message, or exit the messaging service.
From there, those commands are processed completely on the server side so the client is never handling anything related to database access or modification.
Because we make a new thread every time a client is created, we allow the server to house multiple clients that can read and write to files based on the user input for what they want to have happen in the DM software.


GraphicalServerRunner.java:
- This class is used to run the server on our localhost, and it's the basis for the multi-threading of the client classes within the main server.

GraphicalServerInterface.java:
- This houses all the methods we used for the GraphicalServer.java class

GraphicalClient.java:
- This is the client that is the basis for the GUI implementation and takes in user input each time a user wants to log in, create a new account, and use the software.
If the user tries to log in and doesn't have an account, we automatically make the account for them, that way they don't have to log in again (we also have checks in place to make sure that only 1 person can have any given username).
Once the user enters a valid username and password, they're taken to the home page which displays their profile attributes and allows them to change them as well as add friends, block users, view other user's profiles, and start sending messages to other users.
The set name button allows the user to set their name, which takes the command from the button input and text input, passes it to the server, and then has the server edit the files, and let the user know back in the terminal that it was a successful or unsuccessful change.
The set profile description button allows the user to set their profile description, doing this same system of sending 3 layers down and pulling 3 layers back up. (This is the same process for every different button).
The set profile picture button allows for setting your profile picture, and those 3 attributes of the name, profile description, and profile picture are all there for you to view on the home page (and if you haven't set anything in your profile, it just sets a blank string for all of that information and makes the main icon the Skyline Chat icon).
The view all users button allows you to view another user's profile information but creating a drop-down and showing all the names of all the different users in the software. This same service is built into the view friends and view blocked users buttons as well.
The send message button is where the direct messaging comes in. The user is first given the entire message history of the other specified user (if this is the first time messaging the chat is empty) and can then start sending messages, delete a message or image, or exit the chat entirely.
As the other person sends more messages, and as more messages are being sent to the given user, it updates in the chat frame in real time.
Messaging also checks to see if the user you're messaging can receive messages from anyone and if not, if the users are friends with each other before they can start messaging. 
The block user button allows the user to block another user, and the unblock user button is there to unblock a user, still checking to make sure the user exists.
The view blocked users button shows the user all of their currently blocked users.
The add friend button Allows the user to enter a friend to add, and add that friend as long as it's a valid user.
The remove friend button allows the user to remove a friend, still checking to make sure it's a valid user, and the view friends button returns your entire friend list, allowing you to see their profile in the process.
The change receiving options button allows you to change if you can receive messages from anyone, or just messages from people who you have added as friends.
You can exit at any time by using the close frame button in the top left, and everything you've done so far will be saved for when you log back in.

GraphicalClientInterface.java:
- This houses all the methods we used for the ApplicationClient.java class


ReadMessageThreadGraphical.java:
- This is what is used to read the messages from the file in the server and display them back to both users as concurrent messages happen.
Basically, as 2 people are trying to message each other at the same time, it allows for concurrent messaging to be displayed for both users, allowing the users to see each other's messages right as they're coming in (including images).
This threaded class allows for the same file to be updated concurrently by the server, and continuously checks to find the new messages as they're being added, printing them back out to both users.

GraphicalClientReader.java:
- This is a thread that is created by the client, for reading and displaying messages sent from the server's ReadMessageThreadGraphical(RMTG) thread. Whenever a user has a chat open it continuously looks for input from the RMTG thread. The input it receives in a string of HTML commands that display the entire chat history between the 2 users. Upon receiving the input it modifies the text within the JTextPane, sets its content type to HTML text, and then repaints the pane so the user can see the updated messages.


## How to Run 
Start by running the GraphicalServerRunner.java in your first terminal. Then, in your other terminals, run as many GraphicalClient.java clients as you want. Within each of those clients, you can start DMing whoever you want, and editing each of your individual user profiles, friends, and blocked users. See the other java file explanations above for more specificity on what each of the commands can do, and how to fully use the client.




