package com.rp.mood.diary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rp.mood.diary.activity.MoodActivity;
import com.rp.mood.diary.adapter.MoodTableViewAdapter;
import com.rp.mood.diary.databinding.MoodViewerBinding;
import com.rp.mood.diary.model.MoodDao;
import com.rp.mood.diary.service.MoodAppService;
import com.rp.mood.diary.utils.MoodDataUtils;

import org.json.JSONException;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AndroidEntryPoint
public class ViewerFragment extends Fragment  {

    RecyclerView recyclerView;
    MoodTableViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private MoodAppService moodAppService;
    MoodDao moodDao;
    MoodViewerBinding binding;

    @Inject
    public ViewerFragment(MoodDao moodDao, MoodAppService moodAppService) {
        super();
        this.moodDao = moodDao;
        this.moodAppService = moodAppService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoodViewerBinding.inflate(inflater,container,false);
        adapter = new MoodTableViewAdapter(getContext(),moodDao);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView =  binding.moodViewerList;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        binding.refreshView.setOnRefreshListener(()->{

                Observable.defer(()->
                {
                    try {
                        return Observable.just(this.moodAppService.getMoodData(adapter.getMoodLazyList()
                                .stream()
                                .findFirst()
                                .map(m->m.getMoodTime())
                                .orElse(new Date())));
                        } catch (ExecutionException | InterruptedException | JSONException | TimeoutException e) {
                        Log.e("routes", e.getMessage());
                        return Observable.error(e);
            }}
            ).retry(3).subscribeOn(Schedulers.computation())
                        .map(jsonObject -> MoodDataUtils.getMood(jsonObject))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((moodList) ->{
                            adapter.setMoodLazyList(moodList);
                            binding.refreshView.setRefreshing(false);
                            Observable.just(moodList).subscribeOn(Schedulers.io()).subscribe(moodList1 -> {
                               moodList1.forEach(mood -> {
                                   moodDao.save(mood);
                               });
                            });
                        });
        });
        ((MoodActivity)getActivity()).hideFab();
        return binding.getRoot();
    }


}
