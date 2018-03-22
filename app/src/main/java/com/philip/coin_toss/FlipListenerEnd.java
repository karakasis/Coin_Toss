package com.philip.coin_toss;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.widget.Button;

/**
 * Created by Xrhstos on 3/15/2018.
 */

class FlipListenerEnd implements ValueAnimator.AnimatorListener {

    private final MainActivity parent;
    private final Button butA;
    private final Button butB;

    public FlipListenerEnd(MainActivity parent){
        this.parent = parent;
        butA = parent.headsButton;
        butB = parent.tailsButton;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        LockActivityOrientation.lockActivityOrientation(parent);
        butA.setEnabled(false);
        butB.setEnabled(false);
    }

    @Override
    public void onAnimationEnd(Animator animator) {

        parent.tailsView.setScaleX(0.35f);
        parent.tailsView.setScaleY(0.35f);
        parent.headsView.setScaleX(0.35f);
        parent.headsView.setScaleY(0.35f);

        parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        parent.makeResult();
        butA.setEnabled(true);
        butB.setEnabled(true);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
