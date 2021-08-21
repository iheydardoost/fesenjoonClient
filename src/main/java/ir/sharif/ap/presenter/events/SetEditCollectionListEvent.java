package ir.sharif.ap.presenter.events;

import ir.sharif.ap.model.CollectionListType;

import java.util.ArrayList;

public class SetEditCollectionListEvent {
    private ArrayList<String> userNames;
    private long collectionID;
    private CollectionListType collectionListType;

    public SetEditCollectionListEvent() {
        this.userNames = new ArrayList<>();
        this.collectionID = 0;
        this.collectionListType = null;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public SetEditCollectionListEvent addUserName(String userName) {
        this.userNames.add(userName);
        return this;
    }

    public long getCollectionID() {
        return collectionID;
    }

    public SetEditCollectionListEvent setCollectionID(long collectionID) {
        this.collectionID = collectionID;
        return this;
    }

    public CollectionListType getCollectionListType() {
        return collectionListType;
    }

    public void setCollectionListType(CollectionListType collectionListType) {
        this.collectionListType = collectionListType;
    }
}
