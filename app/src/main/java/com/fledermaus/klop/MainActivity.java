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
    private LinearLayout[] playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("\n\nNEW SCORE!\n\n");

        Intent intent = getIntent();
        numPlayers = intent.getIntExtra(NewGameActivity.EXTRA_PLAYER_NUMBER, 0);

        if (numPlayers > 0) {
            System.out.println("Going to create a new game for " + numPlayers + " Players!");

            LinearLayout              layout       = (LinearLayout) findViewById(R.id.scores);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView                  titleText    = new TextView(this);

            layout.removeAllViews();

            titleText.setLayoutParams(layoutParams);
            titleText.setText("Scores:");
            layout.addView(titleText);

            String[] names = intent.getStringArrayExtra(NewGameActivity.EXTRA_PLAYER_NAMES);

            playerScore = new LinearLayout[numPlayers];
            for (int i = 0; i < numPlayers; ++i) {
                playerScore[i] = new LinearLayout(this);
                playerScore[i].setLayoutParams(layoutParams);
                playerScore[i].setHorizontalGravity(LinearLayout.HORIZONTAL);
                layout.addView(playerScore[i]);

                TextView nameText = new TextView(this);
                nameText.setLayoutParams(layoutParams);
                SpannableString n = new SpannableString(names[i]);
                n.setSpan(new StyleSpan(Typeface.BOLD), 0, n.length(), 0);
                nameText.setText(n);
                playerScore[i].addView(nameText);

                for (int j = 0; i < 10; ++j) {

                }
            }


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