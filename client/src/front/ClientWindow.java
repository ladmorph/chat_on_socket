package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class ClientWindow extends JFrame {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private Socket clientSocket;

    private BufferedReader reader;
    private BufferedWriter writer;

    private JTextField jtfMessage;
    private JTextArea jtaTextAreaMessage;


    private String username = "";

    public ClientWindow() {
        try {
            clientSocket = new Socket(HOST, PORT);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        String messageStr = username + ": " + jtfMessage.getText();
        try {
            writer.write(messageStr + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jtfMessage.setText("");
    }

    public void display(String username) {
        this.username = username;

        setBounds(600, 300, 400, 300);
        setTitle("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        JButton jbSendMessage = new JButton("Send!");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);

        jtfMessage = new JTextField("Write your message: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);


        jbSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jtfMessage.getText().trim().isEmpty()) {
                    sendMessage();
                    jtfMessage.grabFocus();
                }
            }
        });

        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (reader.ready()) {
                            String message = reader.readLine();
                            jtaTextAreaMessage.append(message);
                            jtaTextAreaMessage.append("\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    writer.close();
                    reader.close();
                    clientSocket.close();
                } catch (IOException exc) {

                }
            }
        });

        setVisible(true);
    }
}
