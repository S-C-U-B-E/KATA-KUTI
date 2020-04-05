package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
     private Button mButtonNext;

    //To display the current Onging round
     private String mGameRoundMessage;

    //To store scores of players in a match.
     private int mScorePlayer1;
     private int mScorePlayer2;
     private String mStringScorePlayer1;
    private String mStringScorePlayer2;

     TextView gameResultMessageBox,gameRoundMessageBox,gameScorePlayer1,gameScorePlayer2;
     ImageView cell00,cell01,cell02,cell10,cell11,cell12,cell20,cell21,cell22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView textViewPlayer1 = findViewById(R.id.textviewplayer1);
        textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        TextView textViewPlayer2 = findViewById(R.id.textviewplayer2);
        textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME


        mCurrentRound = 0;
        cellMap = new SparseIntArray();
        winningSet = new ArrayList<>();
        mScorePlayer1 = 0;
        mScorePlayer2 = 0;

        gameResultMessageBox = findViewById(R.id.textviewgameresultmessage);
        gameRoundMessageBox = findViewById(R.id.textviewgameroundmessage);
        gameScorePlayer1 = findViewById(R.id.textviewscore1);
        gameScorePlayer2 = findViewById(R.id.textviewscore2);
        mStringScorePlayer1  = mScorePlayer1+"";
        mStringScorePlayer2 = mScorePlayer2+"";
        gameScorePlayer1.setText(mStringScorePlayer1);
        gameScorePlayer2.setText(mStringScorePlayer2);


        mButtonStart = findViewById(R.id.buttonstart);
        mButtonNext = findViewById(R.id.buttonnext);

                /*
        * Initially (Before selection of mRound and start of game) Gamee Round text box is disabeled.
        * */
        gameRoundMessageBox.setVisibility(View.GONE);
        mButtonNext.setVisibility(View.GONE);

        mChoiceList = (Spinner) findViewById(R.id.rounds);
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
                Toast.makeText(MainActivity.this,"Out of startGame()",Toast.LENGTH_SHORT).show();
            }
        });


        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
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
                R.array.rounds_array, android.R.layout.simple_spinner_item);
        // the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // the adapter to the mChoiceList XD
        mChoiceList.setAdapter(adapter);


        // Set the integer mSelected to the constant values
        mChoiceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("1")) {
                        mRound = 1;
                    } else if (selection.equals("2")) {
                        mRound = 2;
                    } else if (selection.equals("3")) {
                        mRound = 3;
                    } else {
                        mRound = 4;
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

    private void gamePlay(View view){

        ImageView imageView = findViewById(view.getId());
        TextView textViewPlayer1 = findViewById(R.id.textviewplayer1);
        TextView textViewPlayer2 = findViewById(R.id.textviewplayer2);

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
            if(foundWinner){
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
                if(mCurrentRound<mRound)mButtonNext.setVisibility(View.VISIBLE);

                updateScores();

            }else if(clickCount == 9){
                /*All cells have been set but winner is still not found
                 *Logic for handling Tie
                 * */
                //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                gameResultMessageBox.setText("IT'S A TIE");
                textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));
                mScorePlayer2++;mScorePlayer1++;
                if(mCurrentRound<mRound)mButtonNext.setVisibility(View.VISIBLE);

                updateScores();
            }

        }

    }

    private void startGame(){
        refreshTheCellsForNextRound();

        TextView textViewGameRoundMessage;
        TextView textViewPlayer1 = findViewById(R.id.textviewplayer1);
        textViewPlayer1.setBackgroundColor(Color.parseColor("#424242"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        textViewGameRoundMessage = findViewById(R.id.textviewgameroundmessage);

        mChoiceList.setEnabled(false);

        gameRoundMessageBox.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.GONE);
        mButtonNext.setVisibility(View.GONE);

        setupOnClickListeners();

        Toast.makeText(MainActivity.this,"mRound: "+mRound,Toast.LENGTH_SHORT).show();

            mCurrentRound++;Toast.makeText(MainActivity.this,"mCurrentRound: "+mCurrentRound,Toast.LENGTH_SHORT).show();


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

    private void createGameRoundMessage(int mCurrentRound){
        mGameRoundMessage = "Round - "+mCurrentRound+"/"+mRound;
    }

    private void updateScores(){
        mStringScorePlayer2 = mScorePlayer2+""; mStringScorePlayer1 = mScorePlayer1+"";
        gameScorePlayer2.setText(mStringScorePlayer2);
        gameScorePlayer1.setText(mStringScorePlayer1);
    }

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
}
