package ir.sharif.ap.model;

public class ChatItem {
    private String chatName;
    private int unReadMessages;
    private long chatID;

    public ChatItem() {
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getUnReadMessages() {
        return unReadMessages;
    }

    public void setUnReadMessages(int unReadMessages) {
        this.unReadMessages = unReadMessages;
    }

    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }
}
