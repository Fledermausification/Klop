package com.fledermaus.klop;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by James on 1/8/2015.
 */
public class PlayerScore {
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