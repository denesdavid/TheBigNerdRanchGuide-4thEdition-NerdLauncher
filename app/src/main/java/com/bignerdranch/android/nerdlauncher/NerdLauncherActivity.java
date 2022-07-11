package com.bignerdranch.android.nerdlauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NerdLauncherActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final String TAG = "NerdLauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nerd_launcher);

        recyclerView = findViewById(R.id.app_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
    }

    private  void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(startupIntent, 0);
        activities.sort((a, b) ->
                String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(getPackageManager()).toString(),
                        b.loadLabel(getPackageManager()).toString()));

        recyclerView.setAdapter(new ActivityAdapter(activities));

        Log.i(TAG, "Found " + activities.size() + activities);
    }

    private class ActivityHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView;
        private ResolveInfo _resolveInfo;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView;
        }

        private void bindActivity(ResolveInfo resolveInfo)
        {
            _resolveInfo = resolveInfo;
            PackageManager packageManager = itemView.getContext().getPackageManager();
            String appName = resolveInfo.loadLabel(packageManager).toString();
            nameTextView.setText(appName);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{

        private final List<ResolveInfo> _activities;

        public ActivityAdapter (List<ResolveInfo> activities) {
            _activities = activities;
        }


        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = _activities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return _activities.size();
        }
    }
}