package ir.irezaa.viewcontroller.viewcontroller;

import android.os.Bundle;

import java.util.ArrayList;

public interface Presenter {
    void presentViewController(ViewController viewController);

    void presentViewController(ViewController viewController, boolean animated);

    void presentViewController(ViewController viewController, Bundle bundle, boolean animated);

    void popViewController();

    void popViewController(boolean animated);

    void removeViewController(ViewController viewController);

    boolean onBackPressed();

    void onPause();

    void onResume();

    ArrayList<ViewController> getViewControllers();
}
