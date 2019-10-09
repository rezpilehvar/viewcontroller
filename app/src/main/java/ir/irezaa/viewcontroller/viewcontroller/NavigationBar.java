package ir.irezaa.viewcontroller.viewcontroller;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

public class NavigationBar extends FrameLayout {
    public NavigationBar(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(120, MeasureSpec.EXACTLY));
    }
}
