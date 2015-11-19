package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.FootballScoresService;

/**
 * Created by Bradley on 11/11/15.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        for (int i = 0; i < count; i++) {
            Intent intent = new Intent(context, FootballScoresService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.simple_widget);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.scores_list, intent);
            rv.setEmptyView(R.id.scores_list, R.id.empty_view);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.v("Buntin", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.v("Buntin", "onDisabled");
    }

}