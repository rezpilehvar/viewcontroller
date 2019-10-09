package ir.irezaa.viewcontroller.viewcontroller;

import android.app.Activity;
import android.os.Bundle;

public abstract class ViewControllerActivity extends Activity implements ViewControllerPresenter.Delegate {
    public ViewControllerPresenter viewControllerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewControllerPresenter = new ViewControllerPresenter(this);
        viewControllerPresenter.setDelegate(this);
        setContentView(viewControllerPresenter);
    }

    @Override
    public void onBackPressed() {
        if (viewControllerPresenter == null) {
            throw new IllegalStateException("create activity first");
        }

        if (!viewControllerPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewControllerPresenter == null) {
            throw new IllegalStateException("create activity first");
        }

        viewControllerPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewControllerPresenter == null) {
            throw new IllegalStateException("create activity first");
        }

        viewControllerPresenter.onPause();
    }

    @Override
    public boolean viewControllerWillDestroy(ViewController viewController) {
        if (viewControllerPresenter.getCurrentViewController() instanceof Presenter) {
            if (((Presenter) viewControllerPresenter.getCurrentViewController()).getViewControllers().size() == 1) {
                onBackPressed();
                return false;
            }
        }else if (viewControllerPresenter.getViewControllers().size() == 1) {
            onBackPressed();
            return false;
        }

        return true;
    }

    @Override
    public void viewControllerWillPresent(ViewController viewController) {

    }
}
