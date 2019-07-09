package com.example.android.fragmentexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleFragment extends Fragment {
    private static final int YES = 0;
    private static final int NO = 1;

    public static SimpleFragment newInstance() {
        // Required empty public constructor
        return new SimpleFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =
                inflater.inflate(R.layout.fragment_simple, container, false);

        final RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new
                                                      RadioGroup.OnCheckedChangeListener() {
                                                          @Override
                                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                              View radioButton = radioGroup.findViewById(checkedId);
                                                              int index = radioGroup.indexOfChild(radioButton);
                                                              TextView textView =
                                                                      rootView.findViewById(R.id.fragment_header);
                                                              switch (index) {
                                                                  case YES: // User chose "Yes."
                                                                      textView.setText(R.string.yes_message);
                                                                      break;
                                                                  case NO: // User chose "No."
                                                                      textView.setText(R.string.no_message);
                                                                      break;
                                                                  default: // No choice made.
                                                                      // Do nothing.
                                                                      break;
                                                              }
                                                          }
                                                      });

        final RatingBar ratingBar = rootView.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String myRating = (getString(R.string.my_rating) +
                        String.valueOf(ratingBar.getRating()));
                Toast.makeText(getContext(), myRating,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

}
