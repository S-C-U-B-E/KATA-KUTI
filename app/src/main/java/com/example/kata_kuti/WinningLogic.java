package com.example.kata_kuti;


import java.util.ArrayList;
import java.util.List;

import static com.example.kata_kuti.MainActivity.Player1;
import static com.example.kata_kuti.MainActivity.Player2;
import static com.example.kata_kuti.MainActivity.markWonCells;
import static com.example.kata_kuti.MainActivity.winningSet;

public class WinningLogic {

     static boolean theWinner(int clickCount){

         if(clickCount%2 == 0){
             return(calcTheWinner(Player2)); //Player 2 wins (even click)
         }else{
             return(calcTheWinner(Player1)); //Player 1 wins (odd click)
         }

    }

    private static boolean calcTheWinner(List<Integer> Player){
     boolean anyWinnigSetFoundAmongTheCellsSetByAPlayer = false;

     for(List<Integer> i: winningSet){
         if(Player.containsAll(i)){
             anyWinnigSetFoundAmongTheCellsSetByAPlayer = true;
             markWonCells = new ArrayList<>();
             markWonCells.addAll(i);
             break;
         }
     }
         return anyWinnigSetFoundAmongTheCellsSetByAPlayer;
    }

}
