package com.rp.mood.diary.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rp.mood.diary.R;
import com.rp.mood.diary.activity.MoodActivity;
import com.rp.mood.diary.adapter.MoodViewAdapter;
import com.rp.mood.diary.databinding.MoodLoggerBinding;
import com.rp.mood.diary.databinding.MoodLoggerListBinding;
import com.rp.mood.diary.listener.OnItemClickListener;

import javax.inject.Inject;

public class LoggerFragment extends Fragment  {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private OnItemClickListener itemClickListener;
    MoodLoggerListBinding binding;

    @Inject
    public LoggerFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoodLoggerListBinding.inflate(inflater,container,false);
        adapter = new MoodViewAdapter(getContext(), itemClickListener);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView = binding.moodSelectionList;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        ((MoodActivity)getActivity()).showFab();
        return binding.getRoot();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            itemClickListener = (OnItemClickListener)context;
        }catch (ClassCastException e)
        {
            throw  new ClassCastException(context.toString() + " must implement MainActivityFragment.OnClicklistener");
        }
    }



}
