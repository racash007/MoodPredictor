package com.rp.mood.diary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rp.mood.diary.R;
import com.rp.mood.diary.activity.MoodLogger;
import com.rp.mood.diary.async.PositionCalculatorTask;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.model.MoodImage;
import com.rp.mood.diary.service.RestCallService;

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
    private CompositeSubscription mCompositeSubscription =  new CompositeSubscription();
    private PositionCalculatorTask positionCalculatorTask;
    private RestCallService applicationService;
    private ProgressBar pgsBar;
    private TextView textView;
    private ImageView imageView;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view =  inflater.inflate(R.layout.fragment_prediction,container,false);
        pgsBar = view.findViewById(R.id.pBar);
        textView = view.findViewById(R.id.msg_prediction);
        imageView = view.findViewById(R.id.img_prediction);
        positionCalculatorTask = ((MoodLogger)getContext()).getPositionCalculatorTask();
        applicationService = ((MoodLogger)getContext()).getRestCallService();
        mCompositeSubscription.add(
              Observable.defer(()->{
                  Mood mood = new Mood();
                  mood.setMoodTime(new Date());
              return  Observable.just(positionCalculatorTask.getMoodDriver(mood));
              })
              .subscribe((moodDriver)->
        mCompositeSubscription.add(getNewPrediction(moodDriver)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this))));
        ((MoodLogger)getActivity()).hideFab();
        return view;
    }

    @Override
    public void onCompleted() {
        pgsBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable e) {
        pgsBar.setVisibility(View.GONE);
        textView.setText("Something went wrong.Please try after a while");
        textView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onNext(JSONObject jsonObject) {
        pgsBar.setVisibility(View.GONE);
        try {

           String prediction =  jsonObject.get("prediction").toString();
           if(prediction.equalsIgnoreCase(getString(R.string.NoModelYet)))
           {
              textView.setText(getString(R.string.msg_no_model_yet));
              textView.setVisibility(View.VISIBLE);
           }else {
               Integer integer = MoodImage.moodMap.get(prediction);
               imageView.setImageResource(integer);
               imageView.setVisibility(View.VISIBLE);
               textView.setText(MoodImage.moodMsgMap.get(prediction));
           }

        }catch (JSONException e) {
            e.printStackTrace();
        }

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
    }
}
