package com.example.kata_kuti;


import java.util.List;

import static com.example.kata_kuti.MainActivity.Player1;
import static com.example.kata_kuti.MainActivity.Player2;
import static com.example.kata_kuti.MainActivity.winningSet;

public class WinningLogic {

     static boolean theWinner(int clickCount){

         if(clickCount%2 == 0){
             return(calcTheWinner(Player2));
         }else{
             return(calcTheWinner(Player1));
         }

    }

    private static boolean calcTheWinner(List<Integer> Player){
     boolean answer = false;

     for(List<Integer> i: winningSet){
         if(Player.containsAll(i)){
             answer = true;
             break;
         }
     }
         return answer;
    }

}
