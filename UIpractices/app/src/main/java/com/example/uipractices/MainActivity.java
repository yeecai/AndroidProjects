package com.example.uipractices;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.uipractices.MyRecyclerView.MyRecyclerViewFragment;
import com.example.uipractices.MyRecyclerView.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "hmm";
    // private String[] myDataset;
    private List<String> myDataset;

    private RecyclerView recyclerView;
    // private LinearLayoutManager layoutManager;
    private MyAdapter mAdapter;
    private Button rvButton;

    private boolean isFragmentDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initData();
        // Buttons jump to different UI
        // RV!
        rvButton = findViewById(R.id.recyclerview_fragment_button);
        rvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFragmentDisplayed) {
                   // Log.d(TAG, "hmm");
                    openRecyclerViewFragment();
                }else {
                    closeRecyclerViewFFragment();
                }
            }
        });




        //


      // setListView();

      //  setGridView();
        //all the code from https://developer.android.com/guide/topics/ui/layout/recyclerview

        // CV

        // Notifactions

        //TookBar&Palette
    }

    private void closeRecyclerViewFFragment() {
        isFragmentDisplayed = false;
    }

    private void openRecyclerViewFragment() {
       // Log.d(TAG, "hmm");
        MyRecyclerViewFragment mFragment = MyRecyclerViewFragment.newInstance(2); // Do we have to new an instance each single time?

        //Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container,
                mFragment).addToBackStack(null).commit();

        isFragmentDisplayed = true;
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




    /*public void setListView() {

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

    }*/

}