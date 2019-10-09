package ir.irezaa.viewcontroller.viewcontroller;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class NavigationViewController extends ViewController implements Presenter {
    private ViewControllerPresenter presenter;
    private NavigationBar navigationBar;

    @Override
    protected View createView(ViewGroup parent) {
        navigationBar = new NavigationBar(parent.getContext());
        presenter = new ViewControllerPresenter(parent.getContext());
        presenter.setDelegate(parentPresenter.getDelegate());

        FrameLayout frameLayout = new FrameLayout(parent.getContext()) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
                navigationBar.measure(widthMeasureSpec,heightMeasureSpec);
                presenter.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(getMeasuredHeight() - navigationBar.getMeasuredHeight(),MeasureSpec.EXACTLY));
            }

            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                navigationBar.layout(left, top, right, top + navigationBar.getMeasuredHeight());
                presenter.layout(left, top + navigationBar.getMeasuredHeight(), right, bottom);
            }
        };

        frameLayout.addView(presenter);
        frameLayout.addView(navigationBar);

        return frameLayout;
    }

    @Override
    public void presentViewController(ViewController viewController) {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.presentViewController(viewController);
    }

    @Override
    public void presentViewController(ViewController viewController, boolean animated) {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.presentViewController(viewController, animated);
    }

    @Override
    public void presentViewController(ViewController viewController, Bundle bundle, boolean animated) {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.presentViewController(viewController, bundle, animated);
    }

    @Override
    public void popViewController() {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.popViewController();
    }

    @Override
    public void popViewController(boolean animated) {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.popViewController(animated);
    }

    @Override
    public void removeViewController(ViewController viewController) {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.removeViewController(viewController);
    }

    @Override
    public boolean onBackPressed() {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        return presenter.onBackPressed();
    }

    @Override
    public void onPause() {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.onPause();
    }

    @Override
    public void onResume() {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        presenter.onResume();
    }

    @Override
    public ArrayList<ViewController> getViewControllers() {
        if (presenter == null) {
            throw new IllegalStateException("present navigation controller first!");
        }

        return presenter.getViewControllers();
    }
}
