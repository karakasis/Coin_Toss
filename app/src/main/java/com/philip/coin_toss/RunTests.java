package com.philip.coin_toss;

import java.util.Random;

/**
 * Created by Xrhstos on 3/19/2018.
 */

public class RunTests {

    private static int headsCount = 0;
    private static int tailsCount = 0;
    private static EnumResult result;
    private static boolean wasDisplayingHeads = true;
    private static boolean isDisplayingHeads = true;
    private static final Random rand=new Random();

    private enum EnumResult {
        HEADS,
        TAILS
    }

    public RunTests(){
        headsCount = 0;
        tailsCount = 0;

        simulate(10000);
    }

    private static void simulate(int num){

        for(int i=0; i<num; i++){

            result = result(rand.nextBoolean());
        }
        System.out.println("Heads: " + headsCount + " \n Tails: "+ tailsCount);
        //setupAnimation(randomFlips());
    }

    private static EnumResult result(boolean val){
        // previous roll
        wasDisplayingHeads = result == EnumResult.HEADS;

        if(val){
            headsCount++;
            isDisplayingHeads = true;
            return EnumResult.HEADS;
        }else{
            tailsCount++;
            isDisplayingHeads = false;
            return EnumResult.TAILS;
        }
    }

    private int randomFlips(){
        if(result == EnumResult.HEADS){
            if(wasDisplayingHeads){
                // result = HEADS , previous result = HEADS , saved result = HEADS
                return 2*(rand.nextInt(3) +1 );
            }
            else{
                // result = HEADS , previous result = TAILS , saved result = HEADS
                return 2*(rand.nextInt(3) +1 ) + 1;
            }
        }else if (result == EnumResult.TAILS){
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

}
