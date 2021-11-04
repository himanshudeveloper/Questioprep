package com.examplmakecodeeasy.questionprep;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public  class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;


    }

    @NonNull

    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);

        return new CategoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.CategoryViewHolder holder, int position) {
    final CategoryModel model = categoryModels.get(position);

    holder.textView.setText(model.getCategoryName());


        Picasso.get().load(model.getCategoryImage())

                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.imageView);
       /* Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,QuizActivity.class);
                intent.putExtra("catId",model.getCategoryId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("catName",model.getCategoryName());
                context.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull  View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);


        }
    }
}
