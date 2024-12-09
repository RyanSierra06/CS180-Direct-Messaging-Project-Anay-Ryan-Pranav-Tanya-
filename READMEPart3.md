README Phase 3:

Our direct messaging application is named Skyline Chat. When opening the app, the user is prompted with 3 choices upfront. These choices include creating an account, logging in, or exiting. Creating an account involves answering two prompts, a username and a password. After filling in this information, hitting the OK button takes a user back to the main application. Logging in with an existing account involves inputting in a username and password for a previously created account. Pressing the exit button closes the entire application.

On the main application, near the top, there is the opportunity to set your name, set a profile description, and set a profile picture.

There are also 3 sections on main: friends, blocked users, and send message
Friends - view friends, add friends, remove friends, change receiving options (from friends only to everyone)
Blocked users - view blocked users, block users, unblock user
Send message - message user, view all users
For you to send a message, you must ensure that the receiver hasn’t blocked you and that you are on his friend list (if in case he has a setting of receiving messages from friends only). You also need to add that person to your friend list. Once that is done, you can enter their DM, and a new window pops up. You can then text that person. Each text can be of one of two types: Text or Image (extra credit). If it is a text you can directly hit send. Otherwise, the message should be the file path of the image you want to send over. After you’ve ensured that the file path is correct, you may send that image over.

In order to run the application with multiple clients you must first start up the server in the terminal (from the main project directory, not the src folder). Run the following:
javac src/GraphicalServerRunner.java followed by java src/GraphicalServerRunner.java
Next for each client, you want to run the following commands on a separate instance of the terminal (from the main project directory, not the src folder): 
javac src/GraphicalClient.java followed by java src/GraphicalClient.java
