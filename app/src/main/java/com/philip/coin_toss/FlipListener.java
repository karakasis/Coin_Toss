package com.philip.coin_toss;

import android.animation.ValueAnimator;
import android.view.View;

class FlipListener implements ValueAnimator.AnimatorUpdateListener {

  private final View frontView;
  private final View backView;
  private final int flips;

  public FlipListener(final View front, final View back, final int flips) {
    this.frontView = front;
    this.backView = back;
    this.flips = flips;
    backView.setVisibility(View.GONE);
  }

  /**
   * Main method responsible for rotating and swapping vies to produce animation result
   * @param animation animator object
   */
  @Override
  public void onAnimationUpdate(final ValueAnimator animation) {
    float value = animation.getAnimatedFraction();
    final float animValue = (float) animation.getAnimatedValue();
    float scaleValue = 0.6f - (1.5f * (value - 0.5f) * (value - 0.5f));
    System.out.println(scaleValue + " " + value);
    if (scaleValue <= 0.35 && value > 0.8) {
      scaleValue = 0.35f;
    }
    if (flipBounds(animValue)) {
      this.frontView.setRotationY(180 * animValue);
      this.frontView.setScaleX(scaleValue);
      this.frontView.setScaleY(scaleValue);
      setStateFlipped(false); //front view visible
    } else {
      this.backView.setRotationY(-180 * (1f - animValue));
      this.backView.setScaleX(scaleValue);
      this.backView.setScaleY(scaleValue);
      setStateFlipped(true); //back view visible
    }
  }

  /**
   * Changes visibility of the 2 sides of coin. Depending on stage of animation
   * @param flipped
   */
  private void setStateFlipped(boolean flipped) {
    if (flipped) {
      this.frontView.setVisibility(View.GONE);
      this.backView.setVisibility(View.VISIBLE);
    } else {
      this.frontView.setVisibility(View.VISIBLE);
      this.backView.setVisibility(View.GONE);
    }
  }

  /**
   * Divides animatedValue
   * animatedFraction value will be 0f to 1f
   * animatedValue will be 0f to (float) flips
   *
   * @param val animatedValue of animator
   * @return true if frontView is visible , false if backView
   */
  private boolean flipBounds(float val) {
    for (int i = -1; i < flips; i = i + 2) {
      if (val > (0.5f + i) && val <= (0.5f + i + 1)) {
        return true;
      }
    }
    return false;
  }

}