package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 28-10-2017.
 */

public class DocumentTypeModel
{
    public String docTypeName;
    public String docTypeId;

    public DocumentTypeModel(String docTypeName, String docTypeId) {
        this.docTypeName = docTypeName;
        this.docTypeId = docTypeId;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    @Override
    public String toString() {
        return getDocTypeName();
    }
}
