package com.priyankaj.mybakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.models.IngredientsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private final List<IngredientsModel> list;
    private final Context context;

    public IngredientsAdapter(List<IngredientsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_model, parent, false);
        return new IngredientsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.iname.setText(list.get(position).getIngredient());
        holder.iquantity.setText(list.get(position).getQuantity());
        holder.imeasure.setText(list.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_name)
        TextView iname;
        @BindView(R.id.ingredient_quantity)
        TextView iquantity;
        @BindView(R.id.ingredient_measure)
        TextView imeasure;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
