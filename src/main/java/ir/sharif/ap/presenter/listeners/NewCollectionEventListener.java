package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.model.CollectionListType;

public interface NewCollectionEventListener {
    public void newCollectionEventOccurred(CollectionListType collectionListType, String name);
}
