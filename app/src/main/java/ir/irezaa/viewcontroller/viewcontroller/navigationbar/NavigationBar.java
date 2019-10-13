package ir.irezaa.viewcontroller.viewcontroller.navigationbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

import ir.irezaa.viewcontroller.R;
import ir.irezaa.viewcontroller.viewcontroller.ViewController;
import ir.irezaa.viewcontroller.viewcontroller.ViewControllerPresenter;

public class NavigationBar extends FrameLayout implements NavigationBarImp , ViewControllerPresenter.TransitionDelegate {
    private static Drawable bottomShadow;

    public NavigationBar(Context context) {
        super(context);
        setBackgroundColor(Color.DKGRAY);
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

    @Override
    public ViewControllerPresenter.TransitionDelegate getTransitionDelegate() {
        return this;
    }

    @Override
    public void onPresentAnimationStart(ViewController from, ViewController to) {

    }

    @Override
    public void onPresentAnimationProgressChanged(ViewController from, ViewController to, float progress) {

    }

    @Override
    public void onPresentAnimationEnd(ViewController from, ViewController to, boolean canceled) {

    }

    @Override
    public void onPopAnimationStart(ViewController from, ViewController to) {

    }

    @Override
    public void onPopAnimationProgressChanged(ViewController from, ViewController to, float progress) {

    }

    @Override
    public void onPopAnimationEnd(ViewController from, ViewController to, boolean canceled) {

    }
}
