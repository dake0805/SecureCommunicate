package com.zoe.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JTextArea recordOfMsgText;
    private JTextArea sendMsgText;
    private JButton send;

    public MainWindow() {
        this.setTitle("QiChatV04");
        //最重要的！名字
        this.setLayout(new FlowLayout());
        this.setSize(600, 550);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        recordOfMsgText = new JTextArea(20, 50);
        //文本域 存放历史消息
        recordOfMsgText.setLineWrap(true);
        recordOfMsgText.setEditable(false);
        //改为不可编辑的

        sendMsgText = new JTextArea(3, 30);
        //消息发送框
        sendMsgText.setLineWrap(true);

        send = new JButton("send");
        //发送按钮
        send.setPreferredSize(new Dimension(100, 50));
        this.add(recordOfMsgText);
        this.add(sendMsgText);
        this.add(send);
        this.setVisible(true);

        send.addActionListener(new ButtonAction());
    }

    public JTextArea getSendMsgText() {
        return sendMsgText;
    }

    public JButton getSend() {
        return send;
    }

    public JTextArea getRecordOfMsgText() {
        return recordOfMsgText;
    }
}
