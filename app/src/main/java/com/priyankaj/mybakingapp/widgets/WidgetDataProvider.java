package com.priyankaj.mybakingapp.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.activities.MainActivity;
import com.priyankaj.mybakingapp.models.IngredientsModel;
import com.priyankaj.mybakingapp.models.MainModel;
import com.priyankaj.mybakingapp.utils.Constants;
import com.priyankaj.mybakingapp.utils.Urls;

import java.util.ArrayList;


class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    ArrayList<IngredientsModel> list;
    int pos;
    public WidgetDataProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        pos = sharedPreferences.getInt(Constants.RECEIPE_POSITION, -1);
        Log.v("on create provider", pos + "");
        list = new ArrayList<>();
        if (MainActivity.list.size() != 0) {
            list = MainActivity.list.get(pos).getIngredients();
        } else {
            fetchData();
        }

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        pos = sharedPreferences.getInt(Constants.RECEIPE_POSITION, -1);
        if (MainActivity.list.size() != 0) {
            list = MainActivity.list.get(pos).getIngredients();
        } else {
            fetchData();
        }

    }

    private void fetchData() {

        AndroidNetworking.get(Urls.url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(MainModel.class, new ParsedRequestListener<ArrayList<MainModel>>() {
                    @Override
                    public void onResponse(ArrayList<MainModel> response) {
                        list.clear();
                        list = response.get(pos).getIngredients();
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list);


        remoteViews.setTextViewText(R.id.ingredient, list.get(position).getIngredient());
        remoteViews.setTextViewText(R.id.measure, list.get(position).getMeasure());
        remoteViews.setTextViewText(R.id.quantity, list.get(position).getQuantity() + "");





        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
