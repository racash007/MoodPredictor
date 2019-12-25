package freewill.rakesh.com.howdoyoufeel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.greendao.query.LazyList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import freewill.rakesh.com.howdoyoufeel.R;
import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDao;
import freewill.rakesh.com.howdoyoufeel.model.MoodImage;
import freewill.rakesh.com.howdoyoufeel.utility.Utility;

public class MoodTableViewAdapter extends RecyclerView.Adapter<MoodTableViewAdapter.MoodItemViewHolder> {
    Context context;
    MoodDao moodDao;

    public void setMoodLazyList(LazyList<Mood> moodLazyList) {
        this.moodLazyList = moodLazyList;
    }

    LazyList<Mood> moodLazyList;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_list_view_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MoodTableViewAdapter.MoodItemViewHolder vh = new MoodTableViewAdapter.MoodItemViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MoodItemViewHolder holder, int position) {
        Mood mood = moodLazyList.get(position);
        int backGroundColor = position%2==0?R.color.colorItem1:R.color.colorItem2;
        holder.itemView.setBackgroundColor(context.getResources().getColor(backGroundColor));
        holder.moodTextView.setImageResource(MoodImage.moodMap.get(mood.getMood()));
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
            holder.moodDateView.setText(Utility.getFriendlyDayString(context,moodTime));
            holder.moodTimeView.setText(Utility.getFormattedTime(moodTime));
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

        public MoodItemViewHolder(View itemView) {
            super(itemView);
            moodTextView = itemView.findViewById(R.id.mood_view_item_text);
            moodDateView = itemView.findViewById(R.id.mood_view_item_date);
            moodTimeView = itemView.findViewById(R.id.mood_view_item_time);

        }

        public void bind(Mood mood) {
            moodCommentTextView  = itemView.findViewById(R.id.mood_comment);
            moodCommentTextView.setVisibility(mood.isExpanded()?View.VISIBLE:View.GONE);
        }
    }
}
