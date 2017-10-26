package com.tylerapps.tyler.sovietgame;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tylerapps.tyler.sovietgame.events.IEvent;
import com.tylerapps.tyler.sovietgame.gameManager.GameManager;

public class RandomEvent extends AppCompatActivity implements IEvent {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_event);



        //note: make some functionality to get this from somewhere
        int year = 1933;

        //arraylist of events? Hashmap of events, with event and year linked?
}

    public void setImage(Bitmap bitmap){

        ImageView imageView = (ImageView) this.findViewById(R.id.random_image);
        imageView.setImageBitmap(bitmap);


    }

    @Override
    public void setText(String eventText, String[] options) {

    }

    @Override
    public int getSelectedOption() {
        return 0;
    }

    @Override
    public void selectOption(int option) {

    }

    @Override
    public String[] getOptions() {
        return new String[0];
    }

    public void setText(String eventText, String optionText1, String optionText2){

        TextView mainText = (TextView) this.findViewById(R.id.event_text);
        Button option1 = (Button) this.findViewById(R.id.Next);
        Button option2 = (Button) this.findViewById(R.id.Hide);

        mainText.setText(eventText);
        option1.setText(optionText1);
        option2.setText(optionText2);

    }

    public void option1Selected(){

    }

    public void getStrings(){

    }

    public void option2Selected(){




    }

    @Override
    public String executeChoice(GameManager manager, int choiceNumber) {
        return null;
    }


}
