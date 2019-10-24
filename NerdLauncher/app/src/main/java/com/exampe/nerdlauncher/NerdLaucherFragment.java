package com.exampe.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NerdLaucherFragment extends Fragment {
    private static final String TAG = "NerdLauncherFragment";

    private RecyclerView mRecyclerView;
    public static NerdLaucherFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NerdLaucherFragment fragment = new NerdLaucherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.app_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager packageManager = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(packageManager).toString(),
                        b.loadLabel(packageManager).toString());
            }
            });

        Log.i(TAG, "Found " + activities.size() + "activties.");
        mRecyclerView.setAdapter(new ActivtiyAdapter(activities));
    }
    private class ActivityHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView;
            mNameTextView.setOnClickListener(this);
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager packageManager = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(packageManager).toString();
            mNameTextView.setText(appName);
        }


        @Override
        public void onClick(View view) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);

        }
    }

    private class ActivtiyAdapter extends RecyclerView.Adapter<ActivityHolder> {

        private final List<ResolveInfo> mActivities;

        public ActivtiyAdapter(List<ResolveInfo> mActivities) {
            this.mActivities = mActivities;
        }

        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }

}
