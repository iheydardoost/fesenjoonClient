package ir.sharif.ap.presenter;

public class EditMessageEvent {
    private long msgID;
    private String msgText;

    public EditMessageEvent(long msgID, String msgText) {
        this.msgID = msgID;
        this.msgText = msgText;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
}
