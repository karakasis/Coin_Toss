package com.philip.coin_toss;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.locks.Lock;

/**
 * Created by Xrhstos on 3/15/2018.
 */

public class FlipListenerEnd implements ValueAnimator.AnimatorListener {

    private MainActivity parent;
    private Button butA;
    private Button butB;

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

        parent.headsView.setLayerType(parent.headsView.LAYER_TYPE_HARDWARE, null);
        parent.tailsView.setLayerType(parent.tailsView.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        parent.makeResult();
        butA.setEnabled(true);
        butB.setEnabled(true);

        parent.headsView.setLayerType(parent.headsView.LAYER_TYPE_NONE, null);
        parent.tailsView.setLayerType(parent.tailsView.LAYER_TYPE_NONE, null);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
