package com.rp.mood.diary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.greendao.query.LazyList;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.rp.mood.diary.R;
import com.rp.mood.diary.databinding.MoodListViewItemBinding;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDao;
import com.rp.mood.diary.model.enums.MoodImage;
import com.rp.mood.diary.utils.DateUtils;

public class MoodTableViewAdapter extends RecyclerView.Adapter<MoodTableViewAdapter.MoodItemViewHolder> {
    Context context;
    MoodDao moodDao;
    MoodListViewItemBinding binding;
    List<Mood> moodLazyList;
    SimpleDateFormat simpleDateFormat;


    public MoodTableViewAdapter(Context context, MoodDao moodDao) {
        simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd-MM-yyyy HH:mm:ss");
        this.context = context;
        this.moodDao = moodDao;
        this.moodLazyList = this.moodDao.queryBuilder().orderDesc(MoodDao.Properties.MoodTime).listLazy();
    }

    @Override
    public MoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = MoodListViewItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MoodItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MoodItemViewHolder holder, int position) {
        Mood mood = moodLazyList.get(position);
        int backGroundColor = position%2==0?R.color.colorItem1:R.color.colorItem2;
        holder.itemView.setBackgroundColor(context.getResources().getColor(backGroundColor));
        holder.moodTextView.setImageResource(MoodImage.moodMap.getOrDefault(mood.getMood(),R.drawable.com_facebook_close));
        addFormatedDateTime(mood.getMoodTime(),holder);
        holder.bind(mood);
        holder.moodCommentTextView.setText(mood.getMoodDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mood.getMoodDesc()!=null)
                {mood.setExpanded(!mood.isExpanded());}
                notifyItemChanged(position);
            }
        });

            }

    private void addFormatedDateTime(Date moodTime, MoodItemViewHolder holder) {
            holder.moodDateView.setText(DateUtils.getFriendlyDayString(context,moodTime));
            holder.moodTimeView.setText(DateUtils.getFormattedTime(moodTime));
    }

    @Override
    public int getItemCount() {
        return this.moodLazyList.size();
    }

    public class MoodItemViewHolder extends RecyclerView.ViewHolder
    {
        ImageView moodTextView;
        TextView moodDateView;
        TextView moodTimeView;
        TextView moodCommentTextView;

        public MoodItemViewHolder(MoodListViewItemBinding binding) {
            super(binding.getRoot());
            moodTextView = binding.moodViewItemText;
            moodDateView = binding.moodViewItemDate;
            moodTimeView = binding.moodViewItemTime;

        }

        public void bind(Mood mood) {
            moodCommentTextView  = binding.moodComment;
            moodCommentTextView.setVisibility(mood.isExpanded()?View.VISIBLE:View.GONE);
        }
    }
    public void setMoodLazyList(List<Mood> moodLazyList) {
        if(moodLazyList.size()>0) {
            Collections.sort(moodLazyList, (a, b) -> a.getMoodTime().compareTo(b.getMoodTime()));
            this.moodLazyList = moodLazyList;
            this.notifyDataSetChanged();
        }
    }

    public List<Mood> getMoodLazyList() {
        return this.moodLazyList;
    }

}
