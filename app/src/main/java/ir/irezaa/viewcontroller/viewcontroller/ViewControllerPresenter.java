package ir.irezaa.viewcontroller.viewcontroller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class ViewControllerPresenter extends FrameLayout implements Presenter {
    private Handler handler;

    private ArrayList<ViewController> viewControllers = new ArrayList<>();
    private ViewController currentViewController;
    private ViewController prevViewController;

    private PresenterContainer frontContainer;
    private PresenterContainer backContainer;

    private Delegate delegate;
    private int counter;

    private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();

    public ViewControllerPresenter(Context context) {
        super(context);
        handler = new Handler(context.getMainLooper());
        frontContainer = new PresenterContainer(context);
        backContainer = new PresenterContainer(context);
        addView(backContainer);
        addView(frontContainer);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public Delegate getDelegate() {
        return delegate;
    }

    @Override
    public void presentViewController(ViewController viewController) {
        presentViewController(viewController, null, false);
    }

    @Override
    public void presentViewController(ViewController viewController, boolean animated) {
        presentViewController(viewController, null, animated);
    }

    @Override
    public void presentViewController(ViewController viewController, Bundle bundle, boolean animated) {
        if (viewController == null) {
            return;
        }

        viewController.num = counter++;

        if (!viewController.onViewControllerCreate(bundle)) {
            return;
        }

        View view = viewController.contentView;

        viewController.parentPresenter = this;

        if (view == null) {
            view = viewController.createView(this);
            viewController.contentView = view;
        }

        if (viewController.contentView == null) {
            throw new IllegalArgumentException("ViewController createView() cant be null");
        }

        if (currentViewController != null) {
            removeViewInternal(frontContainer,currentViewController,false,false);
            addViewInternal(backContainer,currentViewController,false,true);
        }
        addViewInternal(frontContainer,viewController, animated,false);
        viewController.onParentAttached();

        viewControllers.add(viewController);
        currentViewController = viewController;

        if (viewControllers.size() > 1) {
            prevViewController = viewControllers.get(viewControllers.size() - 2);
        }

        if (prevViewController != null) {
            prevViewController.onPause();
        }

        if (currentViewController != null) {
            currentViewController.onResume();
        }
    }

    private void addViewInternal(final PresenterContainer container , final ViewController viewController, boolean animated , boolean removeAllViews) {
        if (removeAllViews) {
            container.removeAllViews();
        }

        container.addView(viewController.contentView , new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (animated) {
            Animator animator = viewController.getPresentAnimation();

            if (animator == null) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(200);
                animatorSet.setInterpolator(accelerateDecelerateInterpolator);
                int width = container.getMeasuredWidth();

                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(viewController.contentView, "alpha", 0.0f, 1.0f),
                        ObjectAnimator.ofFloat(viewController.contentView, "translationX", width / 2, 0));

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        viewController.onPresentAnimationEnd(true);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewController.onPresentAnimationEnd(false);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        viewController.onPresentAnimationStart();
                    }

                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {
                        viewController.onPresentAnimationStart();
                    }
                });

                animator = animatorSet;
            }

            animator.start();
        }
    }

    @Override
    public void popViewController() {
        popViewController(false);
    }

    @Override
    public void popViewController(boolean animated) {
        if (currentViewController == null && viewControllers.size() > 0) {
            currentViewController = viewControllers.get(viewControllers.size() - 1);
        }

        if (delegate != null) {
            if (!delegate.viewControllerWillDestroy(currentViewController)) {
                return;
            }
        }

        removeViewInternal(backContainer,prevViewController,false,false);
        addViewInternal(frontContainer,prevViewController,false,false);

        currentViewController.contentView.bringToFront();
        viewControllers.remove(currentViewController);
        removeViewInternal(frontContainer,currentViewController, animated,false);
        currentViewController.onPause();
        currentViewController.onViewControllerDestroy();

        currentViewController = prevViewController;

        if (currentViewController != null) {
            currentViewController.onResume();
        }

        if (viewControllers.size() > 1) {
            prevViewController = viewControllers.get(viewControllers.size() - 2);
            addViewInternal(backContainer,prevViewController,false,true);
        }
    }

    private void removeViewInternal(final PresenterContainer container , final ViewController viewController, boolean animated , final boolean allViews) {
        if (viewController == null || viewController.contentView == null) {
            return;
        }

        if (animated) {
            Animator animator = viewController.getPopAnimation();

            if (animator == null) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(200);
                animatorSet.setInterpolator(accelerateDecelerateInterpolator);
                int width = container.getMeasuredWidth();

                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(viewController.contentView, "alpha", 1.0f, 0.0f),
                        ObjectAnimator.ofFloat(viewController.contentView, "translationX", 0, width / 2));

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        viewController.onPopAnimationEnd(true);
                        if (allViews) {
                            container.removeAllViews();
                        }
                        container.removeView(viewController.contentView);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewController.onPopAnimationEnd(false);
                        if (allViews) {
                            container.removeAllViews();
                        }
                        container.removeView(viewController.contentView);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        viewController.onPopAnimationStart();
                    }

                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {
                        viewController.onPopAnimationStart();
                    }
                });

                animator = animatorSet;
            }

            animator.start();
        } else {
            if (allViews) {
                container.removeAllViews();
            }
            container.removeView(viewController.contentView);
        }
    }

    @Override
    public void removeViewController(ViewController viewController) {
        if (viewController == null) {
            return;
        }

        if (viewController == currentViewController) {
            popViewController();
            return;
        }

        if (viewController == prevViewController) {
            removeViewInternal(backContainer,prevViewController,false,true);
            if (viewControllers.size() > 2) {
                prevViewController = viewControllers.get(viewControllers.size() - 3);
                addViewInternal(backContainer,prevViewController,false,false);
            }
        }

        viewControllers.remove(viewController);
        viewController.onPause();
        viewController.onViewControllerDestroy();
    }

    @Override
    public boolean onBackPressed() {
        if (currentViewController instanceof Presenter) {
            return ((Presenter) currentViewController).onBackPressed();
        } else if (viewControllers.size() > 1) {
            popViewController(true);
            return true;
        } else {
            return false;
        }
    }

    public ViewController getCurrentViewController() {
        return currentViewController;
    }

    public ViewController getPrevViewController() {
        return prevViewController;
    }

    @Override
    public void onResume() {
        if (currentViewController != null) {
            currentViewController.onResume();
        }
    }

    @Override
    public void onPause() {
        if (currentViewController != null) {
            currentViewController.onPause();
        }
    }

    @Override
    public ArrayList<ViewController> getViewControllers() {
        return viewControllers;
    }

    private static class PresenterContainer extends FrameLayout {

        public PresenterContainer(Context context) {
            super(context);
        }
    }
    public interface Delegate {
        void viewControllerWillPresent(ViewController viewController);

        boolean viewControllerWillDestroy(ViewController viewController);
    }
}
