package edu.unc.sanjorge.eight_queens;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boardTile[][] refArray = new boardTile[8][8];
    boolean[][] bool = new boolean[8][8];
    int queenCount = 0;
    int[] queenBookKeeping = new int[8];
    boolean won = false;
    boardTile[][] tempRefArray = new boardTile[8][8];
    ArrayList<Integer> queenLessRows = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0; i<8; i++) {
            queenBookKeeping[i] = -1;
        }

        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardTile button = new boardTile(this, i, j);   ///wrap_content, is a size that matches what you need
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                ViewGroup.LayoutParams squareDim = new ViewGroup.LayoutParams(metrics.widthPixels / 8, metrics.widthPixels / 8);
                button.setLayoutParams(squareDim);

                if ((i + j) % 2 == 1) {     //makes checkered visual
                    button.setBackgroundColor(Color.BLACK);
                } else {
                    button.setBackgroundColor(Color.rgb(222, 184, 135));
                }

                refArray[i][j] = button;
                bool[i][j] = false;

                placeQueen(button);

                //when button is pressed, replace image on button to queen
                grid.addView(button); //adds the button to be viewed
            }
        }

        Button giveUp = (Button) findViewById(R.id.GiveUpbutton);
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveUpFunc(view);
            }
        });

    }

    public void giveUpFunc(View v) {
        for (int i=0; i<8; i++) {
            queenBookKeeping[i] = -1;   //reset book keeping
        }

        for (int i=0; i<8; i++) {
           for (int j=0; j<8; j++) {
                if(bool[i][j] == true) {
                    queenBookKeeping[i] = j;    //fills in the non negatives
                }
           }
        }

        for (int i=0; i<8; i++) {
            if (queenBookKeeping[i] == -1) {    //finds the starting point
                queenLessRows.add(i);
            }
        }


        won = false; //reset for future use purposes
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                tempRefArray[i][j] = refArray[i][j] ;
            }
        }
        giveUpFuncHelper(queenLessRows, 0);

        if (won) { /*keep the answer saved */ }
        else {      //change main board back to the way it was
            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    Log.d("DEBUG", "i:" + i + ", j:" + j);
                    refArray[i][j] = tempRefArray[i][j] ;
                }
            }
            Toast.makeText(this, "No Solution", Toast.LENGTH_SHORT).show();
        }

        queenLessRows = new ArrayList<Integer>(); //reset for future purposes
    }

    //if you return and there is still no 8 queen count, toast no solution
    //return will step back once, not all together
    public void giveUpFuncHelper(ArrayList<Integer> arrayList, int row) {
        if (row == arrayList.size()) {
            won = true;
            return; //out of overall bounds, end
        }
        //if it reaches here, you won

        for (int j=0; j<8; j++) {
            //check method will place queen (true), or not allow you to place queen (true)
            //note that removing a queen would give true, but we will skip row with queens
            //already placed, so that will not interfere with this statement
            if (placeQueenCheck(queenLessRows.get(row),j) == true) {
                giveUpFuncHelper(arrayList, row + 1);
                if (won) { //break out of loop to return solution
                    break;    //note that you do not reach the function to remove it
                }
                placeQueenCheck(queenLessRows.get(row),j); //removes queen to try another attempt
            }
            else { }//keep searching for valid spot for queen
        }

        //if went through for loop and found no spot, go back a step
        return;
    }

    //will give true if you can place and will place queen, gives false if you can't place queen
    //gives true if taking off a queen, because it is still following rules
    //the 1-D array will catch rows with queens, it will skip the rows with queens by default, so
    //the boolean output doesn't matter here, BUT for rows with -1, it will determine whether
    //or not is is indeed valid to place queen here, and will do so if able
    public boolean placeQueenCheck(int xCoordinate, int yCoordinate) {
        if ((xCoordinate + yCoordinate) % 2 == 1) {
            if (bool[xCoordinate][yCoordinate] == false) { //nothing there
                boolean okToPlace = true;

                for (int i = 0; i < 8; i++) {    //check for cross intersection
                    for (int j = 0; j < 8; j++) {
                        if (xCoordinate == i || yCoordinate == j) { //in cross view
                            if (bool[i][j] == true) {
                                okToPlace = false; //do not place
                            } else {
                            } //place
                        } else {
                        } //ignore
                    }
                }


                int x = xCoordinate;
                int y = yCoordinate;
                for (int i = 0; i < 8; i++) {    //check for cross intersection "/" direction
                    for (int j = 0; j < 8; j++) {
                        if ((x + y) == (i + j)) {
                            if (bool[i][j] == true) {
                                okToPlace = false; //do not place
                            } else {
                            } //place
                        } else {
                        }
                    }
                }


                int yTemp = y;
                for (int i = x; i < 8; i++) {
                    if ((yTemp >= 0 && yTemp <= 7) && bool[i][yTemp] == true) {
                        okToPlace = false; //do not place
                    } else {
                    } //place
                    yTemp++;
                }

                yTemp = y;
                for (int i = x; i >= 0; i--) {
                    if ((yTemp >= 0 && yTemp <= 7) && bool[i][yTemp] == true) {
                        okToPlace = false; //do not place
                    } else {
                    } //place
                    yTemp--;
                }

                if (okToPlace) {
                    refArray[xCoordinate][yCoordinate].setBackgroundResource(R.mipmap.queenwithblack);
                    bool[xCoordinate][yCoordinate] = true;
                    queenCount++;
                    return true;
                }
                else {
                    return false;
                }

            } else { //queen is there, so take off
                refArray[xCoordinate][yCoordinate].setBackgroundColor(Color.BLACK);
                bool[xCoordinate][yCoordinate] = false;
                queenCount--;
            }
        } else {
            if (bool[xCoordinate][yCoordinate] == false) {

                boolean okToPlace = true;

                for (int i = 0; i < 8; i++) {    //check for cross intersection
                    for (int j = 0; j < 8; j++) {
                        if (xCoordinate == i || yCoordinate == j) { //in cross view
                            if (bool[i][j] == true) {
                                okToPlace = false; //do not place
                            } else {
                            } //place
                        } else {
                        } //ignore
                    }
                }


                int x = xCoordinate;
                int y = yCoordinate;
                for (int i = 0; i < 8; i++) {    //check for cross intersection "/" direction
                    for (int j = 0; j < 8; j++) {
                        if ((x + y) == (i + j)) {
                            if (bool[i][j] == true) {
                                okToPlace = false; //do not place
                            } else {
                            } //place
                        } else {
                        }
                    }
                }


                int yTemp = y;
                for (int i = x; i < 8; i++) {
                    if ((yTemp >= 0 && yTemp <= 7) && bool[i][yTemp] == true) {
                        okToPlace = false; //do not place
                    } else {
                    } //place
                    yTemp++;
                }

                yTemp = y;
                for (int i = x; i >= 0; i--) {
                    if ((yTemp >= 0 && yTemp <= 7) && bool[i][yTemp] == true) {
                        okToPlace = false; //do not place
                    } else {
                    } //place
                    yTemp--;
                }




                if (okToPlace) {
                    refArray[xCoordinate][yCoordinate].setBackgroundResource(R.mipmap.queenwithtan);
                    bool[xCoordinate][yCoordinate] = true;
                    queenCount++;
                    return true;
                } else {
                    return false;
                }    //do not place

            } else {
                refArray[xCoordinate][yCoordinate].setBackgroundColor(Color.rgb(222, 184, 135));
                bool[xCoordinate][yCoordinate] = false;
                queenCount--;
            }
        }
        return true; // reaches here if you are taking off a queen, note that
        //the place void method will do nothing if no error came up
    }

    public void restartFunc(View v) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if ((i + j) % 2 == 1) {     //makes checkered visual
                    refArray[i][j].setBackgroundColor(Color.BLACK);
                } else {
                    refArray[i][j].setBackgroundColor(Color.rgb(222, 184, 135));
                }
                bool[i][j] = false;

            }
        }
        queenCount = 0;
    }

    public void placeQueen(boardTile button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override       //sets a listener on each tile
            public void onClick(View view) {   //when done rendering, all tiles have listener
                boardTile tile = (boardTile) view;   //when clicked in the future


                boolean ok = placeQueenCheck(tile.xCor, tile.yCor);



                if ((tile.xCor + tile.yCor) % 2 == 1) {
                    if (ok) { }
                    else {
                        Toast.makeText(MainActivity.this, "Wrong Move", Toast.LENGTH_SHORT).show();
                    } //do not place
                }
                else {
                    if (ok) { }
                    else {
                        Toast.makeText(MainActivity.this, "Wrong Move", Toast.LENGTH_SHORT).show();

                    }    //do not place
                }


                if (queenCount == 8) {
                    Toast.makeText(MainActivity.this, "You Win!", Toast.LENGTH_SHORT).show();
                }
                else { }
            }

        });

    }
}