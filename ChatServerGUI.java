import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServerGUI extends JFrame {
    private JTextArea serverLogArea;
    private JTextField serverInputField;
    private JButton serverSendButton;
    private static Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());

    public ChatServerGUI() {
        setTitle("Server Console");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        serverLogArea = new JTextArea();
        serverLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(serverLogArea);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        serverInputField = new JTextField();
        serverSendButton = new JButton("Send to Clients");
        
        bottomPanel.add(serverInputField, BorderLayout.CENTER);
        bottomPanel.add(serverSendButton, BorderLayout.EAST);
        
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        serverSendButton.addActionListener(e -> sendToClients());
        serverInputField.addActionListener(e -> sendToClients());
        
        new Thread(() -> startServer(12345)).start();
    }

    private void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverLogArea.append("Server started on port " + port + "\n");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serverLogArea.append("New client connected: " + clientSocket.getInetAddress() + "\n");
                
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);
                
                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))) {
                        
                        String clientMessage;
                        while ((clientMessage = in.readLine()) != null) {
                            serverLogArea.append("Client: " + clientMessage + "\n");
                        }
                    } catch (IOException e) {
                        serverLogArea.append("Client disconnected\n");
                    } finally {
                        clientWriters.remove(out);
                    }
                }).start();
            }
        } catch (IOException e) {
            serverLogArea.append("Server error: " + e.getMessage() + "\n");
        }
    }

    private void sendToClients() {
        String message = serverInputField.getText().trim();
        if (!message.isEmpty()) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println("Server: " + message);
                }
            }
            serverInputField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatServerGUI().setVisible(true));
    }
}