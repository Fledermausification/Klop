package com.fledermaus.klop;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by James on 1/8/2015.
 */
public class PlayerScore {
    public static final int INITIAL_NUMBER_OF_SCORES = 10;
    public static final int WINNING_SCORE            = 50;
    public static final int MAX_MISS_COUNT           = 3;
    public static final int MISS_PENALTY             = -10;

    private String               name;
    private TextView             nameLabel;
    private ArrayList <EditText> scores;

    private MainActivity         belongsTo;
    private LinearLayout         inLayout;

    public PlayerScore() {

    }

    public EditText addNewScoreRow() {
        return null;
    }
}