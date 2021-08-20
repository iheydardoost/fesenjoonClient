package ir.sharif.ap.model;

public class CollectionItem {
    private String collectionName;
    private String userName;
    private Long collectionID;
    private Boolean isSelected;
    private CollectionItemType collectionItemType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public CollectionItemType getCollectionItemType() {
        return collectionItemType;
    }

    public void setCollectionItemType(CollectionItemType collectionItemType) {
        this.collectionItemType = collectionItemType;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Long getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(Long collectionID) {
        this.collectionID = collectionID;
    }
}
