package com.example.maesilnamu;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContentImageAdapter extends RecyclerView.Adapter<ContentImageAdapter.ContentImageViewHolder>{
    private ArrayList<Bitmap> bitmaps;
    OnPictureClickListener listener;

    public ContentImageAdapter(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public ContentImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new ContentImageViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentImageViewHolder holder, int position) {
        addItem(holder, position);
        holder.image.setImageBitmap(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    private void addItem (ContentImageViewHolder holder, int position){
        Bitmap bitmap = bitmaps.get(position);
        holder.setItem(bitmap);
    }

    public Bitmap getItem(int position) {
        return bitmaps.get(position);
    }


    public void setPictureItemClickListener (OnPictureClickListener listener) {
        this.listener = listener;
    }


    public void OnItemClick(ContentImageViewHolder holder, View view, int position) {
        if(listener != null ){
            listener.OnItemClick(holder, view, position);
        }
    }

    public static class ContentImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ContentImageViewHolder(@NonNull View itemView, OnPictureClickListener listener) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recycler_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if(listener != null) listener.OnItemClick(ContentImageViewHolder.this, v, position);
                }
            });
        }

        public void setItem(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }

}
