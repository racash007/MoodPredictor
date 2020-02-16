package com.rp.mood.diary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.rp.mood.diary.R;
import com.rp.mood.diary.activity.MoodLogger;
import com.rp.mood.diary.async.PositionCalculatorTask;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.service.ApplicationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PredictionFragment extends Fragment implements Observer<JSONObject> {


    private RequestQueue queue;
    private CompositeSubscription mCompositeSubscription =  new CompositeSubscription();
    private PositionCalculatorTask positionCalculatorTask;
    private ApplicationService applicationService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        this.queue = Volley.newRequestQueue(getContext());
        positionCalculatorTask = ((MoodLogger)getContext()).getPositionCalculatorTask();
        applicationService = ((MoodLogger)getContext()).getApplicationService();
        mCompositeSubscription.add(
              Observable.defer(()->{
                  Mood mood = new Mood();
                  mood.setMoodTime(new Date());
              return  Observable.just(positionCalculatorTask.getMoodDriver(mood));
              }).subscribeOn(Schedulers.io())
              .subscribe((moodDriver)->
        mCompositeSubscription.add(getNewPrediction(moodDriver)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this))));
        return view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(JSONObject jsonObject) {

    }


    public Observable<JSONObject> getNewPrediction(MoodDriver moodDriver) {
        return Observable.defer(() ->{
                try {
                    return Observable.just(applicationService.getPrediction(moodDriver));
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    Log.e("routes", e.getMessage());
                    return Observable.error(e);
                }
        });
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
