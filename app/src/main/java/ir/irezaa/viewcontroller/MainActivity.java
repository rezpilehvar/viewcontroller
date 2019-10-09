package ir.irezaa.viewcontroller;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import ir.irezaa.viewcontroller.viewcontroller.NavigationViewController;
import ir.irezaa.viewcontroller.viewcontroller.ViewControllerActivity;

public class MainActivity extends ViewControllerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationViewController navigationViewController = new NavigationViewController();
        viewControllerPresenter.presentViewController(navigationViewController,false);
        navigationViewController.presentViewController(new MainPageViewController(Color.BLUE),false);
    }
}
