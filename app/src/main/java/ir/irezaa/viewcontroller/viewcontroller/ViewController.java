package ir.irezaa.viewcontroller.viewcontroller;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewController {
    protected View contentView;
    protected ViewControllerPresenter parentPresenter;
    protected int num;

    protected boolean onViewControllerCreate(Bundle bundle) {
        return true;
    }

    protected void onViewControllerDestroy() {
    }

    protected abstract View createView(ViewGroup parent);

    protected void onParentAttached() {
    }

    protected Animator getPresentAnimation() {
        return null;
    }

    protected Animator getPopAnimation() {
        return null;
    }

    protected void onPresentAnimationEnd(boolean canceled) {
    }

    protected void onPresentAnimationStart() {
    }

    protected void onPresentAnimationProgressChanged(float progress) {

    }

    protected void onPopAnimationStart() {
    }

    protected void onPopAnimationProgressChanged(float progress) {

    }

    protected void onPopAnimationEnd(boolean canceled) {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }
}
