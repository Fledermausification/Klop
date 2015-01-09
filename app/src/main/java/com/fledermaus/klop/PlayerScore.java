package com.fledermaus.klop;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by James on 1/8/2015.
 */
public class PlayerScore {
    public static final int INITIAL_NUMBER_OF_SCORES = 10;
    public static final int MAX_NUMBER_OF_SCORES     = 25;
    public static final int WINNING_SCORE            = 50;
    public static final int MAX_MISS_COUNT           = 3;
    public static final int MISS_PENALTY             = 10;
    public static final int OVERSHOT_SCORE           = 25;
    public static final int SCORE_ID_OFFSET          = 1234567;
    public static final int PLAYER_ID_OFFSET         = 50;

    public static final LinearLayout.LayoutParams LAYOUT_PARAMS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private static boolean ignore = true;

    private LinearLayout         nameLayout;
    private LinearLayout         scoreLayout;
    private String               name;
    private TextView             nameLabel;
    private int                  totalScore;
    private TextView             totalScoreLabel;
    private ArrayList <EditText> scores;
    private int                  numScores;
    private int                  playerNumber;
    private int                  numPlayers;
    private int                  idOffset;

    private MainActivity         parentMainActivity;
    private LinearLayout         parentNameLayout;
    private LinearLayout         parentScoreLayout;

    public PlayerScore(MainActivity ma, LinearLayout nl, LinearLayout sl, String n, int p, int m) {
        parentMainActivity = ma;
        parentNameLayout   = nl;
        parentScoreLayout  = sl;
        name               = n;
        playerNumber       = p;
        numPlayers         = m;
        idOffset           = ((playerNumber + 1) % numPlayers) * PLAYER_ID_OFFSET;

        //Create the name wrapper Layouts
        nameLayout = new LinearLayout(parentMainActivity);
        nameLayout.setLayoutParams(LAYOUT_PARAMS);
        nameLayout.setPadding(0, 23, 0, 22);
        parentNameLayout.addView(nameLayout);
        scoreLayout = new LinearLayout(parentMainActivity);
        scoreLayout.setLayoutParams(LAYOUT_PARAMS);
        parentScoreLayout.addView(scoreLayout);

        //Create the players name
        nameLabel = new TextView(parentMainActivity);
        nameLabel.setLayoutParams(LAYOUT_PARAMS);
        n = name + ":";
        SpannableString ss = new SpannableString(n); //Make a SpannableString so that we can bold the name
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, n.length(), 0);
        nameLabel.setText(ss);
        nameLayout.addView(nameLabel);

        //Create the total score
        totalScore      = 0;
        totalScoreLabel = new TextView(parentMainActivity);
        totalScoreLabel.setLayoutParams(LAYOUT_PARAMS);
        String s = Integer.toString(totalScore);
        ss = new SpannableString(s); //Make a SpannableString so that we can bold the score
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        totalScoreLabel.setText(ss);
        totalScoreLabel.setPadding(16, 0, 8, 0);
        nameLayout.addView(totalScoreLabel);

        //Create the score inputs
        numScores = 0;
        scores    = new ArrayList <> ();
        for (int i = 0; i < INITIAL_NUMBER_OF_SCORES; ++i) {
            addNewScoreRow();
        }
    }

    public void addNewScoreRow(){
        EditText e = new EditText(parentMainActivity);
        e.setLayoutParams(LAYOUT_PARAMS);
        e.setInputType(InputType.TYPE_CLASS_PHONE);
        e.setHint("     ");
        e.setId(SCORE_ID_OFFSET + (playerNumber * PLAYER_ID_OFFSET) + numScores);
        int next;
        if ((playerNumber + 1) % numPlayers != 0)
            next = SCORE_ID_OFFSET + idOffset + numScores++;
        else
            next = SCORE_ID_OFFSET + ++numScores;
        e.setNextFocusForwardId(next);
        e.setNextFocusDownId(next);

        //Focus change listener
        e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final EditText    e    = (EditText) v;
                    if (totalScore != 50) {
                        String s = e.getText().toString();

                        //Make sure X's aren't checked as invalid
                        if (!s.equals("X")) {
                            //Check the input is valid
                            try {
                                int i = Integer.parseInt(s); //Invalid int's will throw a NumberFormatException
                                if (i < 0 || i > 12) {
                                    //Just throwing this because it's easier than duplicating the cleanup code
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException ex) {
                                e.setTextColor(Color.RED);
                                //Really ugly hack to return the focus...
                                ignore = false;
                                e.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!ignore) {
                                            e.requestFocus();
                                            ignore = true;
                                        }
                                    }
                                });

                                return;
                            }
                        }

                        e.setTextColor(Color.BLACK);
                    }
                    else {
                        //Players score is already 50, ignore the input
                        e.setText("");
                    }
                    //Update the score. Do it for 50 scores as well in-case something was a typo
                    updateTotalScore();

                    //Check if we need to add another score row
                    if (scores.indexOf(e) == scores.size() - 1 && numScores < MAX_NUMBER_OF_SCORES) {
                        parentMainActivity.newScoreRowForAllPlayers();
                    }
                }
            }
        });

        scoreLayout.addView(e);
        scores.add(e);
    }

    private void updateTotalScore() {
        int missCount = 0;
        totalScore = 0;
        for (EditText e : scores) {
            String s = e.getText().toString();
            try {
                int i;
                if (s.equals("X"))
                    i = 0;
                else
                    i = Integer.parseInt(s);
                totalScore += i;

                if (i == 0) {
                    if (++missCount == MAX_MISS_COUNT) {
                        //Deduct the penalty
                        totalScore -= MISS_PENALTY;
                        missCount = 0;
                        //Turn the penalty 0 into an X, to help readability
                        e.setText("X");
                        e.setTextColor(Color.MAGENTA);
                    }
                }
            }
            catch (NumberFormatException ex) {}

            if (totalScore == WINNING_SCORE) {
                e.setTextColor(Color.GREEN);
                totalScoreLabel.setTextColor(Color.GREEN);
            }
            else if (totalScore > WINNING_SCORE) {
                e.setTextColor(Color.YELLOW);
                totalScore = OVERSHOT_SCORE;
            }
        }
        totalScoreLabel.setText(Integer.toString(totalScore));
    }
}