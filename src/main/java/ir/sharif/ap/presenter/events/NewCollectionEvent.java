package ir.sharif.ap.presenter.events;

import ir.sharif.ap.model.CollectionListType;

public class NewCollectionEvent {
    private CollectionListType collectionListType;
    private String name;

    public NewCollectionEvent(CollectionListType collectionListType, String name) {
        this.collectionListType = collectionListType;
        this.name = name;
    }

    public CollectionListType getCollectionListType() {
        return collectionListType;
    }

    public void setCollectionListType(CollectionListType collectionListType) {
        this.collectionListType = collectionListType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
