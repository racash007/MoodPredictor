package com.rp.mood.diary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rp.mood.diary.R;
import com.rp.mood.diary.databinding.StatsBinding;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDao;
import com.rp.mood.diary.model.enums.MoodImage;
import com.rp.mood.diary.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import rx.Observable;

import static com.rp.mood.diary.model.MoodDao.Properties.MoodTime;

public class StatsFragment extends Fragment {

    private MoodDao moodDao;
    private Date to;
    private Date from;
    private Button toButton;
    private Button fromButton;
    private StatsBinding statsBinding;
    private String toLabel;
    private String fromLabel;
    @Inject
    public StatsFragment(MoodDao moodDao) {
        super();
        this.moodDao = moodDao;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        statsBinding = StatsBinding.inflate(inflater,container,false);
        statsBinding.todayButton.setOnClickListener((view)->setDurationFilter(1));
        statsBinding.weekButton.setOnClickListener((view)->setDurationFilter(7));
        statsBinding.oneMonthButton.setOnClickListener((view)->setDurationFilter(Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)));
        fromButton = statsBinding.fromButton;
        fromButton.setOnClickListener((view)->openDatePicker(view,fromLabel));
        toButton = statsBinding.toButton;
        toButton.setOnClickListener((view)->openDatePicker(view,toLabel));
        toLabel = getResources().getString(R.string.To);
        fromLabel = getResources().getString(R.string.From);
        LineChart temporalChart = statsBinding.temporalChart;
        setDurationFilter(1);
        temporalChart.setData(getLimeData());
        temporalChart.setViewPortOffsets(60, 0, 50, 60);
        return statsBinding.getRoot();
    }

    private void openDatePicker(View view, String label) {
        statsBinding.datepicker.setVisibility(View.VISIBLE);
        statsBinding.datepicker.setOnDateChangedListener((pickerView,year, monthOfYear, dayOfMonth)-> populateDate((Button) view, year, monthOfYear, dayOfMonth,label));
    }

    private void populateDate(Button view, int year, int monthOfYear, int dayOfMonth,String label) {
        Calendar now = Calendar.getInstance();
        now.set(year,monthOfYear,dayOfMonth);
        if(toLabel.equalsIgnoreCase(label)){
            to = now.getTime();
        }else {
            from = now.getTime();
        }
        view.setText(DateUtils.getFormattedDate(now.getTime()));
        statsBinding.datepicker.setVisibility(View.INVISIBLE);
        if(!toButton.getText().toString().equalsIgnoreCase(toLabel) && !fromButton.getText().toString().equalsIgnoreCase(fromLabel)) {
            redrawLineChart();
        }
    }

    private void setDurationFilter(Integer durationFilter) {
        Calendar now = Calendar.getInstance();
         to  =  Calendar.getInstance().getTime();
        now.add(Calendar.DATE,-durationFilter);
         from = now.getTime();
         toButton.setText(toLabel);
         fromButton.setText(fromLabel);
        redrawLineChart();
    }

    private void redrawLineChart() {
        Observable.just(statsBinding.temporalChart).subscribe(
                (lineChart)->{
                    lineChart.setData(getLimeData());
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                });
    }

    private LineData getLimeData() {
        List<Mood> moodList = this.moodDao.queryBuilder().where(MoodTime.between(from, to)).list();
        LineDataSet lineDataSet = new LineDataSet(moodList.stream().map(m -> new Entry(m.getMoodTime().getTime(), MoodImage.moodStatsMap.getOrDefault(m.getMood(), 0))).collect(Collectors.toList()), "Mood");
        lineDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        return new LineData(lineDataSet);
    }

}
