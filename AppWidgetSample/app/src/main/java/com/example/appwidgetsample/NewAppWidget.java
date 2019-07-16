package com.example.appwidgetsample;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget<count> extends AppWidgetProvider {


    private static final String mSharedPreFile = "com.example.android.appwidgetsample";
    private static final String COUNT_KEY = "count";




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(mSharedPreFile, 0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;

        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        SharedPreferences.Editor preEditor = prefs.edit();
        preEditor.putInt(COUNT_KEY + appWidgetId, count);
        preEditor.apply();

        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));

        views.setTextViewText(R.id.appwidget_update, context.getResources().getString(
                R.string.date_count_format, count, dateString));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

