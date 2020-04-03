package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.kata_kuti.WinningLogic.theWinner;


public class MainActivity extends AppCompatActivity {

     private int clickCount = 0;
     private ArrayList<Integer> listOfCellsAlreadySet = new ArrayList<>();
     private boolean foundWinner = false;
     public static List<Integer> Player1 ;
     public static List<Integer> Player2 ;
     public static SparseIntArray cellMap;
     static ArrayList<List<Integer>> winningSet;

     TextView messageBox;
     ImageView cell00,cell01,cell02,cell10,cell11,cell12,cell20,cell21,cell22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewPlayer1 = findViewById(R.id.textviewplayer1);
        textViewPlayer1.setBackgroundColor(Color.parseColor("#424242"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME
        TextView textViewPlayer2 = findViewById(R.id.textviewplayer2);
        textViewPlayer2.setBackgroundColor(Color.parseColor("#757575"));// SET TO INITIAL COLOR DENOTING 1's TURN AT THE BEGINNING OF GAME

        listOfCellsAlreadySet.add(-1);
        cellMap = new SparseIntArray();
        winningSet = new ArrayList<>();
        messageBox = findViewById(R.id.textviewmessage);
        Player1 = new ArrayList<>();
        Player2 = new ArrayList<>();

        /*
        * All the possibles set of cells to win
        * */
        winningSet.add(Arrays.asList(1,2,3));
        winningSet.add(Arrays.asList(4,5,6));
        winningSet.add(Arrays.asList(7,8,9));

        winningSet.add(Arrays.asList(1,4,7));
        winningSet.add(Arrays.asList(2,5,8));
        winningSet.add(Arrays.asList(3,6,9));

        winningSet.add(Arrays.asList(1,5,9));
        winningSet.add(Arrays.asList(3,5,7));

        /*
        * all cells found by Ids
        * */
        cell00 = findViewById(R.id.cell00);
         cell01 = findViewById(R.id.cell01);
         cell02 = findViewById(R.id.cell02);


         cell10 = findViewById(R.id.cell10);
         cell11 = findViewById(R.id.cell11);
         cell12 = findViewById(R.id.cell12);


         cell20 = findViewById(R.id.cell20);
         cell21 = findViewById(R.id.cell21);
         cell22 = findViewById(R.id.cell22);

         /*
         * to ease the use of Id .. to find the winner
         * */
         cellMap.put(R.id.cell00,1);
         cellMap.put(R.id.cell01,2);
         cellMap.put(R.id.cell02,3);


         cellMap.put(R.id.cell10,4);
         cellMap.put(R.id.cell11,5);
         cellMap.put(R.id.cell12,6);


         cellMap.put(R.id.cell20,7);
         cellMap.put(R.id.cell21,8);
         cellMap.put(R.id.cell22,9);

        /*
         * Click Listeners for the first row Cells
         * */
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
         * Click Listeners for the middle row Cells
         * */
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
        * Click Listeners for the last row Cells
        * */
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
                textViewPlayer2.setBackgroundColor(Color.parseColor("#757575")); // SET TO RED DENOTING 2 HAS GIVEN ITS CHOICE
                textViewPlayer1.setBackgroundColor(Color.parseColor("#424242"));/*SET TO GREEN DENOTING 1's TURN NEXT*/ }
            else{
                Player1.add(cellMap.get(view.getId()));
                imageView.setImageResource(R.drawable.kata);
                textViewPlayer1.setBackgroundColor(Color.parseColor("#757575"));// SET TO RED DENOTING 1 HAS GIVEN ITS CHOICE
                textViewPlayer2.setBackgroundColor(Color.parseColor("#424242"));/*SET TO GREEN DENOTING 2's TURN NEXT*/}

            listOfCellsAlreadySet.add(view.getId()); // Once Clicked the value of the cell can't be changed

            if(clickCount > 3)foundWinner = theWinner(clickCount);
            if(foundWinner){
                if(clickCount%2 == 0)messageBox.setText("PLAYER 2 WON");
                else messageBox.setText("PLAYER 1 WON");

            }else if(clickCount == 9){
                /*All cells have been set but winner is still not found
                 *Logic for handling Tie
                 * */
                //Toast.makeText(MainActivity.this,"It's a Tie",Toast.LENGTH_SHORT).show();
                messageBox.setText("IT'S A TIE");
            }

        }
    }
}
