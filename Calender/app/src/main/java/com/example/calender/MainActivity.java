package com.example.calender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnMiserable, btnNotSoGood,btnAlive, btnGood, btnQuiteHappy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMiserable = findViewById(R.id.miserable);
        btnNotSoGood = findViewById(R.id.notSoGood);
        btnAlive = findViewById(R.id.alive);
        btnGood = findViewById(R.id.good);
        btnQuiteHappy = findViewById(R.id.quiteHappy);

        btnQuiteHappy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.calender.whatHappend.class);
                String mood = "quitehappy";
                intent.putExtra("quitehappy", mood);
                startActivity(intent);
            }
        });

    }
}
