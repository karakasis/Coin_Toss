package com.philip.coin_toss;

import android.animation.ValueAnimator;
import android.view.View;

public class FlipListener implements ValueAnimator.AnimatorUpdateListener {

    private final View headsView;
    private final View tailsView;
    private final int flips;

    public FlipListener(final View front, final View back , final int flips) {
        this.headsView = front;
        this.tailsView = back;
        this.tailsView.setVisibility(View.GONE);
        this.flips = flips;
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        final float value = animation.getAnimatedFraction();
        final float animValue = (float) animation.getAnimatedValue();
        final float scaleValue = 0.65f - (1.5f * (value - 0.5f) * (value - 0.5f));

        if(flipBounds(animValue)){
            this.headsView.setRotationY(180 * animValue);
            this.headsView.setScaleX(scaleValue);
            this.headsView.setScaleY(scaleValue);
                setStateFlipped(false); //HEADS

            System.out.println(value + " " + scaleValue + " " + animValue + " "
                    + 180 * animValue + " HEADS");

        } else {
            this.tailsView.setRotationY(-180 * (1f- animValue));
            this.tailsView.setScaleX(scaleValue);
            this.tailsView.setScaleY(scaleValue);
                setStateFlipped(true); //TAILS
            System.out.println(value + " " + scaleValue + " " + animValue + " "
                    + -180 * (1f- animValue) + " TAILS");
        }
    }

    private void setStateFlipped(boolean flipped) {
        if (flipped) {
            this.headsView.setVisibility(View.GONE);
            this.tailsView.setVisibility(View.VISIBLE);
        } else {
            this.headsView.setVisibility(View.VISIBLE);
            this.tailsView.setVisibility(View.GONE);
        }
    }

    private boolean flipBounds(float val){
        for(int i=-1; i<flips; i = i + 2){
            if(val> (0.5f + i) && val<= (0.5f + i + 1)){
                return true;
            }
        }
        return false;
    }

}