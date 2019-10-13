package ir.irezaa.viewcontroller.viewcontroller.navigationbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

import ir.irezaa.viewcontroller.R;

public class NavigationBar extends FrameLayout implements NavigationBarImp {
    private static Drawable bottomShadow;

    public NavigationBar(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        bottomShadow = getResources().getDrawable(R.drawable.header_shadow).mutate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(120, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    public void drawShadow(Canvas canvas) {
        bottomShadow.setBounds(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() + bottomShadow.getIntrinsicHeight());
        bottomShadow.draw(canvas);
    }

    @Override
    public View getView() {
        return this;
    }
}
