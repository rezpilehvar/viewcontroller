package ir.irezaa.viewcontroller.viewcontroller.navigationbar;

import android.graphics.Canvas;
import android.view.View;

import ir.irezaa.viewcontroller.viewcontroller.ViewControllerPresenter;

public interface NavigationBarImp {
    boolean isRelative();
    void drawShadow(Canvas canvas);
    View getView();
    ViewControllerPresenter.TransitionDelegate getTransitionDelegate();
}