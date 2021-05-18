package com.example.maesilnamu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class QuestPostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<QuestPost> list;

    public QuestPostListAdapter(ArrayList<QuestPost> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questpost_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            addItem((ItemViewHolder) holder, position);
        } else if(holder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    public void addPost(QuestPost questPost) {
        list.add(questPost);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private void showLoadingView(LoadingViewHolder holder, int position){

    }

    private void addItem(ItemViewHolder holder, int position){
        QuestPost post = list.get(position);
        holder.setItem(post);
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView postTitle, postContent, isAuth;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            postContent = (TextView) itemView.findViewById(R.id.post_content);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            isAuth = (TextView) itemView.findViewById(R.id.is_auth);
        }

        public void setItem(QuestPost post){
            String authNum = String.valueOf(post.getAuthNum());
            isAuth.setText(authNum);
            postTitle.setText(post.getPostTitle());
            postContent.setText(post.getPostContent());
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.post_progress);
        }
    }
}
