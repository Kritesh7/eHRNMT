package in.co.cfcs.ehrnmt.Source;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Model.BookMeaPrevisionModel;


/**
 * Created by Admin on 06-11-2017.
 */

public class EditTextWatcher implements TextWatcher {

    private int mPosition;
    private boolean mActive;

    public void setPosition(int position){
        mPosition = position;
    }
    public void setActive(boolean active){
        mActive = active;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(mActive){
            //rNotes.set(mPosition,s.toString());
            ArrayList<BookMeaPrevisionModel> listData = new ArrayList<>();
            BookMeaPrevisionModel re = listData.get(mPosition);
            re.setFillQuanty(s.toString());
            listData.set(mPosition,re);
        }
    }
}
