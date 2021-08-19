package ir.sharif.ap.model;

public class NotificationTile {
    private byte[] userImage;
    private String userFullName;
    private String username;
    private NotificationListType notificationListType;
    private String statusText;

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public NotificationListType getNotificationListType() {
        return notificationListType;
    }

    public void setNotificationListType(NotificationListType notificationListType) {
        this.notificationListType = notificationListType;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
