package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
     private static int mRound;
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

    //To Call onResume after onStop called atleast once
    private static boolean mWasOnStopCalled;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;


    TextView gameResultMessageBox,gameRoundMessageBox,gameScorePlayer1,gameScorePlayer2,textViewPlayer1,textViewPlayer2;
     public ImageView cell00,cell01,cell02,cell10,cell11,cell12,cell20,cell21,cell22;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWasOnStopCalled = false;
        mRound = 1;

        // Create and setup the to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        playAudioBeforeMatchStart();


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



        /*
         * Click on Start Button to start the game XD
         * */
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame();
            }
        });

        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restartGame();
            }
        });

        //Toast.makeText(MainActivity.this,"onCreate()",Toast.LENGTH_SHORT).show();
    }

    /*
     * Function name is self-explanatory of it's purpose
     * */
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

    /*
     * Function name is self-explanatory of it's purpose
     * */
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

    /*
     * Function name is self-explanatory of it's purpose
     * */
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

    /*
     * Function name is self-explanatory of it's purpose
     * */
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

    /*
    * The cells only have to respond after a match starts (After The Start Button Is Clicked)
    * Thats why it is defined outside of onCreate()
    * */
    private void setupOnClickListeners(){

        /*
          Click Listeners for the first row Cells
         */
        cell00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });
        cell01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });
        cell02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });


        /*
          Click Listeners for the middle row Cells
         */
        cell10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });
        cell11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });
        cell12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });

        /*
         Click Listeners for the last row Cells
         */
        cell20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });
        cell21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                gamePlay(view);
            }
        });
        cell22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
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

    /*
     * Function name is self-explanatory of it's purpose
     * */
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

    /*
    * The Main Game Logic is in here ... Take a look :D
    * */
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

    /*
     * Function name is self-explanatory of it's purpose
     * */
    private void startGame(){
        playAudioAfterMatchStart();
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

    /*
    * Function name is self-explanatory of it's purpose
    * */
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

        playAudioBeforeMatchStart();
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

    /*
    * The name of the function is self-explanatory
    * */
    private void showFinalWinnerDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative response buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        releaseMediaPlayer();
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            if(mScorePlayer1!=mScorePlayer2)
            {
                mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.drum_roll_audio_clip);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.during_winningmessage);
                        mMediaPlayer.start();
                    }
                });
            }
            else{
                mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.sad_gamelost_audio_clip);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.bruh_audio_clip);
                        mMediaPlayer.start();
                    }
                });
            }
        } //Play different audio clips for different
        //winning or tie situations
        if(mScorePlayer1>mScorePlayer2){
            builder.setMessage("Player 1 WON This Match");
        }else if(mScorePlayer2>mScorePlayer1){
            builder.setMessage("Player 2 WON This Match");
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

    /*
    * Audio Before a Match Starts
    * */
    private void playAudioBeforeMatchStart(){

        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.before_start);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }


    /*
    * To play separate audio at every individual rounds
    * */
    private void playAudioAfterMatchStart(){
        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            int caseValue = mRound - mCurrentRound -1;
            //Toast.makeText(MainActivity.this, "mRound:"+mRound+" mCurrentR:"+mCurrentRound, Toast.LENGTH_SHORT).show();
            switch (caseValue){
                case 0:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.final_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.third_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.second_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.first_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
            }
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }

    }

    /*
     * To play separate audio at every individual rounds
     *
     * NOTE: THIS IS A PARAMETERIZED VERSION WHICH PLAYS THE CORRECT AUDIO AFTER MATCH RESUMES (onResume() called)
     * (i am to lazy to think more XD)
     * */
    private void playAudioAfterMatchStart(int mCurrentRound){
        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            int caseValue = mRound - mCurrentRound -1;
            //Toast.makeText(MainActivity.this, "mRound:"+mRound+" mCurrentR:"+mCurrentRound, Toast.LENGTH_SHORT).show();
            switch (caseValue){
                case 0:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.final_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.third_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.second_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.first_round);
                    //Toast.makeText(MainActivity.this, "case:"+(mRound - mCurrentRound -1), Toast.LENGTH_SHORT).show();
                    break;
            }
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }

    }

    /*
    * To release any resourse set with MediaPlayer
    *
    * */
    private void releaseMediaPlayer(){
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()){mMediaPlayer.stop();}
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /*
    * called onStop to release resources when app is closed
    * */
    @Override
    protected void onStop() {
        super.onStop();

        mWasOnStopCalled = true;
        releaseMediaPlayer();

        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        //Toast.makeText(MainActivity.this,"onStop()",Toast.LENGTH_SHORT).show();
    }

    /*
    * To handle the case when another app comes up
    * and the lifecycle of this app goes to Stop() Or Pause() state And;
    * the app is opened up again .. to resume where user left. :D
    *
    * */
    @Override
    protected void onResume() {
        super.onResume();

        if(mWasOnStopCalled){

            // Create and setup the to request audio focus
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);



            if(mIsMatchInProgress){
                playAudioAfterMatchStart(mCurrentRound-1);
            }else{
                playAudioBeforeMatchStart();
            }
        }

        //Toast.makeText(MainActivity.this,"onResume()",Toast.LENGTH_SHORT).show();


        NotificationUtils.clearAllNotifications(MainActivity.this);
    }


    /*
    * Provide a notification only when A game was in progress before user moves to another app
    * Cool nuh?.. i know.. i rock XD
    * */
    @Override
    protected void onPause() {
        super.onPause();
        if(mIsMatchInProgress){
        NotificationUtils.remindUserOfTheOnGoingMatch(MainActivity.this);}

        //Toast.makeText(MainActivity.this,"onPause()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationUtils.clearAllNotifications(MainActivity.this);

       //Toast.makeText(MainActivity.this,"onDestroy()",Toast.LENGTH_SHORT).show();
    }

    public void didTapButton(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);
    }

}
