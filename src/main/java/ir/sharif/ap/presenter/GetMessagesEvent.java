package ir.sharif.ap.presenter;

import ir.sharif.ap.model.TweetListType;

import java.time.LocalDateTime;

public class GetMessagesEvent {
    private int maxNum;
    private LocalDateTime lastMessageDateTime;
    private long chatID;

    public GetMessagesEvent(int maxNum, LocalDateTime lastMessageDateTime, long chatID) {
        this.maxNum = maxNum;
        this.lastMessageDateTime = lastMessageDateTime;
        this.chatID = chatID;
    }

    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public LocalDateTime getLastMessageDateTime() {
        return lastMessageDateTime;
    }

    public void setLastMessageDateTime(LocalDateTime lastMessageDateTime) {
        this.lastMessageDateTime = lastMessageDateTime;
    }
}
