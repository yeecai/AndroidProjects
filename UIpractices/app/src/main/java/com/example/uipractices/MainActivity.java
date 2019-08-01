package com.example.uipractices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uipractices.MyAdapter;
import com.example.uipractices.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   // private String[] myDataset;
    private List<String> myDataset;

    private RecyclerView recyclerView;
    // private LinearLayoutManager layoutManager;
    private MyAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        // Buttons jump to different UI
        // RV!

       //


      // setListView();

        setGridView();
        //all the code from https://developer.android.com/guide/topics/ui/layout/recyclerview

        // CV

        // Notifactions

        //TookBar&Palette
    }


    // Q: Do you know why when test a item from RV, can only use OnData instead of OnView?...
// Facts about RV:1 might only create new view holders till user scroll down, if there view display items 0-9, it might create position 10 in advance so user scoll down the view holders would be right to display
// but not sure how much.  2. create new VH when use scroll down. 3. can  use notify()
    private void initData() {
      // myDataset = new String[20];
      // for (int i = 0; i < 20; i++) myDataset[i]="A";

        myDataset = new ArrayList<>();
        for (int i=1; i<20;i++) myDataset.add(i+"");
    }

    public void setListView() {

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

    }
    public void setGridView() {

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));



        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

    }

}