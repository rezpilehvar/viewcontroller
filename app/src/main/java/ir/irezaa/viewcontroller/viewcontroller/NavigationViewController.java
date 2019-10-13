package ir.irezaa.viewcontroller.viewcontroller;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import ir.irezaa.viewcontroller.viewcontroller.navigationbar.NavigationBar;
import ir.irezaa.viewcontroller.viewcontroller.navigationbar.NavigationBarImp;

public class NavigationViewController extends ViewController implements Presenter {
    private ViewControllerPresenter presenter;
    private NavigationBarImp navigationBar;

    @Override
    protected View createView(ViewGroup parent) {
        navigationBar = createNavigationBar(parent);

        presenter = new ViewControllerPresenter(parent.getContext());
        presenter.setDelegate(parentPresenter.getDelegate());
        presenter.setTransitionDelegate(new ViewControllerPresenter.TransitionDelegate() {
            @Override
            public void onPresentAnimationStart(ViewController from, ViewController to) {
                Log.i("ControllerTransition", "onPresentAnimationStart -> from : " + (from != null ? from.num : "null") + " to : " + (to != null ? to.num : "null"));
            }

            @Override
            public void onPresentAnimationProgressChanged(ViewController from, ViewController to, float progress) {
                Log.i("ControllerTransition", "onPresentAnimationProgressChanged -> progress : " + progress + " from : " + (from != null ? from.num : "null") + " to : " + (to != null ? to.num : "null"));
            }

            @Override
            public void onPresentAnimationEnd(ViewController from, ViewController to, boolean canceled) {
                Log.i("ControllerTransition", "onPresentAnimationEnd -> from : " + (from != null ? from.num : "null") + " to : " + (to != null ? to.num : "null"));
            }

            @Override
            public void onPopAnimationStart(ViewController from, ViewController to) {
                Log.i("ControllerTransition", "onPopAnimationStart -> from : " + (from != null ? from.num : "null") + " to : " + (to != null ? to.num : "null"));
            }

            @Override
            public void onPopAnimationProgressChanged(ViewController from, ViewController to, float progress) {
                Log.i("ControllerTransition", "onPopAnimationProgressChanged -> progress : " + progress + " from : " + (from != null ? from.num : "null") + " to : " + (to != null ? to.num : "null"));
            }

            @Override
            public void onPopAnimationEnd(ViewController from, ViewController to , boolean canceled) {
                Log.i("ControllerTransition", "onPopAnimationEnd -> from : " + (from != null ? from.num : "null") + " to : " + (to != null ? to.num : "null"));
            }
        });


        FrameLayout frameLayout = new FrameLayout(parent.getContext()) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
                navigationBar.getView().measure(widthMeasureSpec, heightMeasureSpec);

                if (navigationBar.isRelative()) {
                    presenter.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
                } else {
                    presenter.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight() - navigationBar.getView().getMeasuredHeight(), MeasureSpec.EXACTLY));
                }
            }

            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                navigationBar.getView().layout(left, top, right, top + navigationBar.getView().getMeasuredHeight());

                if (navigationBar.isRelative()) {
                    presenter.layout(left, top, right, bottom);
                } else {
                    presenter.layout(left, top + navigationBar.getView().getMeasuredHeight(), right, bottom);
                }
            }

            @Override
            protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
                boolean result = super.drawChild(canvas, child, drawingTime);

                if (child == navigationBar.getView()) {
                    navigationBar.drawShadow(canvas);
                }

                return result;
            }
        };

        frameLayout.addView(presenter);
        frameLayout.addView(navigationBar.getView());

        return frameLayout;
    }

    public NavigationBarImp createNavigationBar(ViewGroup parent) {
        return new NavigationBar(parent.getContext());
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
