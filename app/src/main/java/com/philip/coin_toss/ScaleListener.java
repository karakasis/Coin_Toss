package com.philip.coin_toss;

import android.animation.ValueAnimator;
import android.view.View;

class ScaleListener implements ValueAnimator.AnimatorUpdateListener {

  private final View frontView;

  public ScaleListener(final View front, final View back) {
    this.frontView = front;
    View backView = back;
    backView.setVisibility(View.GONE);
  }

  @Override
  public void onAnimationUpdate(final ValueAnimator animation) {
    float value = animation.getAnimatedFraction();
    float scaleValue;
    value = value * 100f;
    //functions calculated with a linear interpolation with 5 data points
    //https://tools.timodenk.com/linear-interpolation
    if(value<=25){
      scaleValue = 1.2f * value;
    }else if(value>25 && value<=50){
      scaleValue = 1.4f * value - 5f;
    }else if(value>50 && value<=70){
      scaleValue = 1.75f*value - 22.5f;
    }else if(value>70 && value<=85){
      scaleValue = 2f * value - 40f;
    }else{
      scaleValue = -2f * value + 300f;
    }

    scaleValue = 0.35f * scaleValue / 100f ;
    System.out.println(scaleValue);

    this.frontView.setScaleX(scaleValue);
    this.frontView.setScaleY(scaleValue);

  }

}