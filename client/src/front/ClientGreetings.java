package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ClientGreetings {

    private JTextField usernameChooser;
    private JFrame frame;

    public void open() {
        frame = new JFrame("ChatSocket");
        usernameChooser = new JTextField(10);
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new EnterServerButtonListener());
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        panel.add(chooseUsernameLabel, preLeft);
        panel.add(usernameChooser, preRight);
        frame.add(BorderLayout.CENTER, panel);
        frame.add(BorderLayout.SOUTH, enterServer);
        frame.setBounds(600, 300, 400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public void displayTrayMessage(String message) {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage("Notification!", message, TrayIcon.MessageType.INFO);
    }

    class EnterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String username = usernameChooser.getText();
            if (username.length() < 3) {
                displayTrayMessage("Length of username must be min 3 symbols");
            } else {
                frame.setVisible(false);
                ClientMainWindow clientGreetings = new ClientMainWindow();
                clientGreetings.display(username);
                try {
                    Socket socket = new Socket("localhost", 7070);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(username + "\n");
                    writer.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println(reader.readLine());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}