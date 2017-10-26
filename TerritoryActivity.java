package com.tylerapps.tyler.sovietgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.tylerapps.tyler.sovietgame.building.Building;
import com.tylerapps.tyler.sovietgame.country.Country;
import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.gameManager.GameManager;
import com.tylerapps.tyler.sovietgame.territory.ITerritory;
import com.tylerapps.tyler.sovietgame.unit.IUnit;
import com.tylerapps.tyler.sovietgame.unit.UnitType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 The territory screen
 */
public class TerritoryActivity extends AppCompatActivity {

    public ITerritory myTerritory = null;
    //if you store unitsToMove up here as an instance variable, it will be null when you return to this activity. Is there a way to preserve
    //that data without passing it to MainActivity?

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        //returns the string value of the name of the territory
        //todo: it just pauses here... wtf.  java.io.FileNotFoundException: /data/user/0/com.tylerapps.tyler.sovietgame/files/mainMapFile.png (No such file or directory)
        String value = intent.getStringExtra("territoryName"); //if it's a string you stored.
        GameManager gm = GameManager.getGameManager();

        //the territory
        myTerritory = gm.getTerritoryFromString(value);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.territory_menu);

        //sets the title
        TextView title = (TextView) findViewById(R.id.TerritoryName);
        title.setText(myTerritory.getName());

        //loads the buildings in the territory
        TextView buildings = (TextView) findViewById(R.id.buildings);
        List<Building> z = myTerritory.getBuildings();
        String buildingText = "";

        for(int i = 0; i < z.size(); i++){
            buildingText =  buildingText + z.get(i).name() + " ";
        }
            buildings.setText(buildingText);



        //sets the unit text = to the units in the territory
        //// TODO: 8/7/2017 this is not very flexible or sustainable. Fix this.
       // populateUnitBox(myTerritory, (LinearLayout) findViewById(R.id.UnitLinearLayout));

        /*



        currentUnitToDisplay = 0;
        if (numberOfGermanInfantry > 0){
            unitTextViews.get(currentUnitToDisplay).setText(Integer.toString(numberOfGermanInfantry) + " German Infantry");
            unitTextViews.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            currentUnitToDisplay++;
        }

        if (numberOfGermanTanks > 0){
            unitTextViews.get(currentUnitToDisplay).setText(Integer.toString(numberOfGermanTanks) + " German Tanks");
            unitTextViews.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            currentUnitToDisplay++;
        }


        if (numberOfSovietInfantry> 0){
            unitTextViews.get(currentUnitToDisplay).setText(Integer.toString(numberOfSovietInfantry) + " Soviet Infantry");
            unitTextViews.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            seekbars.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            seekbars.get(currentUnitToDisplay).setMax(numberOfSovietInfantry);
            unitNumberViews.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            unitNumberViews.get(currentUnitToDisplay).setText("0");
//            unitNumberViews.get(currentUnitToDisplay).setText(String.valueOf(progress));

            seekbarListener1.onProgressChanged(seekbarListeners.get(currentUnitToDisplay), seekbars.get(currentUnitToDisplay).getProgress(), true) ;
            seekbars.get(currentUnitToDisplay).setOnSeekBarChangeListener(seekbarListener1);

            currentUnitToDisplay++;
        }


        if (numberOfSovietTanks> 0){
            unitTextViews.get(currentUnitToDisplay).setText(Integer.toString(numberOfSovietTanks) + " Soviet Tanks");
            unitTextViews.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            seekbars.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            seekbars.get(currentUnitToDisplay).setMax(numberOfSovietTanks);
            unitNumberViews.get(currentUnitToDisplay).setVisibility(View.VISIBLE);
            unitNumberViews.get(currentUnitToDisplay).setText("0");

            seekbars.get(currentUnitToDisplay).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    // TODO Auto-generated method stub

                    unitNumberViews.get(currentUnitToDisplay).setText(String.valueOf(progress));



                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            currentUnitToDisplay++;
        }

        */


        // ts the currently building interface

        populateUnitBox(myTerritory);

        String currentlyBuilding = "";
        Building building = myTerritory.getCurrentBuilding();
        currentlyBuilding = building.getName();

        TextView currentBuildingTitle = (TextView) findViewById(R.id.currentBuilding);
        currentBuildingTitle.setText(currentlyBuilding);

        //sets the turns remaining text
        TextView turnsToGo = (TextView) findViewById(R.id.turnsRemaining);
        int turnsRemaining = myTerritory.getBuildingTurnsRemaining();
        turnsToGo.setText(String.valueOf(turnsRemaining));

        //sets the image for current Building
        //// TODO: 8/6/2017 fix this part
        ImageView currentBuildingImage = (ImageView) findViewById(R.id.currentBuildingImage);
        String name = building.getName();

        //sets the hammers so far and total needed
        String hammersApplied = String.valueOf(myTerritory.getHammersApplied());
        String hammersNeeded = String.valueOf(myTerritory.getCurrentBuilding().getCost());
        String hammerDisplayTxt = hammersApplied + "/" + hammersNeeded;
        TextView hammerDisplay = (TextView) findViewById(R.id.hammerDisplay);
        hammerDisplay.setText(hammerDisplayTxt);

        //sets the hammers and beakers per turn
        TextView hammersPerTurn = (TextView) findViewById(R.id.HammersPerTurn);
        TextView beakersPerTurn = (TextView) findViewById(R.id.BeakersPerTurn);
        String hammers = String.valueOf(myTerritory.getHammerProduction());
        String beakers = String.valueOf(myTerritory.getCurrentScience());
        hammersPerTurn.setText(hammers);
        beakersPerTurn.setText(beakers);

        //set the occupying country icon
        ImageView occupyingCountry = (ImageView) findViewById(R.id.occupyingCountryIcon);
        Country occupier = (Country) myTerritory.getCountry();
        if (occupier.name.equals("USSR"))
            occupyingCountry.setImageResource(R.drawable.hammer_and_sickle_smaller);

        if (occupier.name.equals("Germany"))
            occupyingCountry.setImageResource(R.drawable.ironcrossicon);




    }

    /**
        Cycles through a territory, finding lists of unitTypes by country. Eg: finds all soviet tanks, all American bombers, etc. Using
        data, passes unitType, count, country and a layout to addToUnitBox (method that displays these things on the screen).
     */

    public HashMap<String, ArrayList> getUnitDataArrayLists(){

        TextView unit1 = (TextView) findViewById(R.id.Unit1);
        TextView unit2 = (TextView) findViewById(R.id.Unit2);
        TextView unit3 = (TextView) findViewById(R.id.Unit3);
        TextView unit4 = (TextView) findViewById(R.id.Unit4);
        TextView unit5 = (TextView) findViewById(R.id.Unit5);
        TextView unit6 = (TextView) findViewById(R.id.Unit6);

        SeekBar bar1 = (SeekBar) findViewById(R.id.unit1bar);
        SeekBar bar2 = (SeekBar) findViewById(R.id.unit2bar);
        SeekBar bar3 = (SeekBar) findViewById(R.id.unit3bar);
        SeekBar bar4 = (SeekBar) findViewById(R.id.unit4bar);
        SeekBar bar5 = (SeekBar) findViewById(R.id.unit5bar);
        SeekBar bar6 = (SeekBar) findViewById(R.id.unit6bar);

        ArrayList<TextView> unitTextViews = new ArrayList<TextView>();
        unitTextViews.add(unit1);
        unitTextViews.add(unit2);
        unitTextViews.add(unit3);
        unitTextViews.add(unit4);
        unitTextViews.add(unit5);
        unitTextViews.add(unit6);

        ArrayList<SeekBar> seekbars = new ArrayList<SeekBar>();
        seekbars.add(bar1);
        seekbars.add(bar2);
        seekbars.add(bar3);
        seekbars.add(bar4);
        seekbars.add(bar5);
        seekbars.add(bar6);

        final TextView unit1num = (TextView) findViewById(R.id.Unit1Number);
        final TextView unit2num = (TextView) findViewById(R.id.Unit2Number);
        final TextView unit3num = (TextView) findViewById(R.id.Unit3Number);
        final TextView unit4num = (TextView) findViewById(R.id.Unit4Number);
        final TextView unit5num = (TextView) findViewById(R.id.Unit5Number);
        final TextView unit6num = (TextView) findViewById(R.id.Unit6Number);


        final ArrayList<TextView> unitNumberViews = new ArrayList<TextView>();
        unitNumberViews.add(unit1num);
        unitNumberViews.add(unit2num);
        unitNumberViews.add(unit3num);
        unitNumberViews.add(unit4num);
        unitNumberViews.add(unit5num);
        unitNumberViews.add(unit6num);

        HashMap<String, ArrayList> mapOfArrayLists = new HashMap<>();
        mapOfArrayLists.put("unitTextViews", unitTextViews);
        mapOfArrayLists.put("seekbars", seekbars);
        mapOfArrayLists.put("unitNumberViews", unitNumberViews);

        return mapOfArrayLists;
    }

    public void populateUnitBox(ITerritory territory){





        HashMap<String, ArrayList> unitData = getUnitDataArrayLists();
        ArrayList<TextView> unitTextViews = unitData.get("unitTextViews");
        final ArrayList<SeekBar> seekbars = unitData.get("seekbars");
        final ArrayList<TextView> unitNumberViews = unitData.get("unitNumberViews");

        for (int i = 0; i < unitTextViews.size(); i++){
            unitTextViews.get(i).setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < seekbars.size(); i++){
            seekbars.get(i).setVisibility(View.INVISIBLE);
            seekbars.get(i).setProgress(0);
        }

        for (int i = 0; i < unitNumberViews.size(); i++){
            unitNumberViews.get(i).setVisibility(View.INVISIBLE);
        }

        seekbars.get(0).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                unitNumberViews.get(0).setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbars.get(1).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                unitNumberViews.get(1).setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbars.get(2).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                unitNumberViews.get(2).setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbars.get(3).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                unitNumberViews.get(3).setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbars.get(4).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                unitNumberViews.get(4).setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbars.get(5).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                unitNumberViews.get(5).setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        GameManager gm = GameManager.getGameManager();


        for(ICountry c : gm.countries.values()) {
            for (UnitType t : UnitType.values()) {
                List<IUnit> units = territory.getUnitsByTypeAndCountry(t, c);
                if (units== null || units.isEmpty()){
                    continue;
                }else {
                    //if you're here, you have list containing something like "2 russian tanks" or "3 american bombers"
                    populateNextUnitView(units.size(), c, t, unitTextViews, seekbars, unitNumberViews);
                }
            }
        }
    }

    /**
    cycles through the eight possible views for units. Checks each (in order) to see if they are invisible
     If a view is visible, that means that there is currently a unit occupying that view: move on to the next one.
     When you put a unit in a view, simply make it visible.


     */
    public void populateNextUnitView(int numberOfUnits, ICountry country, UnitType unitType, ArrayList<TextView> unitTextViews, ArrayList<SeekBar> seekbars, ArrayList<TextView> unitNumberViews){
        for (int i = 0; i < unitTextViews.size(); i++){
           if (unitTextViews.get(i).getVisibility() == View.VISIBLE){
               continue;
           }else{
               String name = "";
               if (numberOfUnits <= 1){
                   name = unitType.getName();
               }else name = unitType.getPluralName();

               unitTextViews.get(i).setText(numberOfUnits + " " + country.getPluralName()+ " " + name);
               unitTextViews.get(i).setVisibility(View.VISIBLE);
               TextViewMetadata meta = new TextViewMetadata(country, numberOfUnits, unitType);
               unitTextViews.get(i).setTag(meta);


               //if it's the soviet union, give em sliders
               if (country.toString().equals("USSR")){
                   seekbars.get(i).setVisibility(View.VISIBLE);
                   seekbars.get(i).setMax(numberOfUnits);
                   seekbars.get(i).setProgress(0);

                   unitNumberViews.get(i).setVisibility(View.VISIBLE);
                   unitNumberViews.get(i).setText("0");
               }

               break;
           }
        }
    }

    private class TextViewMetadata{
        public ICountry country;
        public int count;
        public UnitType unitType;

        public TextViewMetadata(ICountry country, int count, UnitType unitType) {
            this.country = country;
            this.count = count;
            this.unitType = unitType;
        }
    }

    /**
     * Handler for the move units button. When first pressed,
     * sliders appear for all Russian units and the button text turns to "Move".
     * On second press, we go back to the territory screen.
     * @param v
     */
    public void move(View v){
        
        HashMap<String, ArrayList> unitData = getUnitDataArrayLists();


        ArrayList<TextView> unitTextViews = unitData.get("unitTextViews");
        ArrayList<SeekBar> seekbars = unitData.get("seekbars");
        ArrayList<TextView> unitNumberViews = unitData.get("unitNumberViews");

        //the list of units to move.
        ArrayList<IUnit> units = new ArrayList<IUnit>();


        //Cycle through the sliders. Check if they're visible - if they are, well, they're soviet units.
        //Check the progress on the sliders - use that data to generate a list of the units to move.


        for (int i = 0; i < seekbars.size(); i++){
           if (seekbars.get(i).getVisibility() == View.VISIBLE){
               int numberToMove = seekbars.get(i).getProgress();
               TextViewMetadata meta = (TextViewMetadata) unitTextViews.get(i).getTag();
               UnitType unitType = meta.unitType;
               ICountry country = meta.country;

               //list1 is a list of all of the units in the territory represented by the slider and textview.
               //we are iterating through all of units in that list and adding the number represented on the slider
               //to our master list of units to move (the list called "units")
               List<IUnit> list1 = myTerritory.getUnitsByTypeAndCountry(unitType, country);
               for (int z = 0; z < numberToMove; z++){
                   units.add(list1.get(z));
               }

           }
        }

        //at this point, you have your list of units to move. Congrats!

        //We are going to startActivityForResult: starts and activity and waits for the response, then executes code in OnActivityResult.

        if (units.isEmpty() == false){

            Intent i = new Intent(this, MainActivity.class);
            ArrayList<String> uniqueIdsToMove = new ArrayList<>();

            for (int z = 0; z < units.size(); z++){
                uniqueIdsToMove.add(units.get(z).getId());
            }

            i.putStringArrayListExtra("uniqueIdsOfUnitsToMove", uniqueIdsToMove);
            i.putExtra("movingUnits", true);

            //// TODO: 8/10/2017 Here, I am storing the unit list and the currentTerritory we are moving from at the top of the screen,
            //because I'm not sure how else I should store them as I wait for the ActivityResult. I'm also not sure how to pass
            //this data to OnActivityResult. Get some advice on this, there are probably some better ways to do this.
            startActivityForResult(i, 99);

        }

        //Hold that list. You now need to find the territory to move to.
        //Open the map. Message the user: select a territory to move to -
        //if they selected the wrong territory, tell them to select a territory adjacent to
        //[name of current territory]. If they selected an appropriate territory, move the units.
        //refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        switch(requestCode) {
            case 99:


                GameManager gm = GameManager.getGameManager();
                Bundle b = i.getExtras();
                String territory = (String) b.get("territory");
                //ITerritory toMove = gm.getTerritoryFromString(territory);

                ArrayList<String> idsOfUnits = b.getStringArrayList("uniqueIdsOfUnitsToMove");
                //problem here: units to move has duplicates.
                ArrayList<IUnit> unitsToMove = myTerritory.getUnitsFromIds(idsOfUnits);
                ITerritory toMove = gm.getTerritoryFromString(b.getString("territory"));

                //Here is where you actually move units.
                gm.moveUnits(unitsToMove, myTerritory, toMove);

                List<IUnit> movedFrom = myTerritory.getUnits();

                List<IUnit> movedTo = toMove.getUnits();

                //finish();
                refresh();

                //by this point, we've moved the units. Great. So, now I'm opening the territory screen of the territory you moved the units to. Not too sure about that.
                Intent myIntent = new Intent(TerritoryActivity.this, TerritoryActivity.class);
                myIntent.putExtra("territoryName", territory); //Optional parameters
                TerritoryActivity.this.startActivity(myIntent);

                //at this point, if the user presses the back button, it will exit the app. We've closed everything in the backstack.
                //// TODO: 8/15/2017 add a back button handler

                //when does this get called? Does it get called after the user leaves the next territory screen?
                finish();
                break;
        }
    }

    public void openMainMap(View v){
        Intent myIntent = new Intent(TerritoryActivity.this, MainActivity.class);

    }




    /**
     * Create sliders next to each russian unit so they can be moved.
     */

    /*
    private void createSliders(){
        //RelativeLayout view = (RelativeLayout) findViewById(R.id.UnitRelativeLayout);
        int childCount = view.getChildCount();
        for(int i =0; i < childCount; i++){

            Object o = view.getChildAt(i);
            if(!(o instanceof TextView))
                continue;
            TextView unitView = (TextView)view.getChildAt(i);
            TextViewMetadata meta = (TextViewMetadata)unitView.getTag();
            System.out.println(meta.unitType.name);






            //if(!country.toString().equals("USSR"))
            //    continue;

        }
    }

    */

    public void refresh(){
        recreate();
    }


}
