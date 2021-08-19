package ir.sharif.ap.model;

public enum NotificationListType {
    MY_PENDING_LIST,
    NOTIFICATION_LIST,
    SYSTEM_NOTIFICATIONS;

    @Override
    public String toString() {
        String name = "";
        switch (ordinal()) {
            case 0:
                name = "My Pending List";
                break;
            case 1:
                name = "Notification List";
                break;
            case 2:
                name = "System notifications";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }
}
