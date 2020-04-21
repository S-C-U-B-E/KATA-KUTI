package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.example.kata_kuti.WinningLogic.theWinner;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //To count the number of clicks on Cells
     private static int clickCount;

    //To mark the cells already clicked
     private static ArrayList<Integer> listOfCellsAlreadySet ;
   //To mark the empty cells for One-Player mode.
     private static ArrayList<Integer> cellChoiceListForAi;
     private static ArrayList<Integer> corner;
     private static ArrayList<Integer> edge;
     private static ArrayList<Integer> center;


    //To determine if there is any valid winner after every move
     private static boolean foundWinner;

    //To mark the choice of cells opted by individual players
     public static List<Integer> Player1 ;
     public static List<Integer> Player2 ;

    //New Thing learnt (to use instead of HashMap) for an easy use of the CellIds
     public static SparseIntArray cellMap; //ease  the use of cellIds by replacing them with single digits
     public static SparseIntArray reverseCellMap; //sometimes the Id is required from respective single digit (e.g. One-Player mode)

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
     private ImageButton mButtonSettings;

    //To display the current Onging round
     private String mGameRoundMessage;

    //To store scores of players in a match.
     private int mScorePlayer1;
     private int mScorePlayer2;
     private String mStringScorePlayer1;
     private String mStringScorePlayer2;

    //To Handle Back Press
    static boolean mIsMatchInProgress;

    //To set Audio Media
    private static MediaPlayer mMediaPlayer;

    //To Call onResume after onStop called atleast once
    private static boolean mWasOnStopCalled;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    private boolean isSettingsScreenOpened;
    private boolean isMusicAllowed;
    private boolean isApplauseAllowed;
    private boolean isNotificationAllowed;
    public static boolean isNotificationSoundAllowed;
    public static boolean isNotificationVibrationAllowed;
    public static boolean isTwoPlayerModeAllowed;
    public static boolean isSymbolTurnedTrue;

    int Player1_symbol,Player2_symbol;
    ImageView Player1_symbol_image,Player2_symbol_image;

    /*
    * Variables specially declared and set only for THEME CHANGE purpose
    * */
    static String mThemeChoice;
    static String mDifficultyChoice;
    TableLayout parentLayoutBackground;
    RelativeLayout scorePlayerOneParent,scorePlayerTwoParent;
    TextView scorePlayerOne,scorePlayerTwo,scorePlayerOneTitle,scorePlayerTwoTitle,gameRound,gameresult;
    Spinner spinner;
    ImageButton settingsButton,restart,next;
    Button start;
    int playerPlaying,playerIdle;
    int scorePlayer_playing, scorePlayer_idle;






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

        //initializeCellChoiceListForAi();

        setupSharedPreferences();
        if(mThemeChoice.equals("fire")){
            setTheme(R.style.AppTheme_fire);
        }else if(mThemeChoice.equals("earth")){
            setTheme(R.style.AppTheme_earth);
        }else if(mThemeChoice.equals("water")){
            setTheme(R.style.AppTheme_water);
        }

        setContentView(R.layout.activity_main);

        parentLayoutBackground = findViewById(R.id.parentlayout);
        scorePlayerOne = findViewById(R.id.textviewscore1);
        scorePlayerTwo = findViewById(R.id.textviewscore2);
        scorePlayerOneParent = findViewById(R.id.textviewscore1_parent);
        scorePlayerTwoParent = findViewById(R.id.textviewscore2_parent);
        Player1_symbol_image = findViewById(R.id.imageviewplayer1_symbol);
        Player2_symbol_image = findViewById(R.id.imageviewplayer2_symbol);
        scorePlayerOneTitle = findViewById(R.id.textviewplayer1);
        scorePlayerTwoTitle = findViewById(R.id.textviewplayer2);
        spinner = findViewById(R.id.rounds);
        settingsButton = findViewById(R.id.buttonsettings);
        gameRound = findViewById(R.id.textviewgameroundmessage);
        gameresult = findViewById(R.id.textviewgameresultmessage);
        start = findViewById(R.id.buttonstart);
        restart = findViewById(R.id.buttonrestart);
        next = findViewById(R.id.buttonnext);


        /*
         * all cells found by Ids
         * */
        setupCellsById();

        mChoiceList = (Spinner) findViewById(R.id.rounds);

        setCustomColor(mThemeChoice);

        isSettingsScreenOpened = false;
        mWasOnStopCalled = false;
        mRound = 1;

        // Create and setup the to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setupSharedPreferences();

        playAudioBeforeMatchStart();

        textViewPlayer2 = findViewById(R.id.textviewplayer2);
        textViewPlayer1 = findViewById(R.id.textviewplayer1);
        if(isTwoPlayerModeAllowed){
            textViewPlayer1.setText("PLAYER 1");
            textViewPlayer2.setText("PLAYER 2");
        }else{
            if(mDifficultyChoice.equals("insane")){
                textViewPlayer1.setText("S.A.I");
                textViewPlayer2.setText("PLAYER 2");
            }else{
                textViewPlayer1.setText("PLAYER 1");
                textViewPlayer2.setText("S.A.I");
            }
        }

        textViewPlayer1.setBackgroundResource(playerIdle);// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        textViewPlayer2.setBackgroundResource(playerIdle);// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
        scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);

        gameResultMessageBox = findViewById(R.id.textviewgameresultmessage);
        gameRoundMessageBox = findViewById(R.id.textviewgameroundmessage);

        gameScorePlayer1 = findViewById(R.id.textviewscore1);
        gameScorePlayer2 = findViewById(R.id.textviewscore2);


        mButtonStart = findViewById(R.id.buttonstart);
        mButtonNext = findViewById(R.id.buttonnext);
        mButtonRestart = findViewById(R.id.buttonrestart);
        mButtonSettings = findViewById(R.id.buttonsettings);


        cellMap = new SparseIntArray();
        reverseCellMap = new SparseIntArray();
        winningSet = new ArrayList<>();

        initialSetupBeforeEveryMatch(); // Clearing The scores rounds and text values i.e. setting the game field fresh
                                        // for every individual Match.
        /*setupSpinner();*/

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

         setupReverseCellMap();


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

        mButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSettingsScreenOpened = true;
                openSettings();
            }
        });

        Toast.makeText(MainActivity.this,"onCreate()",Toast.LENGTH_SHORT).show();
    }

    private void initializeCornerEdgeCenterList(){
         corner = new ArrayList<>(Arrays.asList(1, 3, 7, 9));
         edge = new ArrayList<>(Arrays.asList(2, 4, 6, 8));
         center = new ArrayList<>();
         center.add(5);
    }

    private void initializeCellChoiceListForAi() {
        cellChoiceListForAi = new ArrayList<>();
        cellChoiceListForAi.add(1);
        cellChoiceListForAi.add(2);
        cellChoiceListForAi.add(3);


        cellChoiceListForAi.add(4);
        cellChoiceListForAi.add(5);
        cellChoiceListForAi.add(6);


        cellChoiceListForAi.add(7);
        cellChoiceListForAi.add(8);
        cellChoiceListForAi.add(9);
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isMusicAllowed = sharedPreferences.getBoolean("music",true);
        isApplauseAllowed = sharedPreferences.getBoolean("applause",true);
        isNotificationAllowed = sharedPreferences.getBoolean("notification",true);
        isNotificationSoundAllowed = sharedPreferences.getBoolean("sound",true);
        isNotificationVibrationAllowed = sharedPreferences.getBoolean("vibration",true);
        isTwoPlayerModeAllowed = sharedPreferences.getBoolean("gamemode",true);
        isSymbolTurnedTrue = sharedPreferences.getBoolean("symbol",true);

        loadColorFromPreferences(sharedPreferences);

        loadDifficultyFromPreferences(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    void loadColorFromPreferences(SharedPreferences sharedPreferences){
        mThemeChoice = sharedPreferences.getString("theme","water");
    }

    void loadDifficultyFromPreferences(SharedPreferences sharedPreferences){
        mDifficultyChoice = sharedPreferences.getString("difficulty","easy");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("music")){
            isMusicAllowed = sharedPreferences.getBoolean("music",true);
        }else if(s.equals("applause")){
            isApplauseAllowed = sharedPreferences.getBoolean("applause",true);
        }else if(s.equals("notification")){
            isNotificationAllowed = sharedPreferences.getBoolean("notification",true);
        }else if(s.equals("sound")){
            isNotificationSoundAllowed = sharedPreferences.getBoolean("sound",true);
        }else if(s.equals("vibration")){
            isNotificationVibrationAllowed = sharedPreferences.getBoolean("vibration",true);
        }else if(s.equals("theme")){
            loadColorFromPreferences(sharedPreferences);
        }else if(s.equals("gamemode")){
            isTwoPlayerModeAllowed = sharedPreferences.getBoolean("gamemode",true);
        }else if(s.equals("difficulty")){
            loadDifficultyFromPreferences(sharedPreferences);
        }else if(s.equals("symbol")){
            isSymbolTurnedTrue = sharedPreferences.getBoolean("symbol",true);
        }
    }

    private void openSettings(){
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
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


    private void setupReverseCellMap(){
        reverseCellMap.put(1,R.id.cell00);
        reverseCellMap.put(2,R.id.cell01);
        reverseCellMap.put(3,R.id.cell02);


        reverseCellMap.put(4,R.id.cell10);
        reverseCellMap.put(5,R.id.cell11);
        reverseCellMap.put(6,R.id.cell12);


        reverseCellMap.put(7,R.id.cell20);
        reverseCellMap.put(8,R.id.cell21);
        reverseCellMap.put(9,R.id.cell22);
    }

    /*
     * Function name is self-explanatory of it's purpose
     * */
    private void setupSpinner(int spinnerLayoutId){
        // Created an ArrayAdapter using the string array and a default mChoiceList layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rounds_array,spinnerLayoutId);
        // the layout to use when the list of choices appears
        adapter.setDropDownViewResource(spinnerLayoutId);
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

        if(isTwoPlayerModeAllowed){
            setupTwoPlayerClickListeners();
        }
        else{

            if(mDifficultyChoice.equals("insane")){
                clickCount++;
                onePlayerAiLogic_Insane();
                setupOnePlayerClickListeners();
            }else{
                setupOnePlayerClickListeners();
            }

        }

    }

    private void setupTwoPlayerClickListeners(){
         /*
          Click Listeners for the first row Cells
         */
        cell00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
        cell01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
        cell02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });


        /*
          Click Listeners for the middle row Cells
         */
        cell10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
        cell11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
        cell12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });

        /*
         Click Listeners for the last row Cells
         */
        cell20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
        cell21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
        cell22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                twoPlayerGamePlay(view);
            }
        });
    }

    private void setupOnePlayerClickListeners(){
         /*
          Click Listeners for the first row Cells
         */
        cell00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });
        cell01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });
        cell02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });


        /*
          Click Listeners for the middle row Cells
         */
        cell10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });
        cell11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });
        cell12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });

        /*
         Click Listeners for the last row Cells
         */
        cell20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });
        cell21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
            }
        });
        cell22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view);
                onePlayerGamePlay(view);
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
    * THE PLAYERS MUST KNOW THEIR CURRENT CHOSEN SYMBOLS
    * SOB POSSIBLE CASES ER TOH DHYAN RAKHTE E HOBE ..TAI NA?
    *
    * */
    private void maintainSymbolImage(){
        if(mThemeChoice.equals("fire")){
            maintainSymbol(R.drawable.kata_fire,R.drawable.kuti_fire);

        }else if(mThemeChoice.equals("water")){
            maintainSymbol(R.drawable.kata_water,R.drawable.kuti_water);

        }else if(mThemeChoice.equals("earth")){
            maintainSymbol(R.drawable.kata_earth,R.drawable.kuti_earth);
        }
    }

    private void maintainSymbol(int vkata, int vkuti){
        if(isSymbolTurnedTrue){
            Player1_symbol_image.setImageResource(vkata);
            Player2_symbol_image.setImageResource(vkuti);

        }else{
            Player1_symbol_image.setImageResource(vkuti);
            Player2_symbol_image.setImageResource(vkata);
        }
    }

    /*
    *
    * Depending on choice of user, User may opt for any one of the symbols (Kata/Kuti)
    * BUT THEY CAN'T CHANGE IT IN MIDDLE OF A MATCH GOING ON.. BUT CAN CHANGE IN BETWEEN ROUNDS OR GAMES
    * SCORES WILL REMAIN UNAFFECTED (I MEAN ..THATS IS THE POINT RIGHT!!! XD)
    * */
    private void maintainSymbol(){
        if(isSymbolTurnedTrue){
            Player1_symbol = R.drawable.kata;
            Player2_symbol = R.drawable.kuti;

        }else{
            Player1_symbol = R.drawable.kuti;
            Player2_symbol = R.drawable.kata;
        }
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

        maintainSymbol();
        maintainSymbolImage();

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
    private void twoPlayerGamePlay(View view){

        ImageView imageView = findViewById(view.getId());

        if( !listOfCellsAlreadySet.contains(view.getId()) && !foundWinner){ // To check if the cell has been clicked already or not.
            clickCount++;
            /*
            * clickCount will decide player 1 OR 2 based on odd-even logic
            * */
            if(clickCount%2 == 0){
                Player2.add(cellMap.get(view.getId()));
                imageView.setImageResource(Player2_symbol);
                textViewPlayer2.setBackgroundResource(playerIdle); // SET TO scorerowfield color DENOTING 2 HAS GIVEN ITS CHOICE
                textViewPlayer1.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 1's TURN NEXT*/
                scorePlayerOneParent.setBackgroundResource(scorePlayer_playing);
                scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
            }
            else{
                Player1.add(cellMap.get(view.getId()));
                imageView.setImageResource(Player1_symbol);
                textViewPlayer1.setBackgroundResource(playerIdle);// SET TO scorerowfield color DENOTING 1 HAS GIVEN ITS CHOICE
                textViewPlayer2.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 2's TURN NEXT*/
                scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                scorePlayerTwoParent.setBackgroundResource(scorePlayer_playing);
            }

            listOfCellsAlreadySet.add(view.getId()); // Once Clicked the value of the cell can't be changed

            if(clickCount > 3)foundWinner = theWinner(clickCount);
            if(foundWinner){ //Logics after a winner is found
                if(clickCount%2 == 0)
                {
                    mScorePlayer2++;
                    gameResultMessageBox.setText("PLAYER 2 WON");
                    textViewPlayer2.setBackgroundResource(playerIdle);
                    textViewPlayer1.setBackgroundResource(playerIdle);
                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                }else {
                    mScorePlayer1++;
                    gameResultMessageBox.setText("PLAYER 1 WON");
                    textViewPlayer2.setBackgroundResource(playerIdle);
                    textViewPlayer1.setBackgroundResource(playerIdle);
                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
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
                textViewPlayer2.setBackgroundResource(playerIdle);textViewPlayer1.setBackgroundResource(playerIdle);
                scorePlayerOne.setBackgroundResource(scorePlayer_idle);
                scorePlayerTwo.setBackgroundResource(scorePlayer_idle);
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
     * The Main Game Logic is in here ... Take a look :D
     * */
    private void onePlayerGamePlay(View view){

        //Toast.makeText(MainActivity.this,"Response from One PlayerMode",Toast.LENGTH_SHORT).show();


        ImageView imageView = findViewById(view.getId());

        if( !listOfCellsAlreadySet.contains(view.getId()) && !foundWinner) { // To check if the cell has been clicked already or not.

            if(mDifficultyChoice.equals("insane")){
                clickCount++;

            if (clickCount < 9 && clickCount%2==0)
            {
                ImageView imageview = findViewById(view.getId()); // select the image view
                Player2.add(cellMap.get(view.getId()));           // add it to the list of Player 2 choosen cells (since winning-logic is same)

                imageview.setImageResource(Player2_symbol);
                textViewPlayer2.setBackgroundResource(playerIdle); // SET TO scorerowfield color DENOTING 2 HAS GIVEN ITS CHOICE
                textViewPlayer1.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 1's TURN NEXT*/
                scorePlayerOneParent.setBackgroundResource(scorePlayer_playing);
                scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                //clickCount++;
                listOfCellsAlreadySet.add(view.getId()); // Once Clicked the value of the cell can't be changed
                cellChoiceListForAi.remove((Integer)cellMap.get(view.getId())); //Remove the already clicked cell by index


                if (corner.contains(Player2.get(Player2.size() - 1))) {
                    corner.remove((Integer) Player2.get(Player2.size() - 1));
                } else if (edge.contains(Player2.get(Player2.size() - 1))) {
                    edge.remove((Integer) Player2.get(Player2.size() - 1));
                } else {
                    center.remove(0);
                }

                foundWinner = theWinner(clickCount);
                if (foundWinner) { //Logics after a winner is found
                    if (clickCount % 2 == 0) {
                        mScorePlayer2++;
                        gameResultMessageBox.setText("PLAYER 2 WON");
                        textViewPlayer2.setBackgroundResource(playerIdle);
                        textViewPlayer1.setBackgroundResource(playerIdle);
                        scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                        scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                    } else {
                        mScorePlayer1++;
                        gameResultMessageBox.setText("S.A.I WON");
                        textViewPlayer2.setBackgroundResource(playerIdle);
                        textViewPlayer1.setBackgroundResource(playerIdle);
                        scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                        scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                    }
                    if (mCurrentRound < mRound) {
                        mButtonNext.setVisibility(View.VISIBLE);
                    } else {
                        mButtonRestart.setVisibility(View.VISIBLE);
                        mIsMatchInProgress = false;
                        displayFinalWinner();
                    }

                    /*
                     * To display the scores in the Score Display Box
                     * */
                    displayScores();

                }
                else if (clickCount == 9){
                    // What if no winner is found AND all cells are set.. Boom It's a TIE
                    /*All cells have been set but winner is still not found
                     *Logic for handling Tie
                     * */
                    //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                    gameResultMessageBox.setText("IT'S A TIE");
                    textViewPlayer2.setBackgroundResource(playerIdle);
                    textViewPlayer1.setBackgroundResource(playerIdle);
                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                    mScorePlayer2 += 0;//No increment of score on tie
                    mScorePlayer1 += 0;//No increment of score on tie
                    if (mCurrentRound < mRound) {
                        mButtonNext.setVisibility(View.VISIBLE);
                    } else {
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

            clickCount++;
            if(clickCount%2!=0 && clickCount<10){
            onePlayerAiLogic_Insane();

            foundWinner = theWinner(clickCount);
            if (foundWinner) { //Logics after a winner is found
                if (clickCount % 2 == 0) {
                    mScorePlayer2++;
                    gameResultMessageBox.setText("PLAYER 2 WON");
                    textViewPlayer2.setBackgroundResource(playerIdle);
                    textViewPlayer1.setBackgroundResource(playerIdle);
                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                } else {
                    mScorePlayer1++;
                    gameResultMessageBox.setText("S.A.I WON");
                    textViewPlayer2.setBackgroundResource(playerIdle);
                    textViewPlayer1.setBackgroundResource(playerIdle);
                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                }
                if (mCurrentRound < mRound) {
                    mButtonNext.setVisibility(View.VISIBLE);
                } else {
                    mButtonRestart.setVisibility(View.VISIBLE);
                    mIsMatchInProgress = false;
                    displayFinalWinner();
                }

                /*
                 * To display the scores in the Score Display Box
                 * */
                displayScores();

            }
            else if (clickCount == 9) { // What if no winner is found AND all cells are set.. Boom It's a TIE
                /*All cells have been set but winner is still not found
                 *Logic for handling Tie
                 * */
                //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                gameResultMessageBox.setText("IT'S A TIE");
                textViewPlayer2.setBackgroundResource(playerIdle);
                textViewPlayer1.setBackgroundResource(playerIdle);
                scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                mScorePlayer2 += 0;//No increment of score on tie
                mScorePlayer1 += 0;//No increment of score on tie
                if (mCurrentRound < mRound) {
                    mButtonNext.setVisibility(View.VISIBLE);
                } else {
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



            } else  /*FROM HERE THE LOGIC IS ONLY FOR HARD AND EASY MODE OF AI.. SINCE THE USER'S TURN WILL BE FIRST.*/
                    {
                        Player1.add(cellMap.get(view.getId()));
                        imageView.setImageResource(Player1_symbol);
                        textViewPlayer1.setBackgroundResource(playerIdle);// SET TO scorerowfield color DENOTING 1 HAS GIVEN ITS CHOICE
                        textViewPlayer2.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 2's TURN NEXT*/
                        scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                        scorePlayerTwoParent.setBackgroundResource(scorePlayer_playing);
                        clickCount++;
                        listOfCellsAlreadySet.add(view.getId()); // Once Clicked the value of the cell can't be changed
                        cellChoiceListForAi.remove((Integer) cellMap.get(view.getId())); // nedded to type-cast it ; otherwise the param was taken as an index (hence, arrayIndexOutOfBoundExcep. XD)
                        //Toast.makeText(MainActivity.this, "removed: " + cellMap.get(view.getId()), Toast.LENGTH_SHORT).show();
                        /*StringBuilder temp = new StringBuilder();
                        for (int i : cellChoiceListForAi) temp.append(i);
                        gameRoundMessageBox.setText(temp);
                        Toast.makeText(MainActivity.this, "Length: " + cellChoiceListForAi.size(), Toast.LENGTH_SHORT).show();*/

                        foundWinner = theWinner(clickCount);
                        if (foundWinner) { //Logics after a winner is found
                            if (clickCount % 2 == 0) {
                                mScorePlayer2++;
                                gameResultMessageBox.setText("S.A.I WON");
                                textViewPlayer2.setBackgroundResource(playerIdle);
                                textViewPlayer1.setBackgroundResource(playerIdle);
                                scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                                scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                            } else {
                                mScorePlayer1++;
                                gameResultMessageBox.setText("PLAYER 1 WON");
                                textViewPlayer2.setBackgroundResource(playerIdle);
                                textViewPlayer1.setBackgroundResource(playerIdle);
                                scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                                scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                            }
                            if (mCurrentRound < mRound) {
                                mButtonNext.setVisibility(View.VISIBLE);
                            } else {
                                mButtonRestart.setVisibility(View.VISIBLE);
                                mIsMatchInProgress = false;
                                displayFinalWinner();
                            }

                            /*
                             * To display the scores in the Score Display Box
                             * */
                            displayScores();

                        }
                        else if (clickCount == 9) { // What if no winner is found AND all cells are set.. Boom It's a TIE
                            /*All cells have been set but winner is still not found
                             *Logic for handling Tie
                             * */
                            //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                            gameResultMessageBox.setText("IT'S A TIE");
                            textViewPlayer2.setBackgroundResource(playerIdle);
                            textViewPlayer1.setBackgroundResource(playerIdle);
                            scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                            scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                            mScorePlayer2 += 0;//No increment of score on tie
                            mScorePlayer1 += 0;//No increment of score on tie
                            if (mCurrentRound < mRound) {
                                mButtonNext.setVisibility(View.VISIBLE);
                            } else {
                                mButtonRestart.setVisibility(View.VISIBLE);
                                mIsMatchInProgress = false;
                                displayFinalWinner();
                            }

                            /*
                             * To display the scores in the Score Display Box
                             * */
                            displayScores();
                        }

                        //Android korte korte basic java bhule gechilam XD.

                        if (!foundWinner && clickCount < 9)
                        {
                                                            /* Since Easy Mode: Player 1 will start first and end last;
                                                             Now if there happens to be a tie, foundWinner:false this would have lead into this code block and would result
                                                             into a 2nd dialog box showing same "it's a Tie,Oops!!" msg.
                                                             To prevent this situation clickCount<9 check was important (Pardon my English XD)
                                                            */

                            if (mDifficultyChoice.equals("easy")) {
                                onePlayerAiLogic_Easy(); //Easy Mode S.A.I Logic
                            } else if (mDifficultyChoice.equals("hard")) {
                                onePlayerAiLogic_Hard(); //Hard Mode S.A.I Logic
                            }

                            foundWinner = theWinner(clickCount);
                            if (foundWinner) { //Logics after a winner is found
                                if (clickCount % 2 == 0) {
                                    mScorePlayer2++;
                                    gameResultMessageBox.setText("S.A.I WON");
                                    textViewPlayer2.setBackgroundResource(playerIdle);
                                    textViewPlayer1.setBackgroundResource(playerIdle);
                                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                                } else {
                                    mScorePlayer1++;
                                    gameResultMessageBox.setText("PLAYER 1 WON");
                                    textViewPlayer2.setBackgroundResource(playerIdle);
                                    textViewPlayer1.setBackgroundResource(playerIdle);
                                    scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                                    scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                                }
                                if (mCurrentRound < mRound) {
                                    mButtonNext.setVisibility(View.VISIBLE);
                                } else {
                                    mButtonRestart.setVisibility(View.VISIBLE);
                                    mIsMatchInProgress = false;
                                    displayFinalWinner();
                                }

                                /*
                                 * To display the scores in the Score Display Box
                                 * */
                                displayScores();

                            } else if (clickCount == 9) { // What if no winner is found AND all cells are set.. Boom It's a TIE
                                /*All cells have been set but winner is still not found
                                 *Logic for handling Tie
                                 * */
                                //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                                gameResultMessageBox.setText("IT'S A TIE");
                                textViewPlayer2.setBackgroundResource(playerIdle);
                                textViewPlayer1.setBackgroundResource(playerIdle);
                                scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
                                scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
                                mScorePlayer2 += 0;//No increment of score on tie
                                mScorePlayer1 += 0;//No increment of score on tie
                                if (mCurrentRound < mRound) {
                                    mButtonNext.setVisibility(View.VISIBLE);
                                } else {
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

        initializeCornerEdgeCenterList();
        initializeCellChoiceListForAi();

        textViewPlayer1.setBackgroundResource(playerPlaying);// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        scorePlayerOneParent.setBackgroundResource(scorePlayer_playing);

        mChoiceList.setEnabled(false);

        gameRoundMessageBox.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.GONE);//No need of the start button after the game has started
        mButtonNext.setVisibility(View.GONE);//Next Round button only required after completion of a round
        mButtonRestart.setVisibility(View.GONE);//No Reset option available after Match starts


            mCurrentRound++; // Update Current Round

        /*
        * Initialize all the variables to their initials
        * for individual Rounds
        * */

            maintainSymbol();
            maintainSymbolImage();
            mIsMatchInProgress = true;
            clickCount = 0;
            foundWinner = false;
            listOfCellsAlreadySet = new ArrayList<>();
            listOfCellsAlreadySet.add(-1);
            Player1 = new ArrayList<>();
            Player2 = new ArrayList<>();
            gameResultMessageBox.setText("");

            createGameRoundMessage(mCurrentRound);
            gameRoundMessageBox.setText(mGameRoundMessage);

        setupOnClickListeners();
    }

    /*
    * Function name is self-explanatory of it's purpose
    * */
    private void restartGame(){

        if(!isTwoPlayerModeAllowed){

            initializeCornerEdgeCenterList();
            initializeCellChoiceListForAi();
            Toast.makeText(MainActivity.this,"One-Player mode active",Toast.LENGTH_SHORT).show();

            if(mDifficultyChoice.equals("insane")){
                textViewPlayer1.setText("S.A.I");
                textViewPlayer2.setText("PLAYER 2");
            }else{
                textViewPlayer1.setText("PLAYER 1");
                textViewPlayer2.setText("S.A.I");
            }

        }else {

            textViewPlayer1.setText("PLAYER 1");
            textViewPlayer2.setText("PLAYER 2");
            Toast.makeText(MainActivity.this,"Two-Player mode active",Toast.LENGTH_SHORT).show();
        }

        maintainSymbol();
        maintainSymbolImage();

        textViewPlayer2.setBackgroundResource(playerIdle);
        textViewPlayer1.setBackgroundResource(playerIdle);
        scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);
        scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);

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
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
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

        if(isApplauseAllowed){
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
            }}

        } //Play different audio clips for different
        //winning or tie situations
        if(mScorePlayer1>mScorePlayer2){

            if( ( (textViewPlayer1.getText()).toString() ).equals("PLAYER 1")){ //match er majhe giye one-player mode e chng kore asleo .. winning msg e kichu bodlabe na
                builder.setMessage("Player 1 WON This Match");}else{builder.setMessage("S.A.I WON This Match!!"+"\nHuman Race Is Doomed!!");}

        }else if(mScorePlayer2>mScorePlayer1){

            if( ( (textViewPlayer2.getText()).toString() ).equals("PLAYER 2")){ //match er majhe giye one-player mode e chng kore asleo .. winning msg e kichu bodlabe na
            builder.setMessage("Player 2 WON This Match");}else{builder.setMessage("S.A.I WON This Match!!"+"\nHuman Race Is Doomed!!");}

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

        if(isMusicAllowed){
        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


            mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.before_start);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();}
        }
    }


    /*
    * To play separate audio at every individual rounds
    * */
    private void playAudioAfterMatchStart(){
        if(isMusicAllowed){
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
            mMediaPlayer.start();}
        }

    }

    /*
     * To play separate audio at every individual rounds
     *
     * NOTE: THIS IS A PARAMETERIZED VERSION WHICH PLAYS THE CORRECT AUDIO AFTER MATCH RESUMES (onResume() called)
     * (i am to lazy to think more XD)
     * */
    private void playAudioAfterMatchStart(int mCurrentRound){
        if(isMusicAllowed){
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
            mMediaPlayer.start();}
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

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(MainActivity.this,"onStart()",Toast.LENGTH_SHORT).show();

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

        Toast.makeText(MainActivity.this,"onStop()",Toast.LENGTH_SHORT).show();
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

        isSettingsScreenOpened = false;

        if(mWasOnStopCalled){

            // Create and setup the to request audio focus
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);



            if(mIsMatchInProgress){
                playAudioAfterMatchStart(mCurrentRound-1);
            }else{
                playAudioBeforeMatchStart();
            }
        }

        Toast.makeText(MainActivity.this,"onResume()",Toast.LENGTH_SHORT).show();


        NotificationUtils.clearAllNotifications(MainActivity.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if(!mIsMatchInProgress) { // ONLY APPLY THESE CHANGES AFTER COMING FROM SETTINGS SCREEN IF A MATCH IS NOT IN PROGRESS
            if (isTwoPlayerModeAllowed) {
                textViewPlayer1.setText("PLAYER 1");
                textViewPlayer2.setText("PLAYER 2");
            } else {

                if(mDifficultyChoice.equals("insane")){
                    textViewPlayer1.setText("S.A.I");
                    textViewPlayer2.setText("PLAYER 2");
                }else{
                    textViewPlayer1.setText("PLAYER 1");
                    textViewPlayer2.setText("S.A.I");
                }

            }

            maintainSymbolImage();
        }
        Toast.makeText(MainActivity.this,"onRestart()",Toast.LENGTH_SHORT).show();

    }

    /*
    * Provide a notification only when A game was in progress before user moves to another app
    * Cool nuh?.. i know.. i rock XD
    * */
    @Override
    protected void onPause() {
        super.onPause();
        if(mIsMatchInProgress && isNotificationAllowed && !isSettingsScreenOpened){
        NotificationUtils.remindUserOfTheOnGoingMatch(MainActivity.this);}

        Toast.makeText(MainActivity.this,"onPause()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

        NotificationUtils.clearAllNotifications(MainActivity.this);

       Toast.makeText(MainActivity.this,"onDestroy()",Toast.LENGTH_SHORT).show();
    }

    public void didTapButton(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Used bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);
    }

    public void setCustomColor(String theme){
        if(theme.equals("fire")){
            parentLayoutBackground.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorEntireBackground_fire));
            //scorePlayerOne.setBackgroundResource(R.color.colorScoreAndPlayer_fire);
            scorePlayerOne.setTextColor(getResources().getColor(R.color.colorText_fire));
            //scorePlayerTwo.setBackgroundResource(R.color.colorScoreAndPlayer_fire);
            scorePlayerTwo.setTextColor(getResources().getColor(R.color.colorText_fire));
            scorePlayerOneTitle.setTextColor(getResources().getColor(R.color.colorText_fire));
            scorePlayerTwoTitle.setTextColor(getResources().getColor(R.color.colorText_fire));

            scorePlayer_idle = R.drawable.gradient_score_player_idle_fire;
            scorePlayer_playing = R.drawable.gradient_score_player_playing_fire;
            playerIdle = R.color.colorScoreAndPlayer_fire;
            playerPlaying = R.color.colorEntireBackground_fire;

            gameRound.setBackgroundResource(R.color.colorGameAndRound_fire);
            gameRound.setTextColor(getResources().getColor(R.color.colorText_fire));
            gameresult.setTextColor(getResources().getColor(R.color.colorText_fire));
            setupSpinner(R.layout.spinner_item_fire);
            spinner.setBackgroundResource(R.color.colorScoreAndPlayer_fire);
            settingsButton.setImageResource(R.drawable.ic_settings_fire);
            restart.setImageResource(R.drawable.restart_game_fire);
            next.setImageResource(R.drawable.next_round_fire);
            start.setBackgroundResource(R.drawable.button_round_fire);
            setCustomCellColor(R.color.colorGameAndRound_fire);

        }
        else if(theme.equals("water")) {
            parentLayoutBackground.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorEntireBackground_water));

            scorePlayerOne.setTextColor(getResources().getColor(R.color.colorText_water));

            scorePlayerTwo.setTextColor(getResources().getColor(R.color.colorText_water));
            scorePlayerOneTitle.setTextColor(getResources().getColor(R.color.colorText_water));

            scorePlayerTwoTitle.setTextColor(getResources().getColor(R.color.colorText_water));

            scorePlayer_idle = R.drawable.gradient_score_player_idle_water;
            scorePlayer_playing = R.drawable.gradient_score_player_playing_water;
            playerIdle = R.color.colorScoreAndPlayer_water;
            playerPlaying = R.color.colorEntireBackground_water;

            gameRound.setBackgroundResource(R.color.colorGameAndRound_water);
            gameRound.setTextColor(getResources().getColor(R.color.colorText_water));
            gameresult.setTextColor(getResources().getColor(R.color.colorText_water));
            setupSpinner(R.layout.spinner_item_water);
            spinner.setBackgroundResource(R.color.colorScoreAndPlayer_water);
            settingsButton.setImageResource(R.drawable.ic_settings_water);
            restart.setImageResource(R.drawable.restart_game_water);
            next.setImageResource(R.drawable.next_round_water);
            start.setBackgroundResource(R.drawable.button_round_water);
            start.setTextColor(getResources().getColor(R.color.colorText_water));
            setCustomCellColor(R.color.colorGameAndRound_water);

        }
        else if(theme.equals("earth")){
            parentLayoutBackground.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorEntireBackground_earth));
            //scorePlayerOne.setBackgroundResource(R.color.colorScoreAndPlayer_earth);
            scorePlayerOne.setTextColor(getResources().getColor(R.color.colorText_earth));
            //scorePlayerTwo.setBackgroundResource(R.color.colorScoreAndPlayer_earth);
            scorePlayerTwo.setTextColor(getResources().getColor(R.color.colorText_earth));
            scorePlayerOneTitle.setTextColor(getResources().getColor(R.color.colorText_earth));
            scorePlayerTwoTitle.setTextColor(getResources().getColor(R.color.colorText_earth));

            scorePlayer_idle = R.drawable.gradient_score_player_idle_earth;
            scorePlayer_playing = R.drawable.gradient_score_player_playing_earth;
            playerIdle = R.color.colorScoreAndPlayer_earth;
            playerPlaying = R.color.colorEntireBackground_earth;

            gameRound.setBackgroundResource(R.color.colorGameAndRound_earth);
            gameRound.setTextColor(getResources().getColor(R.color.colorText_earth));
            gameresult.setTextColor(getResources().getColor(R.color.colorText_earth));
            setupSpinner(R.layout.spinner_item_earth);
            spinner.setBackgroundResource(R.color.colorScoreAndPlayer_earth);
            settingsButton.setImageResource(R.drawable.ic_settings_earth);
            restart.setImageResource(R.drawable.restart_game_earth);
            next.setImageResource(R.drawable.next_round_earth);
            start.setBackgroundResource(R.drawable.button_round_earth);
            setCustomCellColor(R.color.colorGameAndRound_earth);

        }
    }

    public void setCustomCellColor(int colorId){

        cell00.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
        cell01.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
        cell02.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));


        cell10.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
        cell11.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
        cell12.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));


        cell20.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
        cell21.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
        cell22.setBackgroundColor(ContextCompat.getColor(MainActivity.this, colorId));
    }


    /*
    *
    * One-Player mode:
    * Logic of opponent a.k.a JARVIS XD; ("And I am... Iron Man <3")
    * The logic is : any random empty spot is chosen (since it is easy mode)
    * */
    public void onePlayerAiLogic_Easy(){
        /*
        * In easy mode AI will choose a position at random
        * */

        disableOnClickListeners(); // such that no input is taken from user while AI's turn
        Random rand = new Random();
        int numRandomIndex = rand.nextInt(cellChoiceListForAi.size()); //Choose a random index from a list of empty spots.
        int cellChoice  = cellChoiceListForAi.get(numRandomIndex);     //Take the cell value
        int cellIdChoice = reverseCellMap.get(cellChoice);        //Take the cell id for that particular value by reverse-mapping to it's ID


        ImageView imageview = findViewById(cellIdChoice);// select the image view
        Player2.add(cellMap.get(cellIdChoice));           // add it to the list of Player 2 choosen cells (since winning-logic is same)

        imageview.setImageResource(Player2_symbol);
        textViewPlayer2.setBackgroundResource(playerIdle); // SET TO scorerowfield color DENOTING 2 HAS GIVEN ITS CHOICE
        textViewPlayer1.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 1's TURN NEXT*/
        scorePlayerOneParent.setBackgroundResource(scorePlayer_playing);
        scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
        clickCount++;
        listOfCellsAlreadySet.add(cellIdChoice); // Once Clicked the value of the cell can't be changed
        cellChoiceListForAi.remove(numRandomIndex); //Remove the already clicked cell by index


        setupOnePlayerClickListeners(); //for safety purpose temporarily the listeners were disabeled.. but i think it's not that important
                                        //moreover ..this may cause performance issue.. none that i've seen .. but still...
        //(ETTO ENGLISH E COMMENT KORTE BHALO LAGCHENA MY GOOOOOOOOD!!..BUT KORTE HOBEI NAHOLE BUJHBONA PORE XD!!)

    }

    public void onePlayerAiLogic_Hard(){
        /*
         * In hard mode AI will choose a position that if kept empty player 1 will win.
         * */
        // akeText(MainActivity.this, "Hard Mode", Toast.LENGTH_SHORT).show();
        int numRandomIndex=-1, cellIdChoice=-1, cellChoice=-1;

        disableOnClickListeners(); // such that no input is taken from user while AI's turn

        boolean foundPerfectCell = false;
        if(Player1.size()>=2){
            outermostloop:
            for(int i=0;i<Player1.size()-1;i++){
                for(int j=i+1; j<Player1.size(); j++){
                    for(List<Integer> al:winningSet){
                        ArrayList<Integer> l = new ArrayList<>(al);

                        int temp = Player1.get(i);
                        if(l.contains(temp))l.remove((Integer)temp);

                        temp = Player1.get(j);
                        if(l.contains(temp))l.remove((Integer)temp);

                        if( l.size()==1 && cellChoiceListForAi.contains(l.get(0)) ){


                            cellChoice = l.get(0);
                            foundPerfectCell = true;
                            break outermostloop;
                        }

                    }

                }
            }

            cellIdChoice = reverseCellMap.get(cellChoice);


        }
        if(!foundPerfectCell){

            Random rand = new Random();

            numRandomIndex = rand.nextInt(cellChoiceListForAi.size()); //Choose a random index from a list of empty spots.
            cellChoice  = cellChoiceListForAi.get(numRandomIndex);     //Take the cell value

            cellIdChoice = reverseCellMap.get(cellChoice);        //Take the cell id for that particular value by reverse-mapping to it's ID

        }


        ImageView imageview = findViewById(cellIdChoice);// select the image view
        Player2.add(cellMap.get(cellIdChoice));           // add it to the list of Player 2 choosen cells (since winning-logic is same)

        imageview.setImageResource(Player2_symbol);
        textViewPlayer2.setBackgroundResource(playerIdle); // SET TO scorerowfield color DENOTING 2 HAS GIVEN ITS CHOICE
        textViewPlayer1.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 1's TURN NEXT*/
        scorePlayerOneParent.setBackgroundResource(scorePlayer_playing);
        scorePlayerTwoParent.setBackgroundResource(scorePlayer_idle);
        clickCount++;
        listOfCellsAlreadySet.add(cellIdChoice); // Once Clicked the value of the cell can't be changed
        cellChoiceListForAi.remove(cellChoiceListForAi.indexOf(cellChoice)); //Remove the already clicked cell by index

        setupOnePlayerClickListeners(); //for safety purpose temporarily the listeners were disabeled.. but i think it's not that important
        //moreover ..this may cause performance issue.. none that i've seen .. but still...
        //(ETTO ENGLISH E COMMENT KORTE BHALO LAGCHENA MY GOOOOOOOOD!!..BUT KORTE HOBEI NAHOLE BUJHBONA PORE XD!!)

    }

    private void onePlayerAiLogic_Insane(){

        /*
        * In Insane logic there is no chance of opponent winning XD.. and i don't know if people will this or not XD
        * Opponent(of SAI) maximum can make a Tie or Lose the game
        *
        * In the quest of building this logic i myself became a "almost" TIC-TAC-TOE champ LOL...
        *
        * S.A.I will always go first in this mode!!! ;)
        * */

        boolean blockedOpponent = false;
        boolean movedForward = false;

        int cellChoice = -1;

        //FIRST IF ANY CHANCE OF WINNING FROM PREVIOUS DESCISIONS MADE(IF ANY)
        outermostloop:
        for (int i = 0; i < Player1.size() - 1; i++) {
            for (int j = i + 1; j < Player1.size(); j++) {
                for (List<Integer> al : winningSet) {

                    ArrayList<Integer> l = new ArrayList<>(al);
                    int temp = Player1.get(i);
                    if (l.contains(temp)) l.remove((Integer) temp);
                    temp = Player1.get(j);
                    if (l.contains(temp)) l.remove((Integer) temp);
                    if (l.size() == 1 && !Player2.contains(l.get(0)) ) {
                        movedForward = true;
                        cellChoice = l.get(0);

                        Player1.add(l.get(0));
                        cellChoiceListForAi.remove((Integer)l.get(0));

                        break outermostloop;
                    }

                }

            }
        }

        if (!movedForward) {
            //SECOND CHECK FOR ANY CHANCE OF OPPONENT WINNING
            outermostloop:
            for (int i = 0; i < Player2.size() - 1; i++) {
                for (int j = i + 1; j < Player2.size(); j++) {

                        for (List<Integer> al : winningSet) {

                            ArrayList<Integer> l = new ArrayList<>(al);
                            int temp = Player2.get(i);
                            if (l.contains(temp)) l.remove((Integer) temp);
                            temp = Player2.get(j);
                            if (l.contains(temp)) l.remove((Integer) temp);
                            if (l.size() == 1 && !Player1.contains(l.get(0))) {
                                blockedOpponent = true;
                                movedForward = true;
                                cellChoice = l.get(0);
                                //System.out.println(l.get(0));
                                Player1.add(l.get(0));
                                cellChoiceListForAi.remove((Integer)l.get(0));

                                break outermostloop;
                            }
                        }

                }
            }
        }

        Random rand;
        if (!blockedOpponent && !movedForward) {
            if (clickCount == 1) {              //IN S.A.I FIRST TURN IT TAKES A RANDOM CORNER...
                rand = new Random();
                int chosenCorner = corner.get(rand.nextInt(corner.size()));

                cellChoiceListForAi.remove((Integer)chosenCorner);
                Player1.add(chosenCorner);
                cellChoice = chosenCorner;

                corner.remove(corner.indexOf(chosenCorner));


            } else if (clickCount == 3) {/* FROM HERE ON S.A.I's TURN WILL DEPEND ON OPPONENT*/ //S.A.I's 3rd TURN

                //any corner
                if (corner.contains(Player2.get(0))) {  // IF OPPONENT TAKES ANY CORNER... SO WILL S.A.I
                    rand = new Random();
                    int chosenCorner = corner.get(rand.nextInt(corner.size()));

                    cellChoiceListForAi.remove((Integer)chosenCorner);
                    Player1.add(chosenCorner);
                    cellChoice = chosenCorner;

                    corner.remove(corner.indexOf(chosenCorner));

                } //edge cell next to 1st X  IF OPPONENT TAKES ANY EDGE TOUCHED TO THE 'X' OF S.A.I
                else if (Math.abs(Player1.get(0) - Player2.get(0)) == 1 || Math.abs(Player1.get(0) - Player2.get(0)) == 3) {
                                /*
                                * ALL SUCH EDGES AND 'X' PLACES HAVE A DIFFERENCE OF 1 OR 3 (ABSOLUTE VALUE).. THEN S.A.I WILL GO FOR THE CENTER..
                                * */

                    cellChoiceListForAi.remove((Integer)center.get(0));
                    Player1.add(center.get(0));
                    cellChoice = center.get(0);

                    center.remove(0);

                } //center IF OPPONENT TAKES THE CENTER
                else if (Player2.get(0) == 5) {
                        /*
                        * THEN S.A.I WILL TRY TO MAKE A 'X O X' DIAGONALLY...
                        * */

                    cellChoiceListForAi.remove((Integer) (10 - Player1.get(0)));
                    Player1.add(10 - Player1.get(0));
                    cellChoice = (10 - Player1.get(0));

                    corner.remove((Integer) (10 - Player1.get(0)));

                } //any other edge cell  IF OPPONENT TAKES ANY EDGE... S.A.I WILL GO FOR ANY RANDOM CORNER...
                else {
                    rand = new Random();
                    int chosenCorner = corner.get(rand.nextInt(corner.size()));

                    cellChoiceListForAi.remove((Integer)chosenCorner);
                    Player1.add(chosenCorner);
                    cellChoice = chosenCorner;

                    corner.remove(corner.indexOf(chosenCorner));
                }

            }
            else if (clickCount == 5) { //S.A.I 5th TURN
                rand = new Random();
                int chosenCorner = corner.get(rand.nextInt(corner.size()));

                cellChoiceListForAi.remove((Integer)chosenCorner);
                Player1.add(chosenCorner);
                cellChoice = chosenCorner;

                corner.remove(corner.indexOf(chosenCorner));
            }
            else{
                rand = new Random(); // FOR ANY OTHER TURN .. S.A.I WILL TAKE ANY RANDOM CORNER ... AGAIN!!!!!!! B)
                int randomChoice = cellChoiceListForAi.get(rand.nextInt(cellChoiceListForAi.size()));

                cellChoiceListForAi.remove((Integer)randomChoice);
                Player1.add(randomChoice);
                cellChoice = randomChoice;
            }
        }

        /*
        * THIS NEXT PART OF ASSIGNUNG SYMBOL IS SAME AS HARD OR EASY MODE...
        * */
        int cellIdChoice = reverseCellMap.get(cellChoice);        //Take the cell id for that particular value by reverse-mapping to it's ID


        ImageView imageview = findViewById(cellIdChoice);// select the image view
        Player1.add(cellMap.get(cellIdChoice));           // add it to the list of Player 2 choosen cells (since winning-logic is same)

        imageview.setImageResource(Player1_symbol);
        textViewPlayer1.setBackgroundResource(playerIdle); // SET TO scorerowfield color DENOTING 2 HAS GIVEN ITS CHOICE
        textViewPlayer2.setBackgroundResource(playerPlaying);/*SET TO gameplayfield color DENOTING 1's TURN NEXT*/
        scorePlayerTwoParent.setBackgroundResource(scorePlayer_playing);
        scorePlayerOneParent.setBackgroundResource(scorePlayer_idle);

        listOfCellsAlreadySet.add(cellIdChoice); // Once Clicked the value of the cell can't be changed



    }

}
