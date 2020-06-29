package com.zoe.gui;

import com.zoe.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JFrame {
    private JTextArea userInfoMsgText;
    private JTextArea recordOfMsgText;
    private JTextArea sendMsgText;
    private JButton sendButton;

    private String serverAddress;
    private int port;
    private String account;

    private final Client client;

    public MainWindow(String serverAddress, int port, String account) {
        client = new Client();
        if (client.startWithGui(serverAddress, port, account)) {
            this.serverAddress = serverAddress;
            this.port = port;
            this.account = account;
            this.setTitle("chat room@" + serverAddress);
            this.setLayout(new BorderLayout());
            this.setBounds(100, 100, 450, 300);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            welcomeTextBox();
            sendButton();
            recordMessageBox();
            inputMessageBox();

            this.add(userInfoMsgText, "North");
            this.add(recordOfMsgText, "Center");
            this.add(sendButton, "East");
            this.add(sendMsgText, "South");
            this.setVisible(true);
            new Receive().start();
        } else {
            //alert
        }
    }

    private void welcomeTextBox() {
        userInfoMsgText = new JTextArea();
        userInfoMsgText.setLineWrap(true);
        userInfoMsgText.setEditable(false);
        userInfoMsgText.setText("welcome to chat room@" + serverAddress + ":" + port + ", your username is " + account);

    }

    private void recordMessageBox() {
        recordOfMsgText = new JTextArea();
        recordOfMsgText.setLineWrap(true);
        recordOfMsgText.setEditable(false);
    }

    private void inputMessageBox() {
        sendMsgText = new JTextArea();
        sendMsgText.setLineWrap(true);
        sendMsgText.setEditable(true);
    }


    private void sendButton() {
        sendButton = new JButton("send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = sendMsgText.getText();
                if (message.length() > 0) {
                    client.sendMessageWithGui(message);
                    sendMsgText.setText("");
                }
            }
        });
    }

    private class Receive extends Thread {
        @Override
        public void run() {
            while (true) {
                try {

                    Thread.sleep(1);
                    var s = client.receiveMessageWithGui();
                    recordOfMsgText.append(s.toString());
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
