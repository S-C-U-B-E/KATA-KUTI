package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.kata_kuti.WinningLogic.theWinner;


public class MainActivity extends AppCompatActivity {

    //To count the number of clicks on Cells
     private static int clickCount;

    //To mark the cells already clicked
     private static ArrayList<Integer> listOfCellsAlreadySet ;

    //To determine if there is any valid winner after every move
     private static boolean foundWinner;

    //To mark the choice of cells opted by individual players
     public static List<Integer> Player1 ;
     public static List<Integer> Player2 ;

    //New Thing learnt (to use instead of HashMap) for an easy use of the CellIds
     public static SparseIntArray cellMap;

    //To store all the possible sets of cells required to Win
     static ArrayList<List<Integer>> winningSet;

    //Stores the choice of the number of rounds the players wanted to play
    //Strores the current Round Number
     private static int mRound = 1;
     private static int mCurrentRound ;

    //choice List
     private Spinner mChoiceList;

    //Start Button , Next Round Button
     private Button mButtonStart;
     private ImageButton mButtonNext;
     private ImageButton mButtonRestart;

    //To display the current Onging round
     private String mGameRoundMessage;

    //To store scores of players in a match.
     private int mScorePlayer1;
     private int mScorePlayer2;
     private String mStringScorePlayer1;
     private String mStringScorePlayer2;

    //To Handle Back Press
    private boolean mIsMatchInProgress;

    //To set Audio Media
    private static MediaPlayer mMediaPlayer;
    private static MediaPlayer mButtonMediaplayer;
    private static MediaPlayer mGameButtonsMediaPlayer;

     TextView gameResultMessageBox,gameRoundMessageBox,gameScorePlayer1,gameScorePlayer2,textViewPlayer1,textViewPlayer2;
     ImageView cell00,cell01,cell02,cell10,cell11,cell12,cell20,cell21,cell22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playAudioBeforeStart();


        textViewPlayer1 = findViewById(R.id.textviewplayer1);
        textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        textViewPlayer2 = findViewById(R.id.textviewplayer2);
        textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME


        gameResultMessageBox = findViewById(R.id.textviewgameresultmessage);
        gameRoundMessageBox = findViewById(R.id.textviewgameroundmessage);

        gameScorePlayer1 = findViewById(R.id.textviewscore1);
        gameScorePlayer2 = findViewById(R.id.textviewscore2);


        mButtonStart = findViewById(R.id.buttonstart);
        mButtonNext = findViewById(R.id.buttonnext);
        mButtonRestart = findViewById(R.id.buttonrestart);


        mChoiceList = (Spinner) findViewById(R.id.rounds);

        cellMap = new SparseIntArray();
        winningSet = new ArrayList<>();

        initialSetupBeforeEveryMatch(); // Clearing The scores rounds and text values i.e. setting the game field fresh
                                        // for every individual Match.
        setupSpinner();


        /*
        * All the possibles set of cells to win
        * */
        setupWinnigSet();


        /*
        * all cells found by Ids
        * */
        setupCellsById();

         /*
         * to ease the use of Id .. to find the winner
         * */
         setupCellMap();


        mButtonMediaplayer = MediaPlayer.create(MainActivity.this,R.raw.button_clicks);

