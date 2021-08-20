package ir.sharif.ap.presenter;

import ir.sharif.ap.model.CollectionListType;

public class GetEditCollectionListEvent {
    private CollectionListType collectionListType;
    private long id;

    public GetEditCollectionListEvent(CollectionListType collectionListType, long id) {
        this.collectionListType = collectionListType;
        this.id = id;
    }

    public CollectionListType getCollectionListType() {
        return collectionListType;
    }

    public void setCollectionListType(CollectionListType collectionListType) {
        this.collectionListType = collectionListType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
