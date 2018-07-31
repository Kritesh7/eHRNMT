package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 21-09-2017.
 */

public class LanguageModel
{
    public String langaugae;
    public String read;
    public String write;
    public String speak;
    public String RecordID;

    public LanguageModel(String langaugae, String read, String write, String speak, String RecordID) {
        this.langaugae = langaugae;
        this.read = read;
        this.write = write;
        this.speak = speak;
        this.RecordID = RecordID;
    }

    public String getRecordID() {
        return RecordID;
    }

    public String getLangaugae() {
        return langaugae;
    }

    public String getRead() {
        return read;
    }

    public String getWrite() {
        return write;
    }

    public String getSpeak() {
        return speak;
    }
}
