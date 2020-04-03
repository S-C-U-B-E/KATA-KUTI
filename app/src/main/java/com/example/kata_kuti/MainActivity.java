package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.kata_kuti.WinningLogic.theWinner;


public class MainActivity extends AppCompatActivity {

     private int clickCount = 0;
     private ArrayList<Integer> listOfCellsAlreadySet = new ArrayList<>();
     private boolean foundWinner = false;
     public static List<Integer> Player1 = new ArrayList<>();
     public static List<Integer> Player2 = new ArrayList<>();
     public static SparseIntArray cellMap;
     static ArrayList<List<Integer>> winningSet;

    ImageView cell00,cell01,cell02,cell10,cell11,cell12,cell20,cell21,cell22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfCellsAlreadySet.add(-1);
        cellMap = new SparseIntArray();
        winningSet = new ArrayList<>();

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
                nextSymbol(view);
            }
        });
        cell01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });
        cell02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });


        /*
         * Click Listeners for the middle row Cells
         * */
        cell10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });
        cell11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });
        cell12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });

        /*
        * Click Listeners for the last row Cells
        * */
        cell20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });
        cell21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });
        cell22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSymbol(view);
            }
        });
    }

    private void nextSymbol(View view){
        //Toast.makeText(MainActivity.this,"Clicked on "+view.getId(),Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(view.getId());

        if( !listOfCellsAlreadySet.contains(view.getId()) && !foundWinner){ // To check if the cell has been clicked already or not.
            clickCount++;
        if(clickCount%2 == 0){
            Player2.add(cellMap.get(view.getId()));
            imageView.setImageResource(R.drawable.kuti);
            /*Toast.makeText(MainActivity.this,"Clicked on "+cellMap.get(view.getId())+"th cell",Toast.LENGTH_SHORT).show();*/}
        else{
            Player1.add(cellMap.get(view.getId()));
            imageView.setImageResource(R.drawable.kata);
            /*Toast.makeText(MainActivity.this,"Clicked on "+cellMap.get(view.getId())+"th cell",Toast.LENGTH_SHORT).show();*/}

        listOfCellsAlreadySet.add(view.getId()); // Once Clicked the value of the cell can't be changed


        foundWinner = theWinner(clickCount);
            if(foundWinner){
                if(clickCount%2 == 0)Toast.makeText(MainActivity.this,"PLAYER 2 WON",Toast.LENGTH_SHORT).show();
                else Toast.makeText(MainActivity.this,"PLAYER 1 WON",Toast.LENGTH_SHORT).show();

            }

        }
    }
}
