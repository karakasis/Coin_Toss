package com.philip.coin_toss;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

  private int swapFlips = 3;
  private static final String SCORE_KEY = "SCORE";
  private static final String HIGHSCORE_KEY = "HIGHSCORE";
  private static final String STRIKES_KEY = "STRIKES";
  private static final String WAS_HEADS_KEY = "WAS_HEADS";
  private static final String IS_HEADS_KEY = "IS_HEADS";
  private static final String SOUND_KEY = "SOUND";
  private final ArrayList<Toast> toastDatabase = new ArrayList<>();
  private final Random rand = new Random();
  public ImageView headsView;
  public ImageView tailsView;
  public Button headsButton;
  public Button tailsButton;

  private EnumChoice choice;
  private EnumChoice result = EnumChoice.HEADS;
  private boolean newHighscore = false;
  private int score = 0;
  private int highScore = 0;
  private ArrayList<String> strikes = new ArrayList<>();
  private boolean wasDisplayingHeads = true;
  private boolean isDisplayingHeads = true;
  private ValueAnimator mFlipAnimator;
  private ValueAnimator scaleAnimator;
  private MediaPlayer coinSound;
  private MediaPlayer scoreSound;
  private MediaPlayer highScoreSound;
  private boolean soundEnabled = true;

  /**
   * Kills all running toasts, avoid queuing toasts
   */
  private void killAllToast() {
    for (Toast t : toastDatabase) {
      if (t != null) {
        t.cancel();
      }
    }
    toastDatabase.clear();
  }

  /**
   * Init method for mp3 sounds
   */
  private void initMP3() {
    coinSound = MediaPlayer.create(this, R.raw.coin);
    scoreSound = MediaPlayer.create(this, R.raw.score);
    highScoreSound = MediaPlayer.create(this, R.raw.highscore);

  }

  /**
   * Mutes global sound in app
   * @param view is the speaker icon in xml
   */
  public void mute(View view) {
    if (soundEnabled) {
      soundEnabled = false;
      view.setBackground(getResources().getDrawable(R.drawable.mute));
    } else {
      soundEnabled = true;
      view.setBackground(getResources().getDrawable(R.drawable.volume));
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initMP3();

    headsView = findViewById(R.id.headsViewXML);
    tailsView = findViewById(R.id.tailsViewXML);
    headsButton = findViewById(R.id.heads);
    tailsButton = findViewById(R.id.tails);

    View speakerView = findViewById(R.id.muteButton);

    //button slide r2left
    TranslateAnimation r2l = new TranslateAnimation(1500.0f, 0.0f, 0.0f,
        0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
    r2l.setDuration(1000); // animation duration
    r2l.setFillAfter(true);
    tailsButton.startAnimation(r2l);
    //button slide l2right
    TranslateAnimation l2r = new TranslateAnimation(-1500.0f, 0.0f, 0.0f,
        0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
    l2r.setDuration(1000); // animation duration
    l2r.setFillAfter(true);
    headsButton.startAnimation(l2r);

    //First run will initialize animation
    mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
    mFlipAnimator.addListener(new FlipListenerEnd(this));
    mFlipAnimator.setDuration(1500);
    mFlipAnimator.setInterpolator(new LinearInterpolator());

    if (savedInstanceState != null) {
      //load bundle
      score = savedInstanceState.getInt(SCORE_KEY);
      highScore = savedInstanceState.getInt(HIGHSCORE_KEY);
      //noinspection ConstantConditions
      strikes = new ArrayList<>(savedInstanceState.getStringArrayList(STRIKES_KEY));
      isDisplayingHeads = savedInstanceState.getBoolean(IS_HEADS_KEY);
      wasDisplayingHeads = savedInstanceState.getBoolean(WAS_HEADS_KEY);
      soundEnabled = savedInstanceState.getBoolean(SOUND_KEY);

      if (isDisplayingHeads) {
        result = EnumChoice.HEADS;
        headsView.setVisibility(View.VISIBLE);
        tailsView.setVisibility(View.GONE);
      } else {
        result = EnumChoice.TAILS;
        tailsView.setVisibility(View.VISIBLE);
        headsView.setVisibility(View.GONE);
      }

      if (!soundEnabled) {
        speakerView.setBackground(getResources().getDrawable(R.drawable.mute));
      }
      updateScreen();
    }

    scaleAnimator = ValueAnimator.ofFloat(0f, 1f);
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
    bundle.putBoolean(SOUND_KEY, soundEnabled);
  }

  /**
   * Gets called after heads/tail click
   * contains basic setup to get things running
   * also starts coin animation
   * @param view is heads/tails button
   */
  public void update(View view) {

    if (soundEnabled) {
      coinSound.start();
    }
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

  /**
   * Converts boolean refering to heads or tails to enum
   * @param val true = heads / false = tails
   * @return proper enum wording
   */
  private EnumChoice result(boolean val) {
    // previous roll
    wasDisplayingHeads = result == EnumChoice.HEADS;

    if (val) {
      isDisplayingHeads = true;
      return EnumChoice.HEADS;
    } else {
      isDisplayingHeads = false;
      return EnumChoice.TAILS;
    }
  }

  /**
   * Translates current coin face , resulting coin face transition
   * to flips value. Then this value is used to animate X coin flips.
   * @return int X coin flips
   */
  private int randomFlips() {

    if (result == EnumChoice.HEADS) {
      if (wasDisplayingHeads) {
        // result = HEADS , previous result = HEADS , saved result = HEADS
        return 2;
      } else {
        // result = HEADS , previous result = TAILS , saved result = HEADS
        return swapFlips;
      }
    } else if (result == EnumChoice.TAILS) {
      if (wasDisplayingHeads) {
        // result = TAILS , previous result = HEADS , saved result = TAILS
        return swapFlips;
      } else {
        // result = TAILS , previous result = TAILS , saved result = TAILS
        return 2;
      }

    }
    return 0;//possible bug
  }

  /**
   * main screen updater also handles current strike animations (eg H-T-H-T-T-T-H)
   */
  private void updateScreen() {
    TextView scoreTV = findViewById(R.id.score);
    scoreTV.setText(String.valueOf(score));
    TextView highScoreTV = findViewById(R.id.highScore);
    highScoreTV.setText(String.valueOf(highScore));
    final HorizontalScrollView hsv = findViewById(R.id.strikesScrollView);
    //
    LinearLayout linearLayout = findViewById(R.id.strikesLayout);

    linearLayout.removeAllViewsInLayout();
    if (strikes.size() > 0) {
      //thanks to https://stackoverflow.com/a/4720563/9301923
      hsv.postDelayed(() -> hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT), 100L);
      if (strikes.size() == 1) {
        //play animation
        ImageView image = new ImageView(this);
        image.setScaleType(ScaleType.CENTER);
        image.setAdjustViewBounds(true);
        if (strikes.get(0).equals("H")) {
          image.setImageDrawable(getResources().getDrawable(R.drawable.heads));
        } else {
          image.setImageDrawable(getResources().getDrawable(R.drawable.tails));
        }
        linearLayout.addView(image);

        TranslateAnimation r2l = new TranslateAnimation(1500.0f, 0.0f, 0.0f,
            0.0f);
        r2l.setDuration(650);
        r2l.setFillAfter(true);
        image.startAnimation(r2l);
      } else {
        for (int i = 0; i <= strikes.size() - 2; i++) {
          ImageView image = new ImageView(this);
          image.setScaleType(ScaleType.CENTER);
          image.setAdjustViewBounds(true);
          if (strikes.get(i).equals("H")) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.heads));
          } else {
            image.setImageDrawable(getResources().getDrawable(R.drawable.tails));
          }
          linearLayout.addView(image);
        }
        ImageView image = new ImageView(this);
        image.setScaleType(ScaleType.CENTER);
        image.setAdjustViewBounds(true);
        if (strikes.get(strikes.size() - 1).equals("H")) {
          image.setImageDrawable(getResources().getDrawable(R.drawable.heads));
        } else {
          image.setImageDrawable(getResources().getDrawable(R.drawable.tails));
        }
        linearLayout.addView(image);
        //play animation
        TranslateAnimation r2l = new TranslateAnimation(1500.0f, 0.0f, 0.0f,
            0.0f);
        r2l.setDuration(650);
        r2l.setFillAfter(true);
        image.startAnimation(r2l);
      }
    } else {
      linearLayout.removeAllViews();
    }

    Log.v("MainActivity", "Update screen was called");
  }

  /**
   * gets called from flip Listener on end of animation to produce results on screen
   * after the animation ends. Calls updateScreen()
   * Handles win/lose or highscore and produces toast in case of the latter
   */
  public void makeResult() {

    if (result == choice) {
      //win
      score++;
      if (soundEnabled) {
        scoreSound.start();
      }

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
        if (soundEnabled) {
          highScoreSound.start();
        }

        Toast tag = Toast.makeText(this, R.string.highScoreToast, Toast.LENGTH_SHORT);
        killAllToast();
        toastDatabase.add(tag); // for kill
        tag.show();
        newHighscore = false;
      }
    }
    updateScreen();
  }

  /**
   * Method called at every prediction of the coin. > Meaning once every time
   * you click heads/tails button
   * Adjusts the update Listener class with a new Object with the right parameters
   * Also sets the floatValues on animator , meaning more flips on 2nd parameter will result
   * in more animator values thus more rotations etc..
   * Change the 2nd param to get more flips in a predefined duration 1.5 at this moment of compile
   * @param flips int flips produced by randomFlips()
   */
  private void setupAnimation(int flips) {
    mFlipAnimator.setFloatValues(0f, (float) flips);

    if (wasDisplayingHeads) {
      mFlipAnimator.addUpdateListener(new FlipListener(headsView, tailsView, flips));
    } else {
      mFlipAnimator.addUpdateListener(new FlipListener(tailsView, headsView, flips));
    }
  }

  //https://stackoverflow.com/questions/7414065/android-scale-animation-on-view

  /**
   * Method called every time in onCreate or init of app.
   * Handles the scaling animation of the coin when it pops up on screen
   * Same logic as above method, adjusts update listener
   */
  private void setupScaleAnimation() {
    scaleAnimator.setFloatValues(0f, 1f);

    if (isDisplayingHeads) {
      scaleAnimator.addUpdateListener(new ScaleListener(headsView, tailsView));
    } else {
      scaleAnimator.addUpdateListener(new ScaleListener(tailsView, headsView));
    }
    scaleAnimator.start();
  }

  private enum EnumChoice {
    HEADS,
    TAILS
  }
}
