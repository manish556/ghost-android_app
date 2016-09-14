package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private Button restartButton;
    private Button challengeButton;
    private TextView text;
    private TextView label;

    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        text = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        restartButton = (Button) findViewById(R.id.reset);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(view);
            }
        });

        if(savedInstanceState==null)
            onStart(null);
        else
        {
            currentFragment = savedInstanceState.getString("Fragment");
            userTurn = savedInstanceState.getBoolean("Turn");
            updateScreen();
            if(userTurn==false)
                computerTurn();
        }

        challengeButton = (Button) findViewById(R.id.challenge);
        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challenge();
            }
        });


        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    public void challenge() {
      if (currentFragment.length()>3 && dictionary.isWord(currentFragment)) {
          label.setText("User Wins");
          userTurn = false;
          return;
      }

      String nWord = dictionary.getAnyWordStartingWith(currentFragment);
      if( nWord != null)
      {
          label.setText("Computer Wins");
          text.setText(nWord);
          userTurn = false;
          return;
      }

        label.setText("User Wins");
        userTurn = false;
        return;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(userTurn) {
            int ch = event.getUnicodeChar();
            if (Character.isLetter(ch)){
                currentFragment += (char)ch;
                updateScreen();;
                computerTurn();
            }
        }

        return super.onKeyUp(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        currentFragment = "";
        userTurn = random.nextBoolean();
        updateScreen();
        if (!userTurn) {
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        // Do computer turn stuff then make it the user's turn again
        userTurn = false;
        updateScreen();
        if(currentFragment.length()>3 && dictionary.isWord(currentFragment)) {
            label.setText("Computer Wins");
            return;
        }

        String temp = dictionary.getGoodWordStartingWith(currentFragment);
      //  Log.d("temp", temp);
        if(temp == null) {
            label.setText("Computer Wins");
            return;
        }

        int size = currentFragment.length();
        char ch = temp.charAt(size);
        currentFragment += ch;

        userTurn = true;
        updateScreen();
    }

    private void updateScreen()
    {
        text.setText(currentFragment);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Fragment",currentFragment);
        outState.putBoolean("Turn",userTurn);
    }
}
