package ir.irezaa.viewcontroller.viewcontroller.navigationbar;

import android.graphics.Canvas;
import android.view.View;

public interface NavigationBarImp {
    boolean isRelative();
    void drawShadow(Canvas canvas);
    View getView();
}
