package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.model.CollectionListType;
import ir.sharif.ap.presenter.NewCollectionEvent;

public interface NewCollectionEventListener {
    public void newCollectionEventOccurred(NewCollectionEvent e);
}
