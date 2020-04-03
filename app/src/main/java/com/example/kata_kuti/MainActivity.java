package com.example.kata_kuti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int clickCount = 0;
    private ArrayList<Integer> listOfCellsAlreadySet = new ArrayList<>();
    private boolean foundWinner = false;
    private ArrayList<ImageView> Player1 = new ArrayList<>();
    private ArrayList<ImageView> Player2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfCellsAlreadySet.add(-1);

        ImageView cell00 = findViewById(R.id.cell00);
        ImageView cell01 = findViewById(R.id.cell01);
        ImageView cell02 = findViewById(R.id.cell02);


        ImageView cell10 = findViewById(R.id.cell10);
        ImageView cell11 = findViewById(R.id.cell11);
        ImageView cell12 = findViewById(R.id.cell12);


        ImageView cell20 = findViewById(R.id.cell20);
        ImageView cell21 = findViewById(R.id.cell21);
        ImageView cell22 = findViewById(R.id.cell22);


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
        Toast.makeText(MainActivity.this,"Clicked on "+view.getId(),Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(view.getId());
        if( !listOfCellsAlreadySet.contains(view.getId())){
            clickCount++;
        if(clickCount%2 == 0){
            Player2.add(imageView);
            imageView.setImageResource(R.drawable.kuti);}
        else{
            Player1.add(imageView);
            imageView.setImageResource(R.drawable.kata);}

        listOfCellsAlreadySet.add(view.getId());
        }
    }
}
