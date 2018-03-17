package com.philip.coin_toss;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private EnumChoice choice;
    private EnumChoice result;
    private Random rand=new Random();
    public enum EnumChoice {
        HEADS,
        TAILS
    }
    private static final String SCORE_KEY = "SCORE";
    private static final String HIGHSCORE_KEY = "HIGHSCORE";
    private static final String STRIKES_KEY = "STRIKES";
    private static final String COIN_FACE_KEY = "COIN_FACE";
    private int score=0;
    private int highScore=0;
    private ArrayList<String> strikes=new ArrayList<>();

    public ImageView headsView;
    public ImageView tailsView;
    public Button headsButton;
    public Button tailsButton;

    private boolean isDisplayingHeads = true;
    private EnumChoice prevDisplay = EnumChoice.HEADS;
    private boolean isPrevDisplayHeads;

    private ValueAnimator mFlipAnimator;
    private int flips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headsView = (ImageView) findViewById(R.id.headsViewXML);
        tailsView = (ImageView) findViewById(R.id.tailsViewXML);
        headsButton = (Button) findViewById(R.id.heads);
        tailsButton = (Button) findViewById(R.id.tails);


        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        mFlipAnimator.addListener(new FlipListenerEnd(this ));
        mFlipAnimator.setDuration(2500);

        if (savedInstanceState != null) {

            score = savedInstanceState.getInt(SCORE_KEY);
            highScore = savedInstanceState.getInt(HIGHSCORE_KEY);
            strikes = new ArrayList<>(savedInstanceState.getStringArrayList(STRIKES_KEY));
            isPrevDisplayHeads = savedInstanceState.getBoolean(COIN_FACE_KEY);

            if (isPrevDisplayHeads) {
                prevDisplay = EnumChoice.HEADS;
                isPrevDisplayHeads = true;
                mFlipAnimator.addUpdateListener(new FlipListener(headsView, tailsView, flips));
                tailsView.setVisibility(View.GONE);
                headsView.setVisibility(View.VISIBLE);
            } else {
                prevDisplay = EnumChoice.TAILS;
                isPrevDisplayHeads = false;
                mFlipAnimator.addUpdateListener(new FlipListener(tailsView, headsView, flips));
                headsView.setVisibility(View.GONE);
                tailsView.setVisibility(View.VISIBLE);
            }


            Toast.makeText(this, "orientation change", Toast.LENGTH_SHORT).show();
            updateScreen();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putInt(SCORE_KEY,score);
        bundle.putInt(HIGHSCORE_KEY,highScore);
        bundle.putStringArrayList(STRIKES_KEY,strikes);
        bundle.putBoolean(COIN_FACE_KEY,isDisplayingHeads);
    }

    public void update(View view){
        result = result(rand.nextBoolean());
        flips = rollNumOfLoops();
        System.out.println( flips + " Flips");

        mFlipAnimator.setFloatValues(0f, (float) flips);
        if(prevDisplay == EnumChoice.HEADS){
            mFlipAnimator.addUpdateListener(new FlipListener(headsView, tailsView, flips));
            tailsView.setVisibility(View.GONE);
            headsView.setVisibility(View.VISIBLE);
        }else{
            mFlipAnimator.addUpdateListener(new FlipListener(tailsView, headsView, flips));
            headsView.setVisibility(View.GONE);
            tailsView.setVisibility(View.VISIBLE);
        }


        if (view.getId() == R.id.heads) {

            choice = EnumChoice.HEADS;
        }
        else if (view.getId() == R.id.tails) {

            choice = EnumChoice.TAILS;
        }

        mFlipAnimator.start();

    }

    public EnumChoice result(boolean val){
        if(isDisplayingHeads){
            prevDisplay = EnumChoice.HEADS;
            isPrevDisplayHeads = true;
        }
        else{
            prevDisplay = EnumChoice.TAILS;
            isPrevDisplayHeads = false;
        }
        if(val){
            isDisplayingHeads = true;
            return EnumChoice.HEADS;
        }else{
            isDisplayingHeads = false;
            return EnumChoice.TAILS;
        }
    }

    private int rollNumOfLoops(){
        System.out.println("Previous result was: "+prevDisplay.toString() +". New result should be (HEADS): " + isDisplayingHeads);
        if(result == EnumChoice.HEADS){
            if(prevDisplay == EnumChoice.HEADS){
                return 2*(rand.nextInt(3) +1 );
            }
            else if(prevDisplay == EnumChoice.TAILS){
                return 2*(rand.nextInt(3) +1 ) + 1;
            }
        }else if (result == EnumChoice.TAILS){
            if(prevDisplay == EnumChoice.HEADS){
                return 2*(rand.nextInt(3) +1 ) + 1;
            }else if(prevDisplay == EnumChoice.TAILS) {
                return 2 * (rand.nextInt(3) + 1);
            }
        }
        return 0;
        //return (rand.nextInt(5) + 2);
    }

    public void updateScreen(){

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
        System.out.println("RESULT: "+result.toString());
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
    }
}
