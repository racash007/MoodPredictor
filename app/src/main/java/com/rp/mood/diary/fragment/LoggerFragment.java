package com.rp.mood.diary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rp.mood.diary.R;
import com.rp.mood.diary.activity.MoodLogger;
import com.rp.mood.diary.adapter.MoodViewAdapter;
import com.rp.mood.diary.listener.OnItemClickListener;

public class LoggerFragment extends Fragment  {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private OnItemClickListener mOnClicklistener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.content_mood_logger,container,false);
        adapter = new MoodViewAdapter(getContext(),mOnClicklistener);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.mood_selection_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        ((MoodLogger)getActivity()).showFab();
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnClicklistener = (OnItemClickListener)context;
        }catch (ClassCastException e)
        {
            throw  new ClassCastException(context.toString() + " must implement MainActivityFragment.OnClicklistener");
        }
    }



}
