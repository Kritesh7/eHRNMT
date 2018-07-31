package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 26-10-2017.
 */

public class RelationShipeTypeModel
{
    public String relationshipName;
    public String relationshipId;

    public RelationShipeTypeModel(String relationshipName, String relationshipId) {
        this.relationshipName = relationshipName;
        this.relationshipId = relationshipId;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public String getRelationshipId() {
        return relationshipId;
    }

    @Override
    public String toString() {
        return getRelationshipName();
    }
}
