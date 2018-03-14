package com.philip.coin_toss;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * Created by Xrhstos on 3/15/2018.
 */

public class FlipListenerEnd implements ValueAnimator.AnimatorListener {

    private MainActivity parent;

    public FlipListenerEnd(MainActivity parent){
        this.parent = parent;
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        System.out.println("End animation");
        parent.makeResult();
        parent.updateScreen();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
