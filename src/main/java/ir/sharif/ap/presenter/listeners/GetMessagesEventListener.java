package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.presenter.events.GetMessagesEvent;

public interface GetMessagesEventListener {
    public void getMessagesEventOccurred(GetMessagesEvent e);
}
