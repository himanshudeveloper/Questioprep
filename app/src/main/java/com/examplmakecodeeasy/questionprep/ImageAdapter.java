package com.examplmakecodeeasy.questionprep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewholder> {

    Context mContext;
    ArrayList<ImageMode> ImageModels;

    public ImageAdapter(Context context, ArrayList<ImageMode> imageModels) {
        this.mContext = context;
        this.ImageModels = imageModels;
    }




    @NonNull
    @NotNull
    @Override
    public ImageViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,null);


        return new ImageViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewholder holder, int position) {
        final ImageMode mode = ImageModels.get(position);


        Glide.with(mContext).
                load(mode.getImage()).into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return  ImageModels.size();
    }


    public class ImageViewholder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ImageViewholder(@NonNull  View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image2);

        }
    }
}
