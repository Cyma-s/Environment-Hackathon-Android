package com.example.maesilnamu;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UriImageAdapter extends RecyclerView.Adapter<UriImageAdapter.ItemViewHolder> {
    public ArrayList<Uri> album;
    public Context mContext;

    public UriImageAdapter(ArrayList<Uri> albumImgList, Context mContext) {
        this.album = albumImgList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UriImageAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_item, parent, false);
        UriImageAdapter.ItemViewHolder viewHolder = new UriImageAdapter.ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UriImageAdapter.ItemViewHolder holder, int position) {
        holder.imageView.setImageURI(album.get(position));
    }

    @Override
    public int getItemCount() {
        return album.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_image);
        }
    }
}
