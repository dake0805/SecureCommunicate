package com.zoe.gui;

import javax.swing.*;

/**
 * @author zy
 */
public class GuiStart {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8080;
        String account = "user";

        JTextField serverAddressTextBox = new JTextField(9);
        JTextField serverPortTextBox = new JTextField(5);
        JTextField accountTextBox = new JTextField(9);

        JPanel loginAskPanel = new JPanel();
        loginAskPanel.add(new JLabel("server address: "));
        loginAskPanel.add(serverAddressTextBox);
        loginAskPanel.add(Box.createHorizontalStrut(15));
        loginAskPanel.add(new JLabel("server port: "));
        loginAskPanel.add(serverPortTextBox);
        loginAskPanel.add(Box.createHorizontalStrut(15));
        loginAskPanel.add(new JLabel("your nickname"));
        loginAskPanel.add(accountTextBox);

        int result = JOptionPane.showConfirmDialog(null, loginAskPanel,
                "Need to login now.", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                serverAddress = serverAddressTextBox.getText();
                serverPort = Integer.parseInt(serverPortTextBox.getText());
                account = accountTextBox.getText();
            } catch (NumberFormatException e) {
                //
            }

        }
        JFrame frame = new MainWindow(serverAddress, serverPort, account);
    }

}