package ir.irezaa.viewcontroller.viewcontroller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;


public class ViewControllerPresenter extends FrameLayout implements Presenter {
    private Handler handler;

    private ArrayList<ViewController> viewControllers = new ArrayList<>();
    private ViewController currentViewController;
    private ViewController prevViewController;

    private PresenterContainer frontContainer;
    private PresenterContainer backContainer;

    private Delegate delegate;
    private TransitionDelegate transitionDelegate;
    private int counter;

    private boolean presentAnimationInProgress = false;
    private boolean popAnimationInProgress = false;

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

    public void setTransitionDelegate(TransitionDelegate transitionDelegate) {
        this.transitionDelegate = transitionDelegate;
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
            removeViewInternal(frontContainer, currentViewController, false, false, false);
            addViewInternal(backContainer, currentViewController, false, true, false);
        }
        addViewInternal(frontContainer, viewController, animated, false, true);
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

    private void addViewInternal(final PresenterContainer container, final ViewController viewController, boolean animated, boolean removeAllViews, final boolean callTransitionDelegate) {
        if (removeAllViews) {
            container.removeAllViews();
        }

        container.addView(viewController.contentView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (animated) {
            Animator animator = viewController.getPresentAnimation();

            if (animator == null) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(200);
                animatorSet.setInterpolator(accelerateDecelerateInterpolator);
                int width = container.getMeasuredWidth();

                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(viewController.contentView, "alpha", 0.0f, 1.0f);

                if (callTransitionDelegate) {
                    alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            if (presentAnimationInProgress) {
                                float animationProgress = (float) animation.getAnimatedValue();
                                viewController.onPresentAnimationProgressChanged(animationProgress);
                                if (transitionDelegate != null) {
                                    transitionDelegate.onPresentAnimationProgressChanged(prevViewController, currentViewController, animationProgress);
                                }
                            }
                        }
                    });
                }

                animatorSet.playTogether(alphaAnimator,
                        ObjectAnimator.ofFloat(viewController.contentView, "translationX", width / 2, 0));

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        presentAnimationInProgress = false;
                        if (callTransitionDelegate) {
                            viewController.onPresentAnimationEnd(true);
                            if (transitionDelegate != null) {
                                transitionDelegate.onPresentAnimationEnd(prevViewController, currentViewController, true);
                            }
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        presentAnimationInProgress = false;

                        if (callTransitionDelegate) {
                            viewController.onPresentAnimationEnd(false);
                            if (transitionDelegate != null) {
                                transitionDelegate.onPresentAnimationEnd(prevViewController, currentViewController, false);
                            }
                        }
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        presentAnimationInProgress = true;

                        if (callTransitionDelegate) {
                            viewController.onPresentAnimationStart();
                            if (transitionDelegate != null) {
                                transitionDelegate.onPresentAnimationStart(currentViewController, viewController);
                            }
                        }
                    }

                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {
                        presentAnimationInProgress = true;

                        if (callTransitionDelegate) {
                            viewController.onPresentAnimationStart();
                            if (transitionDelegate != null) {
                                transitionDelegate.onPresentAnimationStart(currentViewController, viewController);
                            }
                        }
                    }
                });

                animator = animatorSet;
            }

            animator.start();
        } else {
            if (callTransitionDelegate) {
                viewController.onPresentAnimationStart();
                if (transitionDelegate != null) {
                    transitionDelegate.onPresentAnimationStart(currentViewController, viewController);
                }
                viewController.onPresentAnimationEnd(false);
                if (transitionDelegate != null) {
                    transitionDelegate.onPresentAnimationEnd(currentViewController, viewController, false);
                }
            }
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

        removeViewInternal(backContainer, prevViewController, false, false, false);
        addViewInternal(frontContainer, prevViewController, false, false, false);

        currentViewController.contentView.bringToFront();
        viewControllers.remove(currentViewController);
        removeViewInternal(frontContainer, currentViewController, animated, false, true);
        currentViewController.onPause();
        currentViewController.onViewControllerDestroy();

        currentViewController = prevViewController;

        if (currentViewController != null) {
            currentViewController.onResume();
        }

        if (viewControllers.size() > 1) {
            prevViewController = viewControllers.get(viewControllers.size() - 2);
            addViewInternal(backContainer, prevViewController, false, true, false);
        }
    }

    private void removeViewInternal(final PresenterContainer container, final ViewController viewController, boolean animated, final boolean allViews, final boolean callTransitionDelegate) {
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

                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(viewController.contentView, "alpha", 1.0f, 0.0f);

                if (callTransitionDelegate) {
                    alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            if (popAnimationInProgress) {
                                float animationProgress = (float) animation.getAnimatedValue();
                                viewController.onPopAnimationProgressChanged(animationProgress);
                                if (transitionDelegate != null) {
                                    transitionDelegate.onPopAnimationProgressChanged(viewController, currentViewController, animationProgress);
                                }
                            }
                        }
                    });
                }

                animatorSet.playTogether(alphaAnimator,
                        ObjectAnimator.ofFloat(viewController.contentView, "translationX", 0, width / 2));

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        popAnimationInProgress = false;

                        if (callTransitionDelegate) {
                            viewController.onPopAnimationEnd(true);

                            if (transitionDelegate != null) {
                                transitionDelegate.onPopAnimationEnd(viewController,currentViewController , true);
                            }
                        }

                        if (allViews) {
                            container.removeAllViews();
                        }
                        container.removeView(viewController.contentView);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        popAnimationInProgress = false;

                        if (callTransitionDelegate) {
                            viewController.onPopAnimationEnd(true);

                            if (transitionDelegate != null) {
                                transitionDelegate.onPopAnimationEnd(viewController,currentViewController , false);
                            }
                        }
                        if (allViews) {
                            container.removeAllViews();
                        }
                        container.removeView(viewController.contentView);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        popAnimationInProgress = true;

                        viewController.onPopAnimationStart();
                        if (transitionDelegate != null) {
                            transitionDelegate.onPopAnimationStart(currentViewController, prevViewController);
                        }
                    }

                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {
                        popAnimationInProgress = true;

                        viewController.onPopAnimationStart();
                        if (transitionDelegate != null) {
                            transitionDelegate.onPopAnimationStart(currentViewController, prevViewController);
                        }
                    }
                });

                animator = animatorSet;
            }

            animator.start();
        } else {
            if (callTransitionDelegate) {
                viewController.onPopAnimationStart();
                if (transitionDelegate != null) {
                    transitionDelegate.onPopAnimationStart(currentViewController, prevViewController);
                }
                viewController.onPopAnimationEnd(false);
                if (transitionDelegate != null) {
                    transitionDelegate.onPopAnimationEnd(currentViewController, prevViewController,false);
                }
            }

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
            removeViewInternal(backContainer, prevViewController, false, true, false);
            if (viewControllers.size() > 2) {
                prevViewController = viewControllers.get(viewControllers.size() - 3);
                addViewInternal(backContainer, prevViewController, false, false, false);
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

    public interface TransitionDelegate {
        void onPresentAnimationStart(ViewController from, ViewController to);

        void onPresentAnimationProgressChanged(ViewController from, ViewController to, float progress);

        void onPresentAnimationEnd(ViewController from, ViewController to, boolean canceled);

        void onPopAnimationStart(ViewController from, ViewController to);

        void onPopAnimationProgressChanged(ViewController from, ViewController to, float progress);

        void onPopAnimationEnd(ViewController from, ViewController to , boolean canceled);
    }
}
