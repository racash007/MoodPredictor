package com.rp.mood.diary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rp.mood.diary.R;
import com.rp.mood.diary.activity.MoodLogger;
import com.rp.mood.diary.adapter.MoodTableViewAdapter;
import com.rp.mood.diary.model.DaoSession;
import com.rp.mood.diary.model.MoodDao;

public class ViewerFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    MoodDao moodDao;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.moodDao = ((MoodLogger)context).getMoodDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mood_viewer,container,false);
        adapter = new MoodTableViewAdapter(getContext(),moodDao);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView =  view.findViewById(R.id.mood_viewer_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        ((MoodLogger)getActivity()).hideFab();
        return view;
    }
}
