package ir.sharif.ap.model;

public class RelationTile {
    private byte[] userImage;
    private String userFullName;
    private String username;
    private RelationListType relationListType;
    private long userID;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public RelationListType getRelationType() {
        return relationListType;
    }

    public void setRelationType(RelationListType relationType) {
        this.relationListType = relationType;
    }

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
}
