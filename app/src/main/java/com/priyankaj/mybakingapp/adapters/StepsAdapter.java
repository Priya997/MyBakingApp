package com.priyankaj.mybakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.models.StepsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private final ArrayList<StepsModel> stepsModelList;
    private final Context context;

    public StepsAdapter(ArrayList<StepsModel> list, Context context) {
        this.stepsModelList = list;
        this.context = context;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_recycler_model, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.shortdescription.setText(stepsModelList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsModelList.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_short_description)
        TextView shortdescription;

        StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
