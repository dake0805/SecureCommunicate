package com.zoe.client.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Message {
    String account;
    String message;

    public Message(String account, String message) {
        this.account = account;
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
        Date date = new Date();
        return "\nMessage@" + sdf.format(date) + "\n" +
                account + ": \n" +
                message + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return message1.account.equals(this.account) &&
                message1.message.equals(this.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, message);
    }
}
