package com.rp.mood.diary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rp.mood.diary.R;
import com.rp.mood.diary.listener.OnItemClickListener;
import com.rp.mood.diary.model.MoodImage;

public class MoodViewAdapter extends RecyclerView.Adapter<MoodViewAdapter.MoodItemViewHolder> {
    Context context;
    MoodImage[] moods;
    int selectedPosition = -1;
    OnItemClickListener onItemClickListener;
    public MoodViewAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.moods = MoodImage.values();
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MoodItemViewHolder vh = new MoodItemViewHolder(v); // pass the view to View Holder
        return vh;
    }



    @Override
    public void onBindViewHolder(final MoodItemViewHolder holder, final int position) {
        // set the data in items
        if(selectedPosition==position)
        {holder.moodTextView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));}
        else{
          int backGroundColor = position%2!=0?R.color.colorItem1:R.color.colorItem2;
            holder.moodTextView.setBackgroundColor(context.getResources().getColor(backGroundColor));
        }
        holder.moodTextView.setText(moods[position].getMoodName());
        Drawable img = ContextCompat.getDrawable(context,moods[position].getMoodImage());
        int imgSize = (int) context.getResources().getDimension(R.dimen.img_size);
        img.setBounds(0,0,imgSize,imgSize);
        holder.moodTextView.setCompoundDrawables(img,null,null,null);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                onItemClickListener.onItemClicked(moods[holder.getAdapterPosition()].getMoodName());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moods.length;
    }

    public class MoodItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView moodTextView;

        public MoodItemViewHolder(View itemView) {
            super(itemView);
            moodTextView = itemView.findViewById(R.id.mood_view_item_text);

        }
    }
}
