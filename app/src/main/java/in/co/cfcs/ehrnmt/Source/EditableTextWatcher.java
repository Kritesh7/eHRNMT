package in.co.cfcs.ehrnmt.Source;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Admin on 24-10-2017.
 */

public abstract class EditableTextWatcher implements TextWatcher {

    private boolean editing;

    @Override
    public final void beforeTextChanged(CharSequence s, int start,
                                        int count, int after) {
        if (editing)
            return;

        editing = true;
        try {
            beforeTextChange(s, start, count, after);
        } finally {
            editing = false;
        }
    }

    protected abstract void beforeTextChange(CharSequence s, int start,
                                             int count, int after);

    @Override
    public final void onTextChanged(CharSequence s, int start,
                                    int before, int count) {
        if (editing)
            return;

        editing = true;
        try {
            onTextChange(s, start, before, count);
        } finally {
            editing = false;
        }
    }

    protected abstract void onTextChange(CharSequence s, int start,
                                         int before, int count);

    @Override
    public final void afterTextChanged(Editable s) {
        if (editing)
            return;

        editing = true;
        try {
            afterTextChange(s);
        } finally {
            editing = false;
        }
    }

    public boolean isEditing() {
        return editing;
    }

    protected abstract void afterTextChange(Editable s);
}