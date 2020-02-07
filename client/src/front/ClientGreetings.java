package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.net.Socket;

public class ClientGreetings extends JFrame {

    private final String HOST = "localhost";
    private final int PORT = 7070;

    private JTextField usernameField;
    private JButton jButton;

    public void start() {

        setBounds(600, 300, 300, 75);
        setTitle("Greetings");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        jButton = new JButton("Enter in chat");
        bottomPanel.add(jButton, BorderLayout.EAST);

        usernameField = new JTextField("Pick your username: ");
        bottomPanel.add(usernameField, BorderLayout.CENTER);

        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                usernameField.setText("");
            }
        });
        setVisible(true);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!usernameField.getText().trim().isEmpty()) {
                    setVisible(false);
                    saveUser();
                    new ClientWindow().display(usernameField.getText());

                    usernameField.grabFocus();
                }
            }
        });
    }

    public void saveUser() {
        try {
            Socket socket = new Socket(HOST, PORT);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write(usernameField.getText() + "\n");
            writer.flush();
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
