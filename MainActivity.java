package com.tylerapps.tyler.sovietgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tylerapps.tyler.sovietgame.gameManager.GameManager;
import com.tylerapps.tyler.sovietgame.unit.IUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.R.attr.max;
import static android.R.attr.path;
import static android.R.attr.port;

public class MainActivity extends AppCompatActivity {

    public static ImageView mainMap;
    public Bitmap drawableBitmap;
    int redValue;
    int greenValue;
    int blueValue;
    int alpha;
    public Color color;
    long startClickTime;
    long MAX_CLICK_DURATION = 200;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AtomicBoolean movingUnits = new AtomicBoolean(false);
        final ArrayList<String> uniqueIdsOfUnitsToMove;
        if(getIntent().hasExtra("movingUnits")){
            Bundle bundle = getIntent().getExtras();
            if (bundle.getBoolean("movingUnits") == true){
                movingUnits.set(true);
            }
        }

        //checks to see if you've been sent an ArrayList<String> with ids of units to send.
        if(getIntent().hasExtra("uniqueIdsOfUnitsToMove")){
            uniqueIdsOfUnitsToMove = getIntent().getStringArrayListExtra("uniqueIdsOfUnitsToMove");

        }else {
            uniqueIdsOfUnitsToMove = null;
        }

        setContentView(R.layout.main_map);
        context = getApplicationContext();
        mainMap = (ImageView) findViewById(R.id.mainMap);
        Bitmap map = loadMainMapFromFile();
        mainMap.setImageBitmap(map);

