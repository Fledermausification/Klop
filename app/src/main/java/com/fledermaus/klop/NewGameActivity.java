package com.fledermaus.klop;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class NewGameActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    public final static int    MAX_PLAYERS           = 12;
    public final static int    PLAYER_NAME_ID_OFFSET = 6666666;
    public final static String EXTRA_PLAYER_NAMES    = "com.fledermaus.klop.PLAYER_NAMES_MESSAGE";
    public final static String EXTRA_PLAYER_NUMBER   = "com.fledermaus.klop.PLAYER_NUMBER_MESSAGE";

    private EditText[] playerNames;
    private int        numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Spinner spinner = (Spinner) findViewById(R.id.number_of_players_spinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_players_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //Set the callback for selected items
        spinner.setOnItemSelectedListener(this);

        //Setup the inputs for player names
        LinearLayout              layout       = (LinearLayout) findViewById(R.id.player_names_input);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        InputFilter[]             inputFilters = {new InputFilter.LengthFilter(8)};

        playerNames = new EditText[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; ++i) {
            playerNames[i] = new EditText(this);
            playerNames[i].setLayoutParams(layoutParams);
            playerNames[i].setHint("Player " + (i + 1));
            playerNames[i].setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
            playerNames[i].setFilters(inputFilters);
            playerNames[i].setId(PLAYER_NAME_ID_OFFSET + i);
            if (i < MAX_PLAYERS - 1)
                playerNames[i].setNextFocusForwardId(PLAYER_NAME_ID_OFFSET + i + 1);
            layout.addView(playerNames[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        int n = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        numPlayers = n;

        for (int i = 0; i < MAX_PLAYERS; ++i) {
            playerNames[i].setVisibility(i < n ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        System.out.println("When is this called?");
    }

    public void createNewGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        System.out.print("Creating a new game with " + numPlayers + " players: ");
        String[] names = new String[numPlayers];
        for (int i = 0; i < numPlayers; ++i) {
            String n = playerNames[i].getText().toString();
            if (n.length() == 0)
                n = "Player " + (i + 1);
            names[i] = n;
            System.out.print(n + ", ");
        }
        System.out.println();

        intent.putExtra(EXTRA_PLAYER_NUMBER, numPlayers);
        intent.putExtra(EXTRA_PLAYER_NAMES, names);
        startActivity(intent);
    }
}