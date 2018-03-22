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

  @Override
  public void onAnimationUpdate(final ValueAnimator animation) {
    float value = animation.getAnimatedFraction();
    final float animValue = (float) animation.getAnimatedValue();
    float scaleValue = 0f;
    if (!MainActivity.disableScaleFlipEffect) {
      if (MainActivity.alternateFlipEffect) {
        value = value * 100f;
        //functions calculated with a linear interpolation with 8 data points
        //https://tools.timodenk.com/linear-interpolation
        if (value <= 2) {
          scaleValue = -12.5f * value + 100f;
        } else if (value > 2 && value <= 15) {
          scaleValue = 1.9231f * value + 7.1154f;
        } else if (value > 15 && value <= 35) {
          scaleValue = 2f * value + 70f;
        } else if (value > 35 && value <= 50) {
          scaleValue = 1.3333f * value + 93.3333f;
        } else if (value > 50 && value <= 70) {
          scaleValue = -1.2f * value + 220f;
        } else if (value > 70 && value <= 80) {
          scaleValue = -1.1f * value + 213f;
        } else if (value > 80 && value <= 90) {
          scaleValue = -2f * value + 285f;
        } else {
          scaleValue = -0.5f * value + 150f;
        }

        scaleValue = 0.35f * scaleValue / 100f;
      } else {
        scaleValue = 0.6f - (1.5f * (value - 0.5f) * (value - 0.5f));
        System.out.println(scaleValue + " " + value);
        if (scaleValue <= 0.35 && value > 0.8) {
          scaleValue = 0.35f;
        }
      }
    }

    if (flipBounds(animValue)) {
      this.frontView.setRotationY(180 * animValue);
      if(!MainActivity.disableScaleFlipEffect){

        this.frontView.setScaleX(scaleValue);
        this.frontView.setScaleY(scaleValue);
      }
      setStateFlipped(false); //front view visible
    } else {
      this.backView.setRotationY(-180 * (1f - animValue));
      if(!MainActivity.disableScaleFlipEffect){
        this.backView.setScaleX(scaleValue);
        this.backView.setScaleY(scaleValue);
      }
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

  private boolean flipBounds(float val) {
    for (int i = -1; i < flips; i = i + 2) {
      if (val > (0.5f + i) && val <= (0.5f + i + 1)) {
        return true;
      }
    }
    return false;
  }

}