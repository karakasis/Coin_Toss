package com.philip.coin_toss;

import android.animation.ValueAnimator;
import android.view.View;

public class FlipListener implements ValueAnimator.AnimatorUpdateListener {

    private final View frontView;
    private final View backView;
    private final int flips;

    public FlipListener(final View front, final View back , final int flips) {
        this.frontView = front;
        this.backView = back;
        this.flips = flips;
        backView.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        final float value = animation.getAnimatedFraction();
        final float animValue = (float) animation.getAnimatedValue();
        final float scaleValue = 0.65f - (1.5f * (value - 0.5f) * (value - 0.5f));

        if(flipBounds(animValue)){
            this.frontView.setRotationY(180 * animValue);
            this.frontView.setScaleX(scaleValue);
            this.frontView.setScaleY(scaleValue);
                setStateFlipped(false); //front view visible
        } else {
            this.backView.setRotationY(-180 * (1f- animValue));
            this.backView.setScaleX(scaleValue);
            this.backView.setScaleY(scaleValue);
                setStateFlipped(true); //back view visible
        }
    }

    private void setStateFlipped(boolean flipped) {
        if (flipped) {
            this.frontView.setVisibility(View.GONE);
            this.backView.setVisibility(View.VISIBLE);
        } else {
            this.frontView.setVisibility(View.VISIBLE);
            this.backView.setVisibility(View.GONE);
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