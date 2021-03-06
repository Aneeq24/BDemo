package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AnalyticsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view.DailyExerciseInfo;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.List;

public class DayRecycleAdapter extends RecyclerView.Adapter<DayRecycleAdapter.myHolder> {

    private String[] titles = {"BEGINNER", "INTERMEDIATE", "ADVANCED"};
    private String[] dayName;
    private List<Float> mProgress;
    private int currentPlan;

    public DayRecycleAdapter(Context context, List<Float> progress, int plan) {
        this.currentPlan = plan;
        this.dayName = context.getResources().getStringArray(R.array.days_list);
        this.mProgress = progress;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder holder, final int position) {
        holder.tvDayName.setText(dayName[position]);
        float progress = 0;
        if (mProgress.size() > position)
            progress = mProgress.get(position);
        if (progress == 200f) {
            holder.mProgressBar.setVisibility(View.GONE);
            holder.imgDone.setVisibility(View.VISIBLE);
            holder.imgDone.setImageResource(R.drawable.days_screen_rest_icon);
            holder.itemView.setOnClickListener(view -> goToRestActivity(view.getContext(), position));
        } else {
            if (progress == 1) {
                holder.tvDayName.setTextColor(Color.WHITE);
                holder.mProgressBar.setVisibility(View.GONE);
                holder.imgDone.setVisibility(View.VISIBLE);
                holder.itemView.setBackgroundResource(R.drawable.ic_yellow_round_bar);
            }
            holder.itemView.setOnClickListener(view -> goToNewActivity(view.getContext(), position));
            holder.mProgressBar.setProgress((int) (progress * 100));
        }
    }

    @Override
    public int getItemCount() {
        if (dayName == null)
            return 0;
        else
            return dayName.length;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvDayName;
        ImageView imgDone;
        CircleProgressBar mProgressBar;

        myHolder(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.dayNameId);
            imgDone = itemView.findViewById(R.id.img_done);
            mProgressBar = itemView.findViewById(R.id.line_progress_left);
        }
    }

    private void goToNewActivity(Context context, int position) {
        Intent i = new Intent(context, DailyExerciseInfo.class);
        i.putExtra(context.getString(R.string.day_selected), position + 1);
        i.putExtra(context.getString(R.string.plan), currentPlan);
        AnalyticsManager.getInstance().sendAnalytics("day  " + (position + 1) + "of_plan:" + titles[currentPlan], "day_selected_" + (position + 1));
        context.startActivity(i);
    }

    private void goToRestActivity(Context context, int position) {
        Intent i = new Intent(context, PlayingExercise.class);
        i.putExtra(context.getString(R.string.day_selected), position + 1);
        i.putExtra(context.getString(R.string.plan), currentPlan);
        AnalyticsManager.getInstance().sendAnalytics("day  " + (position + 1) + "of_plan:" + titles[currentPlan], "day_selected_" + (position + 1));
        context.startActivity(i);
    }

}
