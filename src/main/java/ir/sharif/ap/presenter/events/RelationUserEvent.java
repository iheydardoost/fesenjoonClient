package ir.sharif.ap.presenter.events;

import ir.sharif.ap.model.RelationType;

public class RelationUserEvent {
    private RelationType relationType;
    private long objectID;

    public RelationUserEvent(RelationType relationType, long objectID) {
        this.relationType = relationType;
        this.objectID = objectID;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public long getObjectID() {
        return objectID;
    }

    public void setObjectID(long objectID) {
        this.objectID = objectID;
    }
}
