package com.example.android.fragmentcommunicate;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/*
*  seems like if I want to use the SimpleFragment in both activities
*  need to implement the Listener and use the copy the displayMethod
*  hope I find a better way to improve this
*
* */
public class SecondActivity extends AppCompatActivity implements SimpleFragment.OnFragmentInterationListener {
    private Button mButton;
    private boolean isFragmentDisplayed = false;
    private SecondActivity instance = null;

    private static int mRadioButtonChoice = 2;
    static final String STATE_FRAGMWNT = "state_of_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button nButton = (Button) findViewById(R.id.previous_button);

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        final MainActivity mainactivity = new MainActivity();

        mButton = findViewById(R.id.open_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFragmentDisplayed) {
                    displayFragment();
                    //mainactivity.displayFragment();
                }else {
                    closeFragment();
                    //mainactivity.closeFragment();
                }
            }
        });
        if (savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(STATE_FRAGMWNT);
            if (isFragmentDisplayed) {
                mButton.setText(R.string.close);
            }
        }
    }


    public void displayFragment() {
        SimpleFragment simpleFragment = SimpleFragment.newInstance(mRadioButtonChoice); // Do we have to new an instance each single time?
        //Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = (FragmentManager) getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //TODO: Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment_container,
                simpleFragment).addToBackStack(null).commit();
        mButton.setText(R.string.close);
        isFragmentDisplayed = true;
    }

    public void closeFragment() {

        FragmentManager fragmentManager = (FragmentManager) getSupportFragmentManager();
        SimpleFragment simpleFragment =  (SimpleFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        // TODO: if the simplefragment exists, need to remove the callback. which means the displayFragment() called
        if( simpleFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(simpleFragment).commit();
        }

        mButton.setText(R.string.open);
        isFragmentDisplayed = false;
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the state of fragment
        savedInstanceState.putBoolean(STATE_FRAGMWNT,isFragmentDisplayed);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRadioButtonChoice(int choice) {
        mRadioButtonChoice = choice;
        Toast.makeText(this,"Choice is " + Integer.toString(choice),Toast.LENGTH_SHORT).show();
    }
}


