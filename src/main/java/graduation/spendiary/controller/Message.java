package graduation.spendiary.controller;

import lombok.Data;

@Data
public class Message {
    String msg;
    boolean success;

    public Message(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }
}
