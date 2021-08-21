package ir.sharif.ap.presenter.events;

import ir.sharif.ap.model.CollectionItemType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NewMessageEvent {
    private String msgText;
    private byte[] msgImage;
    private LocalDateTime msgDateTime;
    private boolean forwarded;
    private ArrayList<CollectionItemType> collectionItemTypes;
    private ArrayList<Long> IDs;

    public NewMessageEvent() {
        this.msgText = null;
        this.msgImage = null;
        this.msgDateTime = null;
        this.forwarded = false;
        this.collectionItemTypes = new ArrayList<>();
        this.IDs = new ArrayList<>();
    }

    public ArrayList<CollectionItemType> getCollectionItemTypes() {
        return collectionItemTypes;
    }

    public ArrayList<Long> getIDs() {
        return IDs;
    }

    public String getMsgText() {
        return msgText;
    }

    public byte[] getMsgImage() {
        return msgImage;
    }

    public LocalDateTime getMsgDateTime() {
        return msgDateTime;
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public NewMessageEvent setMsgText(String msgText) {
        this.msgText = msgText;
        return this;
    }

    public NewMessageEvent setMsgImage(byte[] msgImage) {
        this.msgImage = msgImage;
        return this;
    }

    public NewMessageEvent setMsgDateTime(LocalDateTime msgDateTime) {
        this.msgDateTime = msgDateTime;
        return this;
    }

    public NewMessageEvent setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
        return this;
    }

    public NewMessageEvent setCollectionItemTypes(ArrayList<CollectionItemType> collectionItemTypes) {
        this.collectionItemTypes = collectionItemTypes;
        return this;
    }

    public NewMessageEvent setIDs(ArrayList<Long> IDs) {
        this.IDs = IDs;
        return this;
    }

    public NewMessageEvent addCollectionItemType(CollectionItemType collectionItemType) {
        this.collectionItemTypes.add(collectionItemType);
        return this;
    }

    public NewMessageEvent addID(long id) {
        this.IDs.add(id);
        return this;
    }
}
