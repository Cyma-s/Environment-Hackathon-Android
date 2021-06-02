package com.example.maesilnamu;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MyQuestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyQuest> list = new ArrayList<>();
    OnMyQuestItemClickListener listener;
    private SparseBooleanArray completeItems = new SparseBooleanArray(0);

    public MyQuestAdapter(ArrayList<MyQuest> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_quest_item, parent, false);

        return new MyQuestItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        addItem((MyQuestItemViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setQuestItemClickListener(OnMyQuestItemClickListener listener){
        this.listener = listener;
    }

    private void addItem (MyQuestItemViewHolder holder, int position) {
        MyQuest quest = list.get(position);
        holder.setItem(quest);
    }

    public MyQuest getItem(int position) {
        return list.get(position);
    }

    public static class MyQuestItemViewHolder extends RecyclerView.ViewHolder {
        private TextView questName;
        private CheckBox check;
        //private boolean isComplete;
        private LinearLayout questLayout;
        private boolean questComplete;

        public MyQuestItemViewHolder(@NonNull View itemView, OnMyQuestItemClickListener listener) {
            super(itemView);
            questName = (TextView) itemView.findViewById(R.id.mypage_quest_questName);
            check = (CheckBox) itemView.findViewById(R.id.mypage_quest_checkbutton);
            questLayout = (LinearLayout) itemView.findViewById(R.id.mypage_quest_item);
            check.setClickable(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if(listener != null) listener.OnItemClick(MyQuestItemViewHolder.this, v, position);
                }
            });
        }

        public void setItem(MyQuest quest) {
            String myPageQuestName = quest.getQuestName();
            questComplete = quest.isComplete();
            questName.setText(myPageQuestName);
            check.setChecked(questComplete);

        }
    }
}
