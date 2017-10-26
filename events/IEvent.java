package com.tylerapps.tyler.sovietgame.events;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tylerapps.tyler.sovietgame.gameManager.GameManager;

/**
 * Created by Tyler on 5/25/2017.
 */

public interface IEvent {

    public void setImage(Bitmap bitmap);
    public void setText(String eventText, String[] options);
    /*
     Returns -1 if no option has been selected yet.
     */
    public int getSelectedOption();
    public void selectOption(int option);
    public String[] getOptions();

    /**
     * Executes the decision the user made and returns any text that should be displayed on the screen.
     * @return
     */
    public String executeChoice(GameManager manager, int choiceNumber);
}