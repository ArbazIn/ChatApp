package Global;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vivacious on 4/20/2016.
 */
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Queen of Camelot.otf" +"");
        setTypeface(tf);
    }

}