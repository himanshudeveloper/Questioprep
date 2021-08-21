package com.examplmakecodeeasy.questionprep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplmakecodeeasy.questionprep.databinding.RowLeaderboadsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LeaderbordsAdapter extends RecyclerView.Adapter<LeaderbordsAdapter.LeaderboardviewHolder>{

    Context context;
    ArrayList<User> users;
    public LeaderbordsAdapter(Context context,ArrayList<User> users){
        this.context =context;
        this.users = users;

    }

    @NonNull
    @NotNull
    @Override
    public LeaderboardviewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboads,parent,false);

        return new LeaderboardviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LeaderboardviewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.name.setText(user.getName());
        holder.binding.coin.setText(String.valueOf(user.getCoins()));
        holder.binding.index.setText(String.format("#%d",position+1));

        Glide.with(context)
                .load(user.getProfile())
                .into(holder.binding.imageView8);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardviewHolder extends RecyclerView.ViewHolder{

        RowLeaderboadsBinding binding;

        public LeaderboardviewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = RowLeaderboadsBinding.bind(itemView);

        }

    }
}
