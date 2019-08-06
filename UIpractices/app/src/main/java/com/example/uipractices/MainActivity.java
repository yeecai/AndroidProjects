package com.example.uipractices;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.uipractices.MyRecyclerView.MyRecyclerViewFragment;
import com.example.uipractices.View.MyBlock;

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
    private MyBlock mBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //A custom view for action practice
        mBlock = (MyBlock) this.findViewById(R.id.my_block);

        //initData();
        // Buttons jump to different UI

        //setListView();

        //  setGridView();


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
     //   openRecyclerViewFragment();





        // CV

        // Notifactions
     /*   NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/

        //TookBar&Palette
    }

    private void closeRecyclerViewFFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        MyRecyclerViewFragment mFragment = (MyRecyclerViewFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(mFragment).commit();
        isFragmentDisplayed = false;
    }

    private void openRecyclerViewFragment() {

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

   public void setListView() {
      //  recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));//it works here!!!

        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }

    public void setGridView() {

       // recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Context mContext;
        private List<String> mDataset;
       // private String[] mDataset;
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder


        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<String> myDataset) {
          //  this.mContext = mContext;
            this.mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            // create a new view
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
          //  holder.itemView.setTag(position);
            holder.textView.setText(mDataset.get(position));

        }



        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
          //  return mDataset.length;
            return mDataset.size();
        }
     // now need to extends ViewHolder anymore????

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textView;

            public MyViewHolder(View v) {
                super(v);
                textView = (TextView) v.findViewById(R.id.textView);
            }
        }

    }
}