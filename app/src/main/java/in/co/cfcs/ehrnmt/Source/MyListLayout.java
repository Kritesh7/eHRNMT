package in.co.cfcs.ehrnmt.Source;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Admin on 12-10-2017.
 */

public class MyListLayout  extends ListView {

    boolean expanded = true;

    public MyListLayout(Context context, AttributeSet attrs,
                        int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public MyListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            android.view.ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}