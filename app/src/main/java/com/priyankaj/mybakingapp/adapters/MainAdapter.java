package com.priyankaj.mybakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.models.MainModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final List<MainModel> list;
    private final Context context;

    public MainAdapter(List<MainModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview_model, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getHome_name());

        String url = list.get(position).getImage();
        if (url == null || url.equals("")) {
            holder.imageView.setImageResource(R.drawable.ic_food_placeholder);
        } else {
            Glide.with(context).load(url).placeholder(R.drawable.ic_food_placeholder).error(R.drawable.ic_food_placeholder).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.recipe_name)
        TextView name;
        @BindView(R.id.recipe_imageview)
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