     // There is a lot going on here... this is listening for any sort of motion on the screen.
        mainMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //Distinguishing between clicks and scrolls. Click only occurs if it lasts less than MAX_CLICK_DURATION.
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if (clickDuration < MAX_CLICK_DURATION) {

                            //click event has occurred

                                //just gets the touch point.


                            Matrix inverse = new Matrix();
                            mainMap.getImageMatrix().invert(inverse);
                            // map touch point from ImageView to image
                            float[] touchPoint = new float[]{motionEvent.getX(), motionEvent.getY()};
                            inverse.mapPoints(touchPoint);
                            // touchPoint now contains x and y in image's coordinate system
                            Point point = new Point((int) touchPoint[0], (int) touchPoint[1]);
                            drawableBitmap = ((BitmapDrawable) mainMap.getDrawable()).getBitmap();

                            //testing new system here:
                            String territory = testThreeClosestPoints(findThreeClosestPoints(drawableBitmap, point), drawableBitmap, point);
                            //end of new system test. It works!

                            //alternate method: if the first method didn't work (the fast way) get the territory name the old, slow, reliable
                            //floodfill way.
                            if (territory.isEmpty())
                                territory = findTerritoryNameFloodFill(touchPoint, motionEvent, point);

                            //You have retrieved either the name of the territory, or an empty string.

                            reactToTerritorySelection(territory, movingUnits, uniqueIdsOfUnitsToMove);

                        }
                    }
                }
                return true;
            }
        });
    }

    //takes in a String with the name of the territory selected. Handles the next event, ie: opens the territory screen, or moves troops to the
    //selected territory. parcelOfUnits can either contain a parcel (containing an ArrayList<IUnit>) or be null.
    public void reactToTerritorySelection(String territory, AtomicBoolean movingUnits, ArrayList<String> idsOfUnitsToMove){

        Intent i = new Intent();
        if (territory != ""){
            if (movingUnits.get() == false){
                openTerritoryScreen(territory);
                //why am I calling finish?
                finish();
            }else {
                //returning data to Territory Activity.
            movingUnits.set(false);
            i.putExtra("territory", territory);
                if (idsOfUnitsToMove != null)
                    i.putStringArrayListExtra("uniqueIdsOfUnitsToMove", idsOfUnitsToMove);
            setResult(1, i);
            //why am I calling finish exactly?
            finish();

            }
        }
    }



                                /*
                                 floodfill algorithm, which was originally designed to change the color of an object. It
                                 needs to change the color, because it looks for the old color and tries to replace it.
                                 If the color doesn't change it gets stuck in an infinite loop. To get around this,
                                 I'm changing the red color by + or - 1, which is imperceptible.

                                 Greg hates this.
                                 */


    public String findTerritoryNameFloodFill(float[] touchPoint, MotionEvent motionEvent, Point point){

        if (touchPoint[0] > 0 && touchPoint[1] > 0){

            int pixel = drawableBitmap.getPixel((int) touchPoint[0], (int) touchPoint[1]);

            redValue = Color.red(pixel);
            blueValue = Color.blue(pixel);
            greenValue = Color.green(pixel);
            alpha = Color.alpha(pixel);
            motionEvent.getX();

            BitmapDrawable drawable = (BitmapDrawable) mainMap.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

            int fakeRed;
            if (redValue > 254) {
                fakeRed = redValue - 1;
            }else {
                fakeRed = redValue + 1;
            }

            String result = floodfill(mutableBitmap, point, Color.argb(alpha, redValue, greenValue, blueValue),
                    Color.argb(alpha, fakeRed, greenValue, blueValue));

            return result;
        }
        Log.e("Error", "Couldn't obtain name of territory");
        return "";
    }




    public static Context getMainContext(){
        return context;
    }

    //considers the distance between the point clicked and the capitals of every territory.
    //returns a hashmap with the names of the three closest territories as keys, and their X and Y coordinates contained
    //in three different ArrayLists.
    public static HashMap<String, ArrayList<Integer>> findThreeClosestPoints(Bitmap image, Point node){
        int width = image.getWidth();
        int height = image.getHeight();

        HashMap<String, Integer> hashMapX = new HashMap<>();
        HashMap<String, Integer> hashMapY = new HashMap();


        int cEuropeX = (int) (width * 0.391);
        hashMapX.put("Central Europe", cEuropeX);
        int cEuropeY = (int) (height * 0.61);
        hashMapY.put("Central Europe", cEuropeY);
        int romaniaX = (int) (width * 0.501);
        hashMapX.put("Romania", romaniaX);
        int romaniaY = (int) (height * 0.721);
        hashMapY.put("Romania", romaniaY);
        int yugoX = (int) (width * 0.421);
        hashMapX.put("Yugoslavia", yugoX);
        int yugoY = (int) (height * 0.822);
        hashMapY.put("Yugoslavia", yugoY);
        int italyX = (int) (width * 0.320);
        hashMapX.put("Italy", italyX);
        int italyY = (int) (height * 0.812);
        hashMapY.put("Italy", italyY);
        int polandX = (int) (width * 0.452);
        hashMapX.put("Poland", polandX);
        int polandY = (int) (height * 0.538);
        hashMapY.put("Poland", polandY);
        int germanyX = (int) (width * 0.320);
        hashMapX.put("Germany", germanyX);
        int germanyY = (int) (height * 0.563);
        hashMapY.put("Germany", germanyY);
        int scandX = (int) (width * 0.351);
        hashMapX.put("Scandinavia", scandX);
        int scandY = (int) (height * 0.235);
        hashMapY.put("Scandinavia", scandY);
        int ukX = (int) (width * 0.191);
        hashMapX.put("UK", ukX);
        int ukY = (int) (height * 0.509);
        hashMapY.put("UK", ukY);
        int franceX = (int) (width * 0.225);
        hashMapX.put("France", franceX);
        int franceY = (int) (height * 0.741);
        hashMapY.put("France", franceY);
        int swissX = (int) (width * 0.286);
        hashMapX.put("Switzerland", swissX);
        int swissY = (int) (height * 0.728);
        hashMapY.put("Switzerland", swissY);
        int portX = (int) (width * 0.067);
        hashMapX.put("Portugal", portX);
        int portY = (int) (height * 0.914);
        hashMapY.put("Portugal", portY);
        int turkeyX = (int) (width * .663);
        hashMapX.put("Turkey", turkeyX);
        int turkeyY = (int) (height * .884);
        hashMapY.put("Turkey", turkeyY);
        int siberiaX = (int) (width * .845);
        hashMapX.put("Siberia", siberiaX);
        int siberiaY = (int) (height * .158);
        hashMapY.put("Siberia", siberiaY);
        int moscowX = (int) (width * .637);
        hashMapX.put("Moscow", moscowX);
        int moscowY = (int) (height * .277);
        hashMapY.put("Moscow", moscowY);
        int stPetersburgX = (int) (width * .514);
        hashMapX.put("St. Petersburg", stPetersburgX);
        int stPetersburgY = (int) (height * .202);
        hashMapY.put("St. Petersburg", stPetersburgY);
        int stalingradX = (int) (width * .698);
        hashMapX.put("Stalingrad", stalingradX);
        int stalinGradY = (int) (height * .556);
        hashMapY.put("Stalingrad", stalinGradY);
        int persiaX = (int) (width * .850);
        hashMapX.put("Persia", persiaX);
        int persiaY = (int) (height * .825);
        hashMapY.put("Persia", persiaY);
        int greeceX = (int) (width * .496);
        hashMapX.put("Greece", greeceX);
        int greeceY = (int) (height * .857);
        hashMapY.put("Greece", greeceY);
        int balkanX = (int) (width * .440);
        hashMapX.put("Balkans", balkanX);
        int balkanY = (int) (height * .378);
        hashMapY.put("Balkans", balkanY);
        int ukraineX = (int) (width * 0.52);
        hashMapX.put("Ukraine", ukraineX);
        int ukraineY = (int) (height * 0.57);
        hashMapY.put("Ukraine", ukraineY);

        double shortestDistance = 10000000;
        double secondShortestDistance = 10000000;
        double thirdShortestDistance = 10000000;

        String closestTerritory = "";
        String secondClosestTerritory = "";
        String thirdClosestTerritory = "";

        //calculates the distance from the point the user clicked to the center of each territory.
        for (String country : hashMapX.keySet()) {
            int xCoordinate = hashMapX.get(country);
            int yCoordinate = hashMapY.get(country);

            int deltaX = Math.abs(node.x - xCoordinate);
            int deltaY = Math.abs(node.y - yCoordinate);

            int deltaXsquared = deltaX * deltaX;
            int deltaYsquared = deltaY * deltaY;

            double distance = Math.sqrt(deltaXsquared + deltaYsquared);

            if (distance < shortestDistance){
                shortestDistance = distance;
                closestTerritory = country;
            }else if(distance < secondShortestDistance){
                secondShortestDistance = distance;
                secondClosestTerritory = country;
            }else if(distance < thirdShortestDistance){
                thirdShortestDistance = distance;
                thirdClosestTerritory = country;
            }
        }


        HashMap<String, ArrayList<Integer>> mapOfCountriesAndCoordinates = new HashMap<String, ArrayList<Integer>>();

        //creating three ArrayLists with two values each: the X and Y coordinates.
        ArrayList<Integer> closestCoordinates = new ArrayList<Integer>();
        closestCoordinates.add(hashMapX.get(closestTerritory));
        closestCoordinates.add(hashMapY.get(closestTerritory));

        ArrayList<Integer> secondClosestCoordinates = new ArrayList<Integer>();
        secondClosestCoordinates.add(hashMapX.get(secondClosestTerritory));
        secondClosestCoordinates.add(hashMapY.get(secondClosestTerritory));

        ArrayList<Integer> thirdClosestCoordinates = new ArrayList<Integer>();
        thirdClosestCoordinates.add(hashMapX.get(thirdClosestTerritory));
        thirdClosestCoordinates.add(hashMapY.get(thirdClosestTerritory));

        mapOfCountriesAndCoordinates.put(closestTerritory, closestCoordinates);
        mapOfCountriesAndCoordinates.put(secondClosestTerritory, secondClosestCoordinates);
        mapOfCountriesAndCoordinates.put(thirdClosestTerritory, thirdClosestCoordinates);

        return mapOfCountriesAndCoordinates;
    }


        //Given three territories and their XY coordinates, tests all three, starting with the closest. First tests
        //whether the pixel the user clicked on is the exact same color as the tested territory's central pixel color.
        //Then, it attempts to "draw an invisible line" between the two points (an L shaped line, not a direct line).
        //If no pixels changed color along the entirety of the line, and the two pixels are the same color, we know we have
        //the correct territory, and we return that String. If none of the three territories in question pass these two tests,
        //we return a null string. The much slower but reliable floodfill test should then be used instead of this method in
        //that case.

    public static String testThreeClosestPoints(HashMap<String, ArrayList<Integer>> threeClosestPoints, Bitmap image, Point node){
        String territoryOne = "";
        String territoryTwo = "";
        String territoryThree = "";

        int territoryOneX = 0;
        int territoryOneY = 0;
        int territoryTwoX = 0;
        int territoryTwoY = 0;
        int territoryThreeX = 0;
        int territoryThreeY = 0;

        for (String key : threeClosestPoints.keySet()) {
            if (territoryOne.isEmpty()){
                territoryOne = key;
            }
            else if (territoryTwo.isEmpty()){
                territoryTwo = key;
            }
            else if (territoryThree.isEmpty())
                territoryThree = key;
        }

        //finally, we have the XY coordinates in a more friendly, usable format...
        territoryOneX = threeClosestPoints.get(territoryOne).get(0);
        territoryOneY = threeClosestPoints.get(territoryOne).get(1);
        territoryTwoX = threeClosestPoints.get(territoryTwo).get(0);
        territoryTwoY = threeClosestPoints.get(territoryTwo).get(1);
        territoryThreeX = threeClosestPoints.get(territoryThree).get(0);
        territoryThreeY = threeClosestPoints.get(territoryThree).get(1);

        //getPixel returns the Color of a given pixel. Here we are conducting the first test - whether the colors of the
        //two pixels are the same - and the second test: if you can draw a line between the two points with no change in color.
       if ((image.getPixel(territoryOneX, territoryOneY) == image.getPixel(node.x, node.y)) && lineTest(image, node, territoryOneX, territoryOneY) == true) {
            return territoryOne;
       }else if ((image.getPixel(territoryTwoX, territoryTwoY) == image.getPixel(node.x, node.y)) && lineTest(image, node, territoryTwoX, territoryTwoY) == true){
           return territoryTwo;
       }else if ((image.getPixel(territoryThreeX, territoryThreeY) == image.getPixel(node.x, node.y)) && lineTest(image, node, territoryThreeX, territoryThreeY) == true){
           return territoryThree;
       }else{
           return "";
       }
    }

    //tests all pixels in a line between two points - if a pixel is found with a different color, returns false. Else, returns true.
    public static boolean lineTest(Bitmap image, Point node, int territoryXCoordinate, int territoryYCoordinate){

        //if the x coordinate of the point selected is to the left of the target, draw a line to the right and test.
        if (node.x < territoryXCoordinate){

            int x = node.x;
            int y = node.y;

            while ( x < territoryXCoordinate )  {
                   if (image.getPixel(x, y) != image.getPixel(territoryXCoordinate, territoryYCoordinate))
                       return false;
                x++;
            }
        }else if (node.x > territoryXCoordinate){
            int x = node.x;
            int y = node.y;

            while ( x > territoryXCoordinate )  {
                if (image.getPixel(x, y) != image.getPixel(territoryXCoordinate, territoryYCoordinate))
                    return false;
                x--;
            }
        }else  if(node.y > territoryYCoordinate){
            int x = node.x;
            int y = node.y;

            while ( y > territoryYCoordinate )  {
                if (image.getPixel(x, y) != image.getPixel(territoryXCoordinate, territoryYCoordinate))
                    return false;
                y--;
            }
        }else if (node.y < territoryYCoordinate){
            int x = node.x;
            int y = node.y;

            while ( y < territoryYCoordinate )  {
                if (image.getPixel(x, y) != image.getPixel(territoryXCoordinate, territoryYCoordinate))
                    return false;
                y++;
            }
        }

        //if it hasn't returned false yet, it has passed the test and there should be no pixels of a different color in between. Return true.
        return true;

    }


    /**
     * This class searches a given country area by using an MS-paint style algorithm - the paintcan. Given
     * a point, it flood fills the shape with a new color. It needs to actually change a color, or it will
     * run in an infinite loop. I use this algorithm to detect given points on the map - every time it changes a pixel's color,
     * it asks - is this one of these 15 or so pre-determined pixels? (linked to various countries)
     * If so, it returns a string identifying the country in question. If not, it returns "". To actually change a country's
     * color, you need to pass in a new replacementColor. A hack I use is to change the red value + or - 1:
     * this allows the algorithm to work without the user noticing the country has changed color.
     * @param image
     * @param node
     * @param targetColor
     * @param replacementColor
     * @return
     */



    public static String floodfill(Bitmap image, Point node, int targetColor,
                          int replacementColor) {

        String result = "";
        int width = image.getWidth();
        int height = image.getHeight();

        int cEuropeX = (int) (width * 0.391);
        int cEuropeY = (int) (height * 0.61);
        int romaniaX = (int) (width * 0.501);
        int romaniaY = (int) (height * 0.721);
        int yugoX = (int) (width * 0.421);
        int yugoY = (int) (height * 0.822);
        int italyX = (int) (width * 0.320);
        int italyY = (int) (height * 0.812);
        int polandX = (int) (width * 0.452);
        int polandY = (int) (height * 0.538);
        int germanyX = (int) (width * 0.320);
        int germanyY = (int) (height * 0.563);
        int scandX = (int) (width * 0.351);
        int scandY = (int) (height * 0.235);
        int ukX = (int) (width * 0.191);
        int ukY = (int) (height * 0.509);
        int franceX = (int) (width * 0.225);
        int franceY = (int) (height * 0.741);
        int swissX = (int) (width * 0.286);
        int swissY = (int) (height * 0.728);
        int portX = (int) (width * 0.067);
        int portY = (int) (height * 0.914);
        int turkeyX = (int) (width * .663);
        int turkeyY = (int) (height * .884);
        int siberiaX = (int) (width * .845);
        int siberiaY = (int) (height * .158);
        int moscowX = (int) (width * .637);
        int moscowY = (int) (height * .277);
        int stPetersburgX = (int) (width * .514);
        int stPetersburgY = (int) (height * .202);
        int stalingradX = (int) (width * .698);
        int stalinGradY = (int) (height * .556);
        int persiaX = (int) (width * .850);
        int persiaY = (int) (height * .825);
        int greeceX = (int) (width * .496);
        int greeceY = (int) (height * .857);
        int balkanX = (int) (width * .440);
        int balkanY = (int) (height * .378);
        int ukraineX = (int) (width * 0.52);
        int ukraineY = (int) (height * 0.57);

        int target = targetColor;
        int replacement = replacementColor;

        if (target != replacement) {
            Queue<Point> queue = new LinkedList<Point>();
            do {
                int x = node.x;
                int y = node.y;
                while (x > 0 && image.getPixel(x - 1, y) == target) {
                    x--;
                }

                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && image.getPixel(x, y) == target) {


                    if (x == cEuropeX && y == cEuropeY){
                        Log.d("test", "this is Central Europe");
                        result = "Central Europe";
                    }

                    if (x == romaniaX && y == romaniaY){
                        Log.d("test", "this is Romania");
                        result = "Romania";
                    }

                    if (x == yugoX && y == yugoY){
                        Log.d("test", "this is Yugoslavia");
                        result = "Yugoslavia";
                    }

                    if (x == italyX && y == italyY){
                        Log.d("test", "this is Italy");
                        result = "Italy";
                    }

                    if (x == polandX && y == polandY){
                        Log.d("test", "this is Poland");
                        result = "Poland";
                    }

                    if (x == germanyX && y == germanyY){
                        Log.d("test", "this is Germany");
                        result = "Germany";
                    }

                    if (x == scandX && y == scandY){
                        Log.d("test", "this is Scandinavia ");
                        result = "Scandinavia";
                    }

                    if (x == ukX && y == ukY){
                        Log.d("test", "this is the United Kingdom");
                        result = "UK";
                    }

                    if (x == franceX && y == franceY){
                        Log.d("test", "this is France");
                        result = "France";
                    }

                    if (x == swissX && y == swissY){
                        Log.d("test", "this is Switzerland");
                        result = "Switzerland";
                    }

                    if (x == portX && y == portY){
                        Log.d("test", "this is Portugal");
                        result = "Portugal";
                    }

                    if (x == siberiaX && y == siberiaY){
                        Log.d("test", "this is Siberia");
                        result = "Siberia";
                    }

                    if (x == moscowX && y == moscowY){
                        Log.d("test", "this is Moscow");
                        result = "Moscow";
                    }

                    if (x == stPetersburgX && y == stPetersburgY){
                        Log.d("test", "this is St. Petersburg");
                        result = "St. Petersburg";
                    }

                    if (x == stalingradX && y == stalinGradY){
                        Log.d("test", "this is StalinGrad");
                        result = "Stalingrad";
                    }

                    if (x == persiaX && y == persiaY){
                        Log.d("test", "this is Persia");
                        result = "Persia";
                    }

                    if (x == turkeyX && y == turkeyY){
                        Log.d("test", "this is Turkey");
                        result = "Turkey";
                    }

                    if (x == greeceX && y == greeceY){
                        Log.d("test", "this is Greece");
                        result = "Greece";
                    }

                    if (x == balkanX && y == balkanY){
                        Log.d("test", "this is the Balkans");
                        result = "Balkans";
                    }

                    if (x == ukraineX && y == ukraineY){
                        Log.d("test", "This is Ukraine");
                        result = "Ukraine";
                    }

                    /*
                    Change color
                     */
                    image.setPixel(x, y, replacement);
                    if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
                        queue.add(new Point(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && image.getPixel(x, y - 1) != target) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && image.getPixel(x, y + 1) == target) {
                        queue.add(new Point(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && image.getPixel(x, y + 1) != target) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        }


        saveMap(image);

        return result;
    }

    public void openTerritoryScreen(String territoryName){

        Intent myIntent = new Intent(MainActivity.this, TerritoryActivity.class);
        myIntent.putExtra("territoryName", territoryName); //Optional parameters
        startActivity(myIntent);
        //MainActivity.this.startActivity(myIntent);

    }

    public static Bitmap getMap(){

        BitmapDrawable drawable = (BitmapDrawable) mainMap.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        return mutableBitmap;

    }

    public static void saveMap(Bitmap image){

        try {
// Use the compress method on the Bitmap object to write image to
// the OutputStream
            FileOutputStream fos = context.openFileOutput("mainMapFile.png", Context.MODE_PRIVATE);

// Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
        }
    }

    public static void resetMap(View v){
        Bitmap startingMap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.startingborders);

        saveMap(startingMap);
        refreshMap();

    }

    public static void refreshMap(){
        Bitmap map = loadMainMapFromFile();
        mainMap.setImageBitmap(map);
    }


    public static Bitmap loadMainMapFromFile() {
        String filename = "mainMapFile.png";
        Bitmap thumbnail = null;

// If no file on external storage, look in internal storage
        if (thumbnail == null) {
            try {
                File filePath = context.getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //if after all of this you can't find a saved map, use the starting borders.
        if (thumbnail == null){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.startingborders);
            thumbnail = bitmap;
        }
        return thumbnail;
    }



    public static void endTurn(View v){
        GameManager gm = GameManager.getGameManager();
        gm.endTurn();
    }




}
