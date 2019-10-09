package ir.irezaa.viewcontroller;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import ir.irezaa.viewcontroller.viewcontroller.ViewController;

public class MainPageViewController extends ViewController {
    private int backgroundColor;

    public MainPageViewController(int color) {
        this.backgroundColor = color;
    }

    @Override
    protected boolean onViewControllerCreate(Bundle bundle) {
        Log.i("ViewController", "onViewControllerCreate() num : " + num);

        return true;
    }

    @Override
    protected void onViewControllerDestroy() {
        Log.i("ViewController", "onViewControllerDestroy() num : " + num);
    }

    @Override
    protected View createView(final ViewGroup parent) {
        Log.i("ViewController", "createView() num : " + num);

        FrameLayout frameLayout = new FrameLayout(parent.getContext());

        frameLayout.setBackgroundColor(backgroundColor);

        Button presentButton = new Button(parent.getContext());
        presentButton.setText("Present");

        frameLayout.addView(presentButton, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (backgroundColor == Color.BLUE) {
            ((FrameLayout.LayoutParams) presentButton.getLayoutParams()).gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        } else {
            ((FrameLayout.LayoutParams) presentButton.getLayoutParams()).gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }

        presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentPresenter.presentViewController(new MainPageViewController(backgroundColor == Color.BLUE ? Color.GREEN : Color.BLUE), true);
            }
        });

        Button popButton = new Button(parent.getContext());
        popButton.setText("Pop");

        frameLayout.addView(popButton, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (backgroundColor == Color.BLUE) {
            ((FrameLayout.LayoutParams) popButton.getLayoutParams()).gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        } else {
            ((FrameLayout.LayoutParams) popButton.getLayoutParams()).gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        }

        popButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentPresenter.popViewController(true);
            }
        });

        Button removeButton = new Button(parent.getContext());
        removeButton.setText("Remove -3 controller");

        frameLayout.addView(removeButton, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ((FrameLayout.LayoutParams) removeButton.getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentPresenter.getViewControllers().size() > 2) {
                    parentPresenter.removeViewController(parentPresenter.getViewControllers().get(parentPresenter.getViewControllers().size() - 2));
                }
            }
        });

        TextView numberTextView = new TextView(parent.getContext());
        numberTextView.setText("" + num);
        numberTextView.setTypeface(numberTextView.getTypeface(), Typeface.BOLD);
        numberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
        frameLayout.addView(numberTextView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((FrameLayout.LayoutParams) numberTextView.getLayoutParams()).gravity = Gravity.CENTER;


        Button presentMain = new Button(parent.getContext());
        presentMain.setText("Present On Main");

        frameLayout.addView(presentMain, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ((FrameLayout.LayoutParams) presentMain.getLayoutParams()).gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        presentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).viewControllerPresenter.presentViewController(new MainPageViewController(Color.BLUE),true);
            }
        });

        return frameLayout;
    }

    @Override
    protected void onParentAttached() {
        super.onParentAttached();
        Log.i("ViewController", "onParentAttached() num : " + num);
    }

    @Override
    protected void onPopAnimationEnd(boolean canceled) {
        super.onPopAnimationEnd(canceled);
        Log.i("ViewController", "onPopAnimationEnd() num : " + num);
    }

    @Override
    protected void onPopAnimationStart() {
        super.onPopAnimationStart();
        Log.i("ViewController", "onPopAnimationStart() num : " + num);
    }

    @Override
    protected void onPresentAnimationEnd(boolean canceled) {
        super.onPresentAnimationEnd(canceled);
        Log.i("ViewController", "onPresentAnimationEnd() num : " + num);
    }

    @Override
    protected void onPresentAnimationStart() {
        super.onPresentAnimationStart();
        Log.i("ViewController", "onPresentAnimationStart() num : " + num);
    }

    @Override
    protected Animator getPopAnimation() {
        Log.i("ViewController", "getPopAnimation() num : " + num);
        return super.getPopAnimation();
    }

    @Override
    protected Animator getPresentAnimation() {
        Log.i("ViewController", "getPresentAnimation() num : " + num);
        return super.getPresentAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ViewController", "onResume() num : " + num);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ViewController", "onPause() num : " + num);
    }
}
