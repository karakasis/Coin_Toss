package com.philip.coin_toss;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.DrawableThumbnailImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
*/

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private EnumChoice choice;
    private EnumChoice result;
    private Random rand=new Random();;
    public enum EnumChoice {
        HEADS,
        TAILS
    }
    private static final String SCORE_KEY = "SCORE";
    private static final String HIGHSCORE_KEY = "HIGHSCORE";
    private static final String STRIKES_KEY = "STRIKES";
    private int score=0;
    private int highScore=0;
    private ArrayList<String> strikes=new ArrayList<>();

    private ImageView headsView;
    private ImageView tailsView;

    private boolean isDisplayingHeads = true;
    private EnumChoice prevDisplay = EnumChoice.HEADS;

    ValueAnimator mFlipAnimator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlipAnimator = ValueAnimator.ofFloat(0f, 5f);

        headsView = (ImageView) findViewById(R.id.headsViewXML);
        tailsView = (ImageView) findViewById(R.id.tailsViewXML);

        mFlipAnimator.addUpdateListener(new FlipListener(headsView, tailsView));
        mFlipAnimator.setDuration(18000);


        tailsView.setVisibility(View.GONE);

        if (savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY);
            highScore = savedInstanceState.getInt(HIGHSCORE_KEY);
            strikes = new ArrayList<>(savedInstanceState.getStringArrayList(STRIKES_KEY));
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
    }

    public void update(View view){
        result = result(rand.nextBoolean());
        //rollNumOfLoops();

        if (view.getId() == R.id.heads) {

            choice = EnumChoice.HEADS;
        }
        else if (view.getId() == R.id.tails) {

            choice = EnumChoice.TAILS;
        }

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
        mFlipAnimator.start();
        updateScreen();
    }

    public EnumChoice result(boolean val){
        if(isDisplayingHeads) prevDisplay = EnumChoice.HEADS;
        else prevDisplay = EnumChoice.TAILS;
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
                return 2*(rand.nextInt(3)+1) + 1;
            }
            else if(prevDisplay == EnumChoice.TAILS){
                return 2*(rand.nextInt(3)+1);
            }
        }else if (result == EnumChoice.TAILS){
            if(prevDisplay == EnumChoice.HEADS){
                return 2*(rand.nextInt(3)+1);
            }else if(prevDisplay == EnumChoice.TAILS) {
                return 2 * (rand.nextInt(3) + 1) + 1;
            }
        }
        return 0;
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
            textView.setTextAppearance(this,R.style.AppTheme);
            linearLayout.addView(textView);
        }
        Log.v("MainActivity", "Update screen was called");
    }
}
