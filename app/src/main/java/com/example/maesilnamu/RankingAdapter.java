package com.example.maesilnamu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<RankingItem> list;

    public RankingAdapter(ArrayList<RankingItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);
            return new RankingLoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            addItem((ItemViewHolder)holder, position);
        } else if(holder instanceof RankingLoadingViewHolder){
            showLoadingView((RankingLoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private void showLoadingView(RankingLoadingViewHolder holder, int position){

    }

    public RankingItem getItem(int position) {
        return list.get(position);
    }

    private void addItem(ItemViewHolder holder, int position){
        RankingItem post = list.get(position);
        holder.setItem(post);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView rankingUserName, userRanking, userRankingPoint;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rankingUserName = (TextView) itemView.findViewById(R.id.ranking_user_name);
            userRanking = (TextView) itemView.findViewById(R.id.ranking_number);
            userRankingPoint = (TextView) itemView.findViewById(R.id.ranking_accumulate_point);
        }

        public void setItem(RankingItem item){
            rankingUserName.setText(item.getUserName());
            userRanking.setText(item.getUserRanking());
            userRankingPoint.setText(item.getUserPoint());
        }

    }

    private static class RankingLoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public RankingLoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.post_progress);
        }
    }
}
