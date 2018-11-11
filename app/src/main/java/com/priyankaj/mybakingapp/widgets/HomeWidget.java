package com.priyankaj.mybakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.activities.MainActivity;
import com.priyankaj.mybakingapp.utils.Constants;


public class HomeWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt(Constants.RECEIPE_POSITION, -1);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_widget);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
            if (position == -1) {
                remoteViews.setViewVisibility(R.id.empty_widget_layout, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.data_widget_layout, View.INVISIBLE);

                PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
                remoteViews.setOnClickPendingIntent(R.id.empty_widget_btn, pendingIntent1);

            } else {
                remoteViews.setViewVisibility(R.id.empty_widget_layout, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.data_widget_layout, View.VISIBLE);
                remoteViews.setTextViewText(R.id.receipe_name, sharedPreferences.getString(Constants.RECEIPE_NAME, null));

                Intent intent1 = new Intent(context, WidgetService.class);
                remoteViews.setRemoteAdapter(R.id.widgetlistview, intent1);


            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetlistview);

            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widgetlistview);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        Log.v("in action", action);
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WidgetDataProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widgetlistview);
            //onUpdate(context,appWidgetManager,appWidgetManager.getAppWidgetIds(componentName));
        }
        super.onReceive(context, intent);
    }


}


