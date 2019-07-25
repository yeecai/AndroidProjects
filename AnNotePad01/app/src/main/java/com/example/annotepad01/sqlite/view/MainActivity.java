package com.example.annotepad01.sqlite.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.annotepad01.R;
import com.example.annotepad01.sqlite.database.DatabaseHelper;
import com.example.annotepad01.sqlite.database.model.Note;

import java.util.ArrayList;
import java.util.List;


/*
*
*   This is the draft practices for AnNotPad
*  SQLite part
*  MVP
*  Materiel Design CardViews
*
* */


public class MainActivity extends AppCompatActivity {

    private NotesAdapter mAdapter;
    private DatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        
        
        // Try to list all notes
        db = new DatabaseHelper(this);
        notesList.addAll(db.getAllNotes());
        for(Note temp:notesList){
            Log.d("note","note: "+temp.getNote());
        }
        
        mAdapter = new NotesAdapter(this, notesList);
      //  mLayoutManager = new LinearLayoutManager(getApplicationContext());
      //  mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


      //  recyclerView.setLayoutManager(mLayoutManager);
        Log.d("mAdapter","mAdapter.getItemCount() "+mAdapter.getItemCount());
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
