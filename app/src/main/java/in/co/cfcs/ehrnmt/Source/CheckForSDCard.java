package in.co.cfcs.ehrnmt.Source;

import android.os.Environment;

/**
 * Created by Admin on 31-10-2017.
 */

public class CheckForSDCard
{
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
