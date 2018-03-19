package com.philip.coin_toss;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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
    private Random rand=new Random();
    private enum EnumChoice {
        HEADS,
        TAILS
    }
    private static final String SCORE_KEY = "SCORE";
    private static final String HIGHSCORE_KEY = "HIGHSCORE";
    private static final String STRIKES_KEY = "STRIKES";
    private static final String WAS_HEADS_KEY = "WAS_HEADS";
    private static final String IS_HEADS_KEY = "IS_HEADS";
    private int score=0;
    private int highScore=0;
    private ArrayList<String> strikes=new ArrayList<>();

    public ImageView headsView;
    public ImageView tailsView;
    public Button headsButton;
    public Button tailsButton;

    private boolean wasDisplayingHeads = true;
    private boolean isDisplayingHeads = true;

    private ValueAnimator mFlipAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headsView = (ImageView) findViewById(R.id.headsViewXML);
        tailsView = (ImageView) findViewById(R.id.tailsViewXML);
        headsButton = (Button) findViewById(R.id.heads);
        tailsButton = (Button) findViewById(R.id.tails);

        //First run will initialize animation
        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        mFlipAnimator.addListener(new FlipListenerEnd(this ));
        mFlipAnimator.setDuration(2500);

        if (savedInstanceState != null) {
            Toast.makeText(this, "orientation change", Toast.LENGTH_SHORT).show();

            //load bundle
            score = savedInstanceState.getInt(SCORE_KEY);
            highScore = savedInstanceState.getInt(HIGHSCORE_KEY);
            strikes = new ArrayList<>(savedInstanceState.getStringArrayList(STRIKES_KEY));
            isDisplayingHeads = savedInstanceState.getBoolean(IS_HEADS_KEY);
            wasDisplayingHeads = savedInstanceState.getBoolean(WAS_HEADS_KEY);

            if(isDisplayingHeads){
                result = EnumChoice.HEADS;
                headsView.setVisibility(View.VISIBLE);
                tailsView.setVisibility(View.GONE);
            }else{
                result = EnumChoice.TAILS;
                tailsView.setVisibility(View.VISIBLE);
                headsView.setVisibility(View.GONE);
            }

            updateScreen();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putInt(SCORE_KEY,score);
        bundle.putInt(HIGHSCORE_KEY,highScore);
        bundle.putStringArrayList(STRIKES_KEY,strikes);
        bundle.putBoolean(WAS_HEADS_KEY,wasDisplayingHeads);
        bundle.putBoolean(IS_HEADS_KEY,isDisplayingHeads);
    }

    public void update(View view){
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

    private EnumChoice result(boolean val){
        if(result == EnumChoice.HEADS) // previous roll
        {
            wasDisplayingHeads = true;
        }else{
            wasDisplayingHeads = false;
        }

        if(val){
            isDisplayingHeads = true;
            return EnumChoice.HEADS;
        }else{
            isDisplayingHeads = false;
            return EnumChoice.TAILS;
        }
    }

    private int randomFlips(){
        if(result == EnumChoice.HEADS){
            if(wasDisplayingHeads){
                // result = HEADS , previous result = HEADS , saved result = HEADS
                return 2*(rand.nextInt(3) +1 );
            }
            else{
                // result = HEADS , previous result = TAILS , saved result = HEADS
                return 2*(rand.nextInt(3) +1 ) + 1;
            }
        }else if (result == EnumChoice.TAILS){
            if(wasDisplayingHeads){
                // result = TAILS , previous result = HEADS , saved result = TAILS
                return 2*(rand.nextInt(3) +1 ) + 1;
            }else{
                // result = TAILS , previous result = TAILS , saved result = TAILS
                return 2 * (rand.nextInt(3) + 1);
            }
        }
        return 0;//possible bug
    }

    private void updateScreen(){

        TextView scoreTV = (TextView) findViewById(R.id.score);
        scoreTV.setText(String.valueOf(score));
        TextView highScoreTV = (TextView) findViewById(R.id.highScore);
        highScoreTV.setText(String.valueOf(highScore));
        //
        LinearLayout linearLayout =(LinearLayout) findViewById(R.id.strikesLayout) ;
        linearLayout.removeAllViewsInLayout();
        for(String ch : strikes){
            TextView textView = new TextView(this);
            textView.setText(ch + " ");
            if(ch.equals("H")){
                textView.setTextAppearance(this,R.style.StrikeHApp);
            }else{
                textView.setTextAppearance(this,R.style.StrikeTApp);
            }
            linearLayout.addView(textView);
        }
        Log.v("MainActivity", "Update screen was called");
    }

    public void makeResult(){
        
        if(result == choice){
            //win
            score++;
            if(score>highScore){
                highScore = score;

                Toast.makeText(this, "New High Score !", Toast.LENGTH_SHORT).show();
            }
            strikes.add(choice.toString().charAt(0) + "");
        }else{
            //lose
            score = 0;
            strikes.clear();
        }
        updateScreen();
    }

    private void setupAnimation(int flips){
        mFlipAnimator.setFloatValues(0f, (float) flips);

        if(wasDisplayingHeads){
            mFlipAnimator.addUpdateListener(new FlipListener(headsView, tailsView, flips));
        }else{
            mFlipAnimator.addUpdateListener(new FlipListener(tailsView, headsView, flips));
        }
    }
}
