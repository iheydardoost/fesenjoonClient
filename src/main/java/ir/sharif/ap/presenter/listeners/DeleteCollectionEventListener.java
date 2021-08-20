package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.model.CollectionListType;

public interface DeleteCollectionEventListener {
    public void deleteCollectionEventOccurred(CollectionListType collectionListType,long id);
}
