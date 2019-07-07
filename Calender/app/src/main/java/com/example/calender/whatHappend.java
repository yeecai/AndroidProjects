package com.example.calender;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class whatHappend extends AppCompatActivity {

    TextView moodtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_happend);
        moodtext = findViewById(R.id.whathappenedwithmood);
        String mood = getIntent().getStringExtra("quitehappy");

        moodtext.setText("Hmm, seems like you're "+mood+", what happend?");
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
    }

}
