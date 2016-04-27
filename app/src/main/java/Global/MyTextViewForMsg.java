package Global;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vivacious on 4/20/2016.
 */
public class MyTextViewForMsg extends TextView {

    public MyTextViewForMsg(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewForMsg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewForMsg(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/FengardoNeue_Regular.otf" +"");
        setTypeface(tf);
    }

}