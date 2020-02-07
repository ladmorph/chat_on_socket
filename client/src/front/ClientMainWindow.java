package front;

import socket.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientMainWindow {

    private JFrame frame = new JFrame("Chat Socket");
    private JTextField messageBox;
    private JTextArea chatBox;

    private String username;

    public void display(String username) {

        this.username = username;
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new SendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        panel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessageButton, right);

        panel.add(BorderLayout.SOUTH, southPanel);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(470, 300);
        frame.setVisible(true);
    }

    class SendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            if (messageBox.getText().equals("/clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                ClientSocket clientSocket = null;
                try {

                    clientSocket = new ClientSocket();
                    clientSocket.sendMessage(username + ": " + messageBox.getText());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                chatBox.append(username + ": " + messageBox.getText()
                        + "\n");
                messageBox.setText("");

                clientSocket.readMessage();
            }
            messageBox.requestFocusInWindow();
        }
    }


}
