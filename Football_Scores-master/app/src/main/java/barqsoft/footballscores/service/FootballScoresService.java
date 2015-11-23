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

import java.util.ArrayList;

import barqsoft.footballscores.R;

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
        fragmentdate[0] = "2015-11-21"; //now.toString();

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
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.simple_widget2);
        if (mCursor.moveToPosition(i)){


            rv.setTextViewText(R.id.textView, mCursor.getString(COL_HOME));
            //rv.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));
            //rv.setTextViewText(R.id.score_textview, mCursor.getString(CO));

            /*rv.setTextViewText(R.id.score_textview, mCursor.getString(COL_HOME_GOALS));
            rv.setTextViewText(R.id.data_textview, mCursor.getString(COL_HOME));
            rv.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));

            mHolder.home_name.setText(cursor.getString(COL_HOME));
            mHolder.away_name.setText(cursor.getString(COL_AWAY));
            mHolder.date.setText(cursor.getString(COL_MATCHTIME));
            mHolder.score.setText(Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
            mHolder.match_id = cursor.getDouble(COL_ID);
            mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                    cursor.getString(COL_HOME)));
            mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                    cursor.getString(COL_AWAY)
            ));
            //Log.v(FetchScoreTask.LOG_TAG,mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() +" id " + String.valueOf(mHolder.match_id));
            //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detail_match_id));
            LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.detail_fragment, null);
            ViewGroup container = (ViewGroup) view.f


            public TextView home_name;
            public TextView away_name;
            public TextView score;
            public TextView date;
            public ImageView home_crest;
            public ImageView away_crest;
            public double match_id;

            home_name = (TextView) view.findViewById(R.id.home_name);
            away_name = (TextView) view.findViewById(R.id.away_name);
            score     = (TextView) view.findViewById(R.id.score_textview);
            date      = (TextView) view.findViewById(R.id.data_textview);
            home_crest = (ImageView) view.findViewById(R.id.home_crest);
            away_crest = (ImageView) view.findViewById(R.id.away_crest); */






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

