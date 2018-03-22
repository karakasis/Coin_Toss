package com.philip.coin_toss;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

  private EnumChoice choice;
  private EnumChoice result = EnumChoice.HEADS; // wont mess with the app results
  private boolean newHighscore = false;
  private Random rand = new Random();

  private enum EnumChoice {
    HEADS,
    TAILS
  }

  private static final String SCORE_KEY = "SCORE";
  private static final String HIGHSCORE_KEY = "HIGHSCORE";
  private static final String STRIKES_KEY = "STRIKES";
  private static final String WAS_HEADS_KEY = "WAS_HEADS";
  private static final String IS_HEADS_KEY = "IS_HEADS";
  private int score = 0;
  private int highScore = 0;
  private ArrayList<String> strikes = new ArrayList<>();

  public ImageView headsView;
  public ImageView tailsView;
  public Button headsButton;
  public Button tailsButton;

  private boolean wasDisplayingHeads = true;
  private boolean isDisplayingHeads = true;

  private ValueAnimator mFlipAnimator;
  private ValueAnimator scaleAnimator;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    headsView = (ImageView) findViewById(R.id.headsViewXML);
    tailsView = (ImageView) findViewById(R.id.tailsViewXML);
    headsButton = (Button) findViewById(R.id.heads);
    tailsButton = (Button) findViewById(R.id.tails);

    TranslateAnimation r2l = new TranslateAnimation(1500.0f, 0.0f, 0.0f,
        0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
    r2l.setDuration(1000); // animation duration
    r2l.setRepeatCount(0); // animation repeat count if u want to repeat
    r2l.setFillAfter(true);
    tailsButton.startAnimation(r2l);//your_view for mine is imageView

    TranslateAnimation l2r = new TranslateAnimation(-1500.0f, 0.0f, 0.0f,
        0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
    l2r.setDuration(1000); // animation duration
    l2r.setRepeatCount(0); // animation repeat count if u want to repeat
    l2r.setFillAfter(true);
    headsButton.startAnimation(l2r);//your_view for mine is imageView

    //First run will initialize animation
    mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
    mFlipAnimator.addListener(new FlipListenerEnd(this));
    mFlipAnimator.setDuration(1500);
    mFlipAnimator.setInterpolator(new LinearInterpolator());

    if (savedInstanceState != null) {
      //load bundle
      score = savedInstanceState.getInt(SCORE_KEY);
      highScore = savedInstanceState.getInt(HIGHSCORE_KEY);
      strikes = new ArrayList<>(savedInstanceState.getStringArrayList(STRIKES_KEY));
      isDisplayingHeads = savedInstanceState.getBoolean(IS_HEADS_KEY);
      wasDisplayingHeads = savedInstanceState.getBoolean(WAS_HEADS_KEY);

      if (isDisplayingHeads) {
        result = EnumChoice.HEADS;
        headsView.setVisibility(View.VISIBLE);
        tailsView.setVisibility(View.GONE);
      } else {
        result = EnumChoice.TAILS;
        tailsView.setVisibility(View.VISIBLE);
        headsView.setVisibility(View.GONE);
      }

      updateScreen();
    }

    scaleAnimator = ValueAnimator.ofFloat(0f, 1f);
    //mFlipAnimator.addListener(new FlipListenerEnd(this));
    scaleAnimator.setDuration(1500);
    scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    setupScaleAnimation();
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    bundle.putInt(SCORE_KEY, score);
    bundle.putInt(HIGHSCORE_KEY, highScore);
    bundle.putStringArrayList(STRIKES_KEY, strikes);
    bundle.putBoolean(WAS_HEADS_KEY, wasDisplayingHeads);
    bundle.putBoolean(IS_HEADS_KEY, isDisplayingHeads);
  }

  public void update(View view) {

    result = result(rand.nextBoolean());

    setupAnimation(randomFlips());

    //Heads button clicked
    if (view.getId() == R.id.heads) {

      choice = EnumChoice.HEADS;
    }
    //or tails button clicked
    else if (view.getId() == R.id.tails) {

      choice = EnumChoice.TAILS;
    }
    //Begins the coin animation
    mFlipAnimator.start();
  }

  private EnumChoice result(boolean val) {
    if (result == EnumChoice.HEADS) // previous roll
    {
      wasDisplayingHeads = true;
    } else {
      wasDisplayingHeads = false;
    }

    if (val) {
      isDisplayingHeads = true;
      return EnumChoice.HEADS;
    } else {
      isDisplayingHeads = false;
      return EnumChoice.TAILS;
    }
  }

  private int randomFlips() {
    if (result == EnumChoice.HEADS) {
      if (wasDisplayingHeads) {
        // result = HEADS , previous result = HEADS , saved result = HEADS
        //return 2 * (rand.nextInt(1) + 1);
        return 2;
      } else {
        // result = HEADS , previous result = TAILS , saved result = HEADS
        //return 2 * (rand.nextInt(1) + 1) + 1;
        return 1;
      }
    } else if (result == EnumChoice.TAILS) {
      if (wasDisplayingHeads) {
        // result = TAILS , previous result = HEADS , saved result = TAILS
        //return 2 * (rand.nextInt(1) + 1) + 1;
        return 1;
      } else {
        // result = TAILS , previous result = TAILS , saved result = TAILS
        //return 2 * (rand.nextInt(1) + 1);
        return 2;
      }
    }
    return 0;//possible bug
  }

  private void updateScreen() {

    TextView scoreTV = (TextView) findViewById(R.id.score);
    scoreTV.setText(String.valueOf(score));
    TextView highScoreTV = (TextView) findViewById(R.id.highScore);
    highScoreTV.setText(String.valueOf(highScore));
    //
    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.strikesLayout);
    linearLayout.removeAllViewsInLayout();
    for (String ch : strikes) {
      TextView textView = new TextView(this);
      if (ch.equals("H")) {
        textView.setText(R.string.strikesHeads);
        textView.setTextAppearance(this, R.style.StrikeHApp);
      } else {
        textView.setText(R.string.strikesTails);
        textView.setTextAppearance(this, R.style.StrikeTApp);
      }
      linearLayout.addView(textView);
    }
    Log.v("MainActivity", "Update screen was called");
  }

  public void makeResult() {

    if (result == choice) {
      //win
      score++;
      if (score > highScore) {
        highScore = score;
        newHighscore = true;
      }
      strikes.add(choice.toString().charAt(0) + "");
    } else {
      //lose
      score = 0;
      strikes.clear();
      if (newHighscore) {
        Toast.makeText(this, R.string.highScoreToast, Toast.LENGTH_SHORT).show();
        newHighscore = false;
      }
    }
    updateScreen();
  }

  private void setupAnimation(int flips) {
    mFlipAnimator.setFloatValues(0f, (float) flips);

    if (wasDisplayingHeads) {
      mFlipAnimator.addUpdateListener(new FlipListener(headsView, tailsView, flips));
    } else {
      mFlipAnimator.addUpdateListener(new FlipListener(tailsView, headsView, flips));
    }
  }

  //https://stackoverflow.com/questions/7414065/android-scale-animation-on-view
  private void setupScaleAnimation() {
    scaleAnimator.setFloatValues(0f, 1f);

    if (isDisplayingHeads) {
      scaleAnimator.addUpdateListener(new ScaleListener(headsView, tailsView));
    } else {
      scaleAnimator.addUpdateListener(new ScaleListener(tailsView, headsView));
    }
    scaleAnimator.start();
  }
}
