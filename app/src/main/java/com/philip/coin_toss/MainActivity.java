package com.philip.coin_toss;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.media.Image;
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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

  //dev
  private boolean firstExecution = true;
  private boolean devActive = false;
  private boolean cheatActive = false;
  private boolean cheatResult;
  private boolean plusFlips = false;
  public static boolean alternateFlipEffect = false;
  public static boolean disableScaleFlipEffect = false;

  private static final String DEV_KEY = "DEV";
  private static final String CHEAT_KEY = "CHEAT";
  private static final String PLUS_FLIP_KEY = "PLUS_FLIP";
  private static final String ALT_FLIP_KEY = "ALT_FLIP";
  private static final String DIS_SCALE_FLIP_KEY = "DIS_SCALE_FLIP";
  private static final String CHEAT_RESULT_KEY = "CHEAT_RESULT";


  private CheckBox devBox;
  private CheckBox cheatBox;
  private CheckBox plusFlipBox;
  private CheckBox altFlipBox;
  private CheckBox disScaleFlipBox;

  //dev
  private int swapFlips = 1;

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


  public void devMode(View view) {
    if (devBox.isChecked()) {
      devActive = true;
      cheatBox.setVisibility(View.VISIBLE);
      plusFlipBox.setVisibility(View.VISIBLE);
      altFlipBox.setVisibility(View.VISIBLE);
      disScaleFlipBox.setVisibility(View.VISIBLE);
      if (cheatActive) {
        cheatMode(null);
      }
      if (plusFlips) {
        plusFlipMode(null);
      }
      if (alternateFlipEffect) {
        altFlipMode(null);
      }
      if (disableScaleFlipEffect) {
        disScaleFlipMode(null);
      }
    } else {
      devActive = false;
      cheatBox.setChecked(false);
      cheatMode(null);
      cheatBox.setVisibility(View.GONE);

      plusFlipBox.setChecked(false);
      plusFlipMode(null);
      plusFlipBox.setVisibility(View.GONE);

      altFlipBox.setChecked(false);
      altFlipMode(null);
      altFlipBox.setVisibility(View.GONE);

      disScaleFlipBox.setChecked(false);
      disScaleFlipMode(null);
      disScaleFlipBox.setVisibility(View.GONE);
    }
  }

  public void cheatMode(View view) {
    cheatActive = cheatBox.isChecked();
    if (cheatActive && view != null) {

      Toast.makeText(this, "Correct choice"
          + " will now be displayed with a RED border", Toast.LENGTH_LONG).show();
    } else if (!cheatActive && view != null) {

      Toast.makeText(this, "Cheat mode"
          + " disabled. Borders are default color", Toast.LENGTH_LONG).show();
    }
    if (cheatActive) {
      showCheatResult();
    } else {
      tailsButton.setBackgroundResource(R.drawable.buttonshape);
      headsButton.setBackgroundResource(R.drawable.buttonshape);
    }
  }

  private void showCheatResult() {
    if (cheatActive) {
      if (cheatResult == true) // HEADS
      {
        headsButton.setBackgroundResource(R.drawable.buttonshape_cheat);
        tailsButton.setBackgroundResource(R.drawable.buttonshape);
      } else {
        tailsButton.setBackgroundResource(R.drawable.buttonshape_cheat);
        headsButton.setBackgroundResource(R.drawable.buttonshape);
      }
    } else {
      tailsButton.setBackgroundResource(R.drawable.buttonshape);
      headsButton.setBackgroundResource(R.drawable.buttonshape);
    }

  }

  public void plusFlipMode(View view) {
    plusFlips = plusFlipBox.isChecked();
    if (plusFlips && view != null) {
      swapFlips = 3;
      Toast.makeText(this, "Swapping sides will"
          + " now be animated with 3 flips", Toast.LENGTH_LONG).show();
    } else if (!plusFlips && view != null) {
      swapFlips = 1;
      Toast.makeText(this, "Swapping sides will"
          + " now be animated with 1 flip", Toast.LENGTH_LONG).show();
    }
    if (plusFlips && view == null) {
      swapFlips = 3;
    } else if (!plusFlips && view == null) {
      swapFlips = 1;
    }
  }

  public void altFlipMode(View view) {
    MainActivity.alternateFlipEffect = altFlipBox.isChecked();
    if (MainActivity.alternateFlipEffect && view != null) {
      Toast.makeText(this, "The coin will now"
          + " flip with modified animation (experimental)", Toast.LENGTH_LONG).show();
    } else if (!MainActivity.alternateFlipEffect && view != null) {
      Toast.makeText(this, "The coin will now"
          + " flip with default animation", Toast.LENGTH_LONG).show();
    }
  }

  public void disScaleFlipMode(View view) {
    MainActivity.disableScaleFlipEffect = disScaleFlipBox.isChecked();
    if (MainActivity.disableScaleFlipEffect && view != null) {
      Toast.makeText(this, "The coin will now"
          + " flip without scaling animation (also ++ flips)", Toast.LENGTH_LONG).show();
    } else if (!MainActivity.disableScaleFlipEffect && view != null) {
      Toast.makeText(this, "The coin will now"
          + " flip with preset scale animation", Toast.LENGTH_LONG).show();
    }
  }

  private void updateCheckboxState() {
    devBox.setChecked(devActive);
    cheatBox.setChecked(cheatActive);
    plusFlipBox.setChecked(plusFlips);
    altFlipBox.setChecked(alternateFlipEffect);
    disScaleFlipBox.setChecked(disableScaleFlipEffect);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    headsView = (ImageView) findViewById(R.id.headsViewXML);
    tailsView = (ImageView) findViewById(R.id.tailsViewXML);
    headsButton = (Button) findViewById(R.id.heads);
    tailsButton = (Button) findViewById(R.id.tails);
    //dev
    if (firstExecution) {
      cheatResult = rand.nextBoolean();
      firstExecution = false;
    }
    devBox = (CheckBox) findViewById(R.id.devBox);
    cheatBox = (CheckBox) findViewById(R.id.cheatBox);
    plusFlipBox = (CheckBox) findViewById(R.id.plusFlipBox);
    altFlipBox = (CheckBox) findViewById(R.id.altFlipBox);
    disScaleFlipBox = (CheckBox) findViewById(R.id.disScaleFlipBox);

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
      //dev
      firstExecution = savedInstanceState.getBoolean("first");
      devActive = savedInstanceState.getBoolean(DEV_KEY);
      cheatActive = savedInstanceState.getBoolean(CHEAT_KEY);
      plusFlips = savedInstanceState.getBoolean(PLUS_FLIP_KEY);
      alternateFlipEffect = savedInstanceState.getBoolean(ALT_FLIP_KEY);
      disableScaleFlipEffect = savedInstanceState.getBoolean(DIS_SCALE_FLIP_KEY);
      cheatResult = savedInstanceState.getBoolean(CHEAT_RESULT_KEY);
      updateCheckboxState();

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

    //dev
    devMode(devBox);

    //dev

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
    //dev
    bundle.putBoolean("first", firstExecution);
    bundle.putBoolean(DEV_KEY, devActive);
    bundle.putBoolean(CHEAT_KEY, cheatActive);
    bundle.putBoolean(PLUS_FLIP_KEY, plusFlips);
    bundle.putBoolean(ALT_FLIP_KEY, alternateFlipEffect);
    bundle.putBoolean(DIS_SCALE_FLIP_KEY, disableScaleFlipEffect);
    bundle.putBoolean(CHEAT_RESULT_KEY, cheatResult);
  }

  public void update(View view) {

    if (!cheatActive) { //dev option
      result = result(rand.nextBoolean());
    } else {
      result = result(cheatResult);
    }

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
        if (MainActivity.disableScaleFlipEffect) {
          return 2 * (rand.nextInt(3) + 1);
        } else {
          return 2;
        }
      } else {
        // result = HEADS , previous result = TAILS , saved result = HEADS
        if (MainActivity.disableScaleFlipEffect) {
          return 2 * (rand.nextInt(3) + 1) + 1;
        } else {
          return swapFlips;
        }
      }
    } else if (result == EnumChoice.TAILS) {
      if (wasDisplayingHeads) {
        // result = TAILS , previous result = HEADS , saved result = TAILS
        if (MainActivity.disableScaleFlipEffect) {
          return 2 * (rand.nextInt(3) + 1) + 1;
        } else {
          return swapFlips;
        }
      } else {
        // result = TAILS , previous result = TAILS , saved result = TAILS
        if (MainActivity.disableScaleFlipEffect) {
          return 2 * (rand.nextInt(3) + 1);
        } else {
          return 2;
        }
      }
    }
    return 0;//possible bug
  }

  private void updateScreen() {
    //dev - changes result with every phone flip, but doesn't matter
    cheatResult = rand.nextBoolean();
    showCheatResult();

    TextView scoreTV = (TextView) findViewById(R.id.score);
    scoreTV.setText(String.valueOf(score));
    TextView highScoreTV = (TextView) findViewById(R.id.highScore);
    highScoreTV.setText(String.valueOf(highScore));
    //
    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.strikesLayout);

    linearLayout.removeAllViewsInLayout();
    if (strikes.size() > 0) {
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
    }else{
      linearLayout.removeAllViews();
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
