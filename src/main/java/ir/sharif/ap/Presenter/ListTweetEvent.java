package ir.sharif.ap.Presenter;

import java.time.LocalDateTime;

public class ListTweetEvent {
    private boolean isTimeline;
    private int maxNum;
    private LocalDateTime lastTweetDateTime;

    public ListTweetEvent(boolean isTimeline, int maxNum, LocalDateTime lastTweetDateTime) {
        this.isTimeline = isTimeline;
        this.maxNum = maxNum;
        this.lastTweetDateTime = lastTweetDateTime;
    }

    public boolean isTimeline() {
        return isTimeline;
    }

    public void setTimeline(boolean timeline) {
        isTimeline = timeline;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public LocalDateTime getLastTweetDateTime() {
        return lastTweetDateTime;
    }

    public void setLastTweetDateTime(LocalDateTime lastTweetDateTime) {
        this.lastTweetDateTime = lastTweetDateTime;
    }
}