        /*
         * Click on Start Button to start the game XD
         * */
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playButtonAudio();
                mButtonMediaplayer.start();
                startGame();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playButtonAudio();
                mButtonMediaplayer.start();
                startGame();
            }
        });

        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playButtonAudio();
                mButtonMediaplayer.start();
                restartGame();
            }
        });

    }

    private void setupCellsById(){
        cell00 = findViewById(R.id.cell00);
        cell01 = findViewById(R.id.cell01);
        cell02 = findViewById(R.id.cell02);


        cell10 = findViewById(R.id.cell10);
        cell11 = findViewById(R.id.cell11);
        cell12 = findViewById(R.id.cell12);


        cell20 = findViewById(R.id.cell20);
        cell21 = findViewById(R.id.cell21);
        cell22 = findViewById(R.id.cell22);
    }

    private void setupWinnigSet(){
        winningSet.add(Arrays.asList(1,2,3));
        winningSet.add(Arrays.asList(4,5,6));
        winningSet.add(Arrays.asList(7,8,9));

        winningSet.add(Arrays.asList(1,4,7));
        winningSet.add(Arrays.asList(2,5,8));
        winningSet.add(Arrays.asList(3,6,9));

        winningSet.add(Arrays.asList(1,5,9));
        winningSet.add(Arrays.asList(3,5,7));
    }

    private void setupCellMap(){
        cellMap.put(R.id.cell00,1);
        cellMap.put(R.id.cell01,2);
        cellMap.put(R.id.cell02,3);


        cellMap.put(R.id.cell10,4);
        cellMap.put(R.id.cell11,5);
        cellMap.put(R.id.cell12,6);


        cellMap.put(R.id.cell20,7);
        cellMap.put(R.id.cell21,8);
        cellMap.put(R.id.cell22,9);
    }

    private void setupSpinner(){
        // Created an ArrayAdapter using the string array and a default mChoiceList layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rounds_array,R.layout.spinner_item);
        // the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // the adapter to the mChoiceList XD
        mChoiceList.setAdapter(adapter);


        // Set the integer mSelected to the constant values
        mChoiceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    switch (selection) {
                        case "1":
                            mRound = 1;
                            break;
                        case "2":
                            mRound = 2;
                            break;
                        case "3":
                            mRound = 3;
                            break;
                        default:
                            mRound = 4;
                            break;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRound = 1;
            }
        });
    }

    private void setupOnClickListeners(){

        /*
          Click Listeners for the first row Cells
         */
        cell00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
        cell01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
        cell02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });


        /*
          Click Listeners for the middle row Cells
         */
        cell10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
        cell11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
        cell12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });

        /*
         Click Listeners for the last row Cells
         */
        cell20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
        cell21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
        cell22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay(view);
            }
        });
    }

    /*
    * Very important to disable the listeners.. as
    * I don't want the game to start before selecting rounds at the begining of every Match. :D
    * */
    private void disableOnClickListeners(){

         /*
          DISABLE Listeners for the first row Cells
         */
        cell00.setOnClickListener(null);
        cell01.setOnClickListener(null);
        cell02.setOnClickListener(null);


         /*
          DISABLE Listeners for the first row Cells
         */
        cell10.setOnClickListener(null);
        cell11.setOnClickListener(null);
        cell12.setOnClickListener(null);


         /*
          DISABLE Listeners for the first row Cells
         */
        cell20.setOnClickListener(null);
        cell21.setOnClickListener(null);
        cell22.setOnClickListener(null);

    }

    private void initialSetupBeforeEveryMatch(){

        mIsMatchInProgress = false;

        mCurrentRound = 0;
        mScorePlayer1 = 0;
        mScorePlayer2 = 0;

        gameScorePlayer2.setTextSize(TypedValue.COMPLEX_UNIT_SP,72);
        gameScorePlayer1.setTextSize(TypedValue.COMPLEX_UNIT_SP,72);

        mStringScorePlayer1  = mScorePlayer1+"";
        mStringScorePlayer2 = mScorePlayer2+"";
        gameScorePlayer1.setText(mStringScorePlayer1); //To display an initial score of 0
        gameScorePlayer2.setText(mStringScorePlayer2); //To display an initial score of 0

        /*
         * Initially (Before selection of mRound and start of game) Gamee Round text box is disabeled.
         * */
        gameRoundMessageBox.setVisibility(View.GONE);
        mButtonNext.setVisibility(View.GONE);
        gameResultMessageBox.setText("");
        mButtonRestart.setVisibility(View.GONE);

    }

    private void gamePlay(View view){

        ImageView imageView = findViewById(view.getId());
         textViewPlayer1 = findViewById(R.id.textviewplayer1);
         textViewPlayer2 = findViewById(R.id.textviewplayer2);

        if( !listOfCellsAlreadySet.contains(view.getId()) && !foundWinner){ // To check if the cell has been clicked already or not.
            clickCount++;
            /*
            * clickCount will decide player 1 OR 2 based on odd-even logic
            * */
            if(clickCount%2 == 0){
                Player2.add(cellMap.get(view.getId()));
                imageView.setImageResource(R.drawable.kuti);
                textViewPlayer2.setBackgroundColor(Color.parseColor("#757575")); // SET TO scorerowfield color DENOTING 2 HAS GIVEN ITS CHOICE
                textViewPlayer1.setBackgroundColor(Color.parseColor("#424242"));/*SET TO gameplayfield color DENOTING 1's TURN NEXT*/ }
            else{
                Player1.add(cellMap.get(view.getId()));
                imageView.setImageResource(R.drawable.kata);
                textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));// SET TO scorerowfield color DENOTING 1 HAS GIVEN ITS CHOICE
                textViewPlayer2.setBackgroundColor(Color.parseColor("#424242"));/*SET TO gameplayfield color DENOTING 2's TURN NEXT*/}

            listOfCellsAlreadySet.add(view.getId()); // Once Clicked the value of the cell can't be changed

            if(clickCount > 3)foundWinner = theWinner(clickCount);
            if(foundWinner){ //Logics after a winner is found
                if(clickCount%2 == 0)
                {
                    mScorePlayer2++;
                    gameResultMessageBox.setText("PLAYER 2 WON");
                    textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));
                    textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));
                }else {
                    mScorePlayer1++;
                    gameResultMessageBox.setText("PLAYER 1 WON");
                    textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));
                    textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));
                    }
                if(mCurrentRound<mRound){
                    mButtonNext.setVisibility(View.VISIBLE);
                }else{
                    mButtonRestart.setVisibility(View.VISIBLE);
                    mIsMatchInProgress = false;
                    displayFinalWinner();
                }

                /*
                 * To display the scores in the Score Display Box
                 * */
                displayScores();

            }else if(clickCount == 9){ // What if no winner is found AND all cells are set.. Boom It's a TIE
                /*All cells have been set but winner is still not found
                 *Logic for handling Tie
                 * */
                //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                gameResultMessageBox.setText("IT'S A TIE");
                textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));
                mScorePlayer2+=0;//No increment of score on tie
                mScorePlayer1+=0;//No increment of score on tie
                if(mCurrentRound<mRound){
                    mButtonNext.setVisibility(View.VISIBLE);
                }else{
                    mButtonRestart.setVisibility(View.VISIBLE);
                    mIsMatchInProgress = false;
                    displayFinalWinner();
                }

                /*
                * To display the scores in the Score Display Box
                * */
                displayScores();
            }

        }

        /*if(mCurrentRound == mRound){
            displayFinalWinner();
        }*/
    }

    private void startGame(){
        playAudio();
        refreshTheCellsForNextRound();

        TextView textViewGameRoundMessage;
        textViewPlayer1 = findViewById(R.id.textviewplayer1);
        textViewPlayer1.setBackgroundColor(Color.parseColor("#424242"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        textViewGameRoundMessage = findViewById(R.id.textviewgameroundmessage);

        mChoiceList.setEnabled(false);

        gameRoundMessageBox.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.GONE);//No need of the start button after the game has started
        mButtonNext.setVisibility(View.GONE);//Next Round button only required after completion of a round
        mButtonRestart.setVisibility(View.GONE);//No Reset option available after Match starts

        setupOnClickListeners();

            mCurrentRound++; // Update Current Round

        /*
        * Initialize all the variables to their initials
        * for individual Rounds
        * */
            mIsMatchInProgress = true;
            clickCount = 0;
            foundWinner = false;
            listOfCellsAlreadySet = new ArrayList<>();
            listOfCellsAlreadySet.add(-1);
            Player1 = new ArrayList<>();
            Player2 = new ArrayList<>();
            gameResultMessageBox.setText("");

            createGameRoundMessage(mCurrentRound);
            textViewGameRoundMessage.setText(mGameRoundMessage);
    }

    private void restartGame(){

        textViewPlayer1 = findViewById(R.id.textviewplayer1);
        textViewPlayer2 = findViewById(R.id.textviewplayer2);
        textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));
        textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));

        initialSetupBeforeEveryMatch();
        refreshTheCellsForNextRound();
        mChoiceList.setEnabled(true);
        disableOnClickListeners();
        mButtonStart.setVisibility(View.VISIBLE);

        playAudioBeforeStart();
    }

    /*
    * Game rounds message creator
    * */
    private void createGameRoundMessage(int mCurrentRound){
        mGameRoundMessage = "Round - "+mCurrentRound+"/"+mRound;
    }

    /*
    * To display the score for individual rounds
    * */
    private void displayScores(){

        mStringScorePlayer2 = mScorePlayer2+""; mStringScorePlayer1 = mScorePlayer1+"";
        gameScorePlayer2.setText(mStringScorePlayer2);
        gameScorePlayer1.setText(mStringScorePlayer1);

       /* if(mCurrentRound == mRound)displayFinalWinner();
        Toast.makeText(MainActivity.this,"mCurrentRound: "+mCurrentRound,Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this,"mRound: "+mRound,Toast.LENGTH_SHORT).show();*/
    }

    /*
    * Empty the cells from their image resources set for next round
    * */
    private void refreshTheCellsForNextRound(){
        cell00.setImageResource(0);
        cell01.setImageResource(0);
        cell02.setImageResource(0);


        cell10.setImageResource(0);
        cell11.setImageResource(0);
        cell12.setImageResource(0);


        cell20.setImageResource(0);
        cell21.setImageResource(0);
        cell22.setImageResource(0);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the match hasn't started, continue with handling back button press and exit successfully
        if (!mIsMatchInProgress) {
            Toast.makeText(MainActivity.this,"Have a good day!!",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unfinished match, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        restartGame();
                        Toast.makeText(MainActivity.this,"Press back button again to Exit",Toast.LENGTH_SHORT).show();
                    }
                };

        // Show dialog that there are unfinished match.
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     * Show a dialog that warns the user there are unfinished match that will be lost
     * if they continue leaving the editor.
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative response buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Current Match In Progress,Want to Exit?");
        builder.setPositiveButton("Exit Match", discardButtonClickListener);
        builder.setNegativeButton("Keep Playing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep Playing" button, so dismiss the dialog
                // and continue enjoying the match XD.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void displayFinalWinner(){
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unfinished match.
        showFinalWinnerDialog(discardButtonClickListener);
    }

    private void showFinalWinnerDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative response buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(mScorePlayer1>mScorePlayer2){
            builder.setMessage("Player 1 WON this Match");
        }else if(mScorePlayer2>mScorePlayer1){
            builder.setMessage("Player 2 WON this Match");
        }else{
            builder.setMessage("It's a TIE, Oops!");
        }
        builder.setPositiveButton("Exit Game", discardButtonClickListener);
        builder.setNegativeButton("Start New Match", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                restartGame();

                // User clicked the "Keep Playing" button, so dismiss the dialog
                // and continue enjoying the match XD.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void playAudioBeforeStart(){
        refreshMediaPlayer();
        mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.before_start);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    private void playButtonAudio(){
        refreshButtonMediaPlayer();
        mButtonMediaplayer = MediaPlayer.create(MainActivity.this,R.raw.button_clicks);
        mButtonMediaplayer.start();
    }

    private void refreshButtonMediaPlayer(){
        if(mButtonMediaplayer != null){
            if(mButtonMediaplayer.isPlaying()){mButtonMediaplayer.stop();}
            mButtonMediaplayer.release();
            mButtonMediaplayer = null;
        }
    }

    /*
    * To play separate audio at every individual rounds
    * */
    private void playAudio(){
        refreshMediaPlayer();
        int caseValue = mRound - mCurrentRound -1;
        switch (caseValue){
            case 0:
                mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.final_round);
                break;
            case 1:
                mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.third_round);
                break;
            case 2:
                mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.second_round);
                break;
             default:
                mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.first_round);
                break;
        }
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    /*
    * To release any resourse set with MediaPlayer
    *
    * */
    private void refreshMediaPlayer(){
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()){mMediaPlayer.stop();}
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        refreshMediaPlayer();
        refreshButtonMediaPlayer();

    }

}
