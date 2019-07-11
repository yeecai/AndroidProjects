package com.example.android.fragmentcommunicate;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {
    private Button mButton;
    private boolean isFragmentDisplayed = false;
    private SecondActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button nButton = (Button) findViewById(R.id.next_button);

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
                if (!isFragmentDisplayed) {
                    mainactivity.displayFragment();
                } else mainactivity.closeFragment();
            }
        });
    }
}