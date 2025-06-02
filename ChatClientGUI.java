import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends JFrame {
    private JTextArea clientChatArea;
    private JTextField clientInputField;
    private JButton clientSendButton;
    private PrintWriter out;

    public ChatClientGUI(String serverIP, int serverPort) {
        setTitle("Client Chat");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        clientChatArea = new JTextArea();
        clientChatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(clientChatArea);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        clientInputField = new JTextField();
        clientSendButton = new JButton("Send");
        
        bottomPanel.add(clientInputField, BorderLayout.CENTER);
        bottomPanel.add(clientSendButton, BorderLayout.EAST);
        
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        clientSendButton.addActionListener(e -> sendMessage());
        clientInputField.addActionListener(e -> sendMessage());
        
        connectToServer(serverIP, serverPort);
    }

    private void connectToServer(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            
            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))) {
                    
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        clientChatArea.append(serverMessage + "\n");
                    }
                } catch (IOException e) {
                    clientChatArea.append("Disconnected from server\n");
                }
            }).start();
            
            clientChatArea.append("Connected to server at " + ip + ":" + port + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private void sendMessage() {
        String message = clientInputField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            clientInputField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> 
            new ChatClientGUI("localhost", 12345).setVisible(true));
    }
}