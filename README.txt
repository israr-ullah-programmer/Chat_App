Author:
Israr Ullah
Imran Ullah
Muhammad Usman


Java Socket Chat Application
Overview
This project implements a simple multi-client chat application in Java using TCP sockets. It consists of two main components:

ChatServer.java: A server program that listens for client connections, receives messages, and broadcasts them to all connected clients.

ChatClientGUI.java: A graphical client application that connects to the server, allows the user to send messages, and displays messages from other clients.

Features
Multiple clients can connect simultaneously to the server.

Real-time message broadcasting to all connected clients.

Simple and user-friendly GUI client interface built with Java Swing.

Graceful handling of client disconnections.

Console output on the server side for connection and message tracking.

Requirements
Java Development Kit (JDK) 8 or higher.

Network access to allow client-server communication (default port 12345).

How to Run
1. Compile the source files
Open a terminal or command prompt in the project directory and run:

bash
javac ChatServer.java ChatClientGUI.java
2. Start the server
Run the server program:

bash
java ChatServer
The server will start listening on port 12345 and wait for client connections.

3. Start the client(s)
Run the client program, specifying the server IP address and port if needed (default is localhost and 12345):

bash
java ChatClientGUI
The client GUI window will open and automatically connect to the server at 127.0.0.1:12345.

4. Use the chat
Type messages in the input field and click "Send" or press Enter to send.

Messages from other clients will appear in the chat area.

Click "Disconnect" to leave the chat.

Code Structure
ChatServer.java
Listens on port 12345 for client connections.

Uses a synchronized set to manage multiple ClientHandler threads.

Each ClientHandler handles communication with one client.

Broadcasts received messages to all other clients.

ChatClientGUI.java
Java Swing GUI with a text area, input field, and buttons.

Connects to the server via a socket.

Sends messages typed by the user.

Runs a listener thread to receive and display messages from the server.

Handles clean disconnection and GUI updates.

Customization
Change the server port by modifying the PORT constant in ChatServer.java.

Change the server IP and port in the client by editing the parameters in ChatClientGUI main method.

Limitations and Future Improvements
No user authentication or usernames.

Messages are plain text without formatting.

No private messaging or message history.

Could be extended with encryption, file transfer, or richer GUI features.

Author
Israr Ullah
