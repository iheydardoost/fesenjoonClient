package ir.sharif.ap.model;

import java.time.LocalDateTime;

public class Message {
    private String msgText;
    private LocalDateTime msgDateTime;
    private long chatID;
    private long msgID;
    private boolean forwarded;
    private MessageStatus msgStatus;
    private byte[] msgImage;
    private String userFullName;
    private boolean isOwner;


    public Message() {
    }

    public Message(String msgText, LocalDateTime msgDateTime,
                   long chatID, long msgID,
                   boolean forwarded, MessageStatus msgStatus, byte[] msgImage,String userFullName,boolean isOwner) {
        this.msgText = msgText;
        this.msgDateTime = msgDateTime;
        this.chatID = chatID;
        this.msgID = msgID;
        this.forwarded = forwarded;
        this.msgStatus = msgStatus;
        this.msgImage = msgImage;
        this.userFullName = userFullName;
        this.isOwner = isOwner;
    }

    public static Message copy(Message message){
        Message copiedMsg = new Message(
                message.getMsgText(),
                LocalDateTime.of(message.getMsgDateTime().toLocalDate(),message.getMsgDateTime().toLocalTime()),
                message.getChatID(),
                message.getMsgID(),
                message.isForwarded(),
                message.getMsgStatus(),
                message.getMsgImage(),
                message.getUserFullName(),
                message.isOwner()
        );
        return copiedMsg;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public LocalDateTime getMsgDateTime() {
        return msgDateTime;
    }

    public void setMsgDateTime(LocalDateTime msgDateTime) {
        this.msgDateTime = msgDateTime;
    }

    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }

    public MessageStatus getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(MessageStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    public byte[] getMsgImage() {
        return msgImage;
    }

    public void setMsgImage(byte[] msgImage) {
        this.msgImage = msgImage;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
