package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.text.format.Time;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Bradley on 11/18/15.
 */
public class FootballScoresService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new FootballScoresViewsFactory(this.getApplicationContext(), intent));
    }
}

class FootballScoresViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final int mCount = 10;
    private ArrayList listItemList = new ArrayList();
    private Context mContext;
    private int mAppWidgetId;
    private Cursor mCursor;

    public FootballScoresViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null){
            mCursor.close();
        }
        Time now = new Time();
        now.setToNow();
        String[] fragmentdate = new String[1];

        Date fragmentdateToday = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String date=mformat.format(fragmentdateToday);

        fragmentdate[0] = date;
        final long token = Binder.clearCallingIdentity();
        try {
            mCursor = mContext.getContentResolver().query(buildScoreWithDate(),null,null,fragmentdate,null);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    //URI data
    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static final String PATH = "scores";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static Uri buildScoreWithDate()
    {
        return BASE_CONTENT_URI.buildUpon().appendPath("date").build();
    }


    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        int count = mCursor.getCount();
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item_widget);
        if (mCursor.moveToPosition(i)){
            rv.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));
            rv.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));
            rv.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(mCursor.getString(COL_HOME)));
            rv.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(mCursor.getString(COL_AWAY)));
            rv.setTextViewText(R.id.score_textview, Utilies.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS)));
            rv.setTextViewText(R.id.data_textview, mCursor.getString(COL_MATCHTIME));

            Intent fillInIntent = new Intent();
            //fillInIntent.putExtra(Widget.EXTRA_LIST_VIEW_ROW_NUMBER, i);
            rv.setOnClickFillInIntent(R.id.id_widget_list_task_name, fillInIntent);
        }
        return rv;
    }

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

