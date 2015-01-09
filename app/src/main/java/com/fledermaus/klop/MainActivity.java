package com.fledermaus.klop;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private int numPlayers = 0;
    private PlayerScore[] playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        numPlayers = intent.getIntExtra(NewGameActivity.EXTRA_PLAYER_NUMBER, 0);

        if (numPlayers > 0) {
            LinearLayout              nameLayout   = (LinearLayout) findViewById(R.id.names);
            LinearLayout              scoreLayout  = (LinearLayout) findViewById(R.id.scores);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView                  titleText    = new TextView(this);

            titleText.setLayoutParams(layoutParams);
            titleText.setText("Scores:");
            //layout.addView(titleText);

            String[] names = intent.getStringArrayExtra(NewGameActivity.EXTRA_PLAYER_NAMES);

            playerScore = new PlayerScore[numPlayers];
            for (int i = 0; i < numPlayers; ++i) {
                playerScore[i] = new PlayerScore(this, nameLayout, scoreLayout, names[i], i, numPlayers);
            }
        }
    }

    public void newScoreRowForAllPlayers() {
        for (PlayerScore p : playerScore) {
            p.addNewScoreRow();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new_game:
                openNewGame();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openNewGame() {
        System.out.println("New game!");
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    public void openSettings() {
        System.out.println("Settings!");
    }
}