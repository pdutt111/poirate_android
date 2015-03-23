package com.poirate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.poirate.dataModels.ActivityModel;
import com.poirate.dataModels.ActivityResp;
import com.poirate.dataModels.SearchModel;
import com.poirate.dataModels.SearchResult;
import com.poirate.interfaces.ActivitiesResponse;
import com.poirate.interfaces.SearchResponse;
import com.poirate.network.ServerCoordinator;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class launch extends Activity {
    @InjectView(R.id.date_start) protected TextView dateStart;
    @InjectView(R.id.date_end) protected TextView dateEnd;
    @InjectView(R.id.plan) protected Button plan;
    @InjectView(R.id.searchView) protected SearchView search;
    @InjectView(R.id.attractions_list) protected ListView attractionsList;

    protected ServerCoordinator coordinator;
    SearchAdapter searchAdapter;
    String query;
    SearchModelClass searchmodel=new SearchModelClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.inject(this);
        Calendar c=Calendar.getInstance();
        int mYear=c.get(Calendar.YEAR);
        String mMonth=getMonthForInt(c.get(Calendar.MONTH));
        int mDay=c.get(Calendar.DAY_OF_MONTH);
        dateStart.setText(new StringBuilder().append(mDay).append("/").append(mMonth).append("/").append(mYear));
        dateEnd.setText(new StringBuilder().append(mDay+2).append("/").append(mMonth).append("/").append(mYear));
        search.setQueryHint("Enter Destination");
        coordinator=new ServerCoordinator();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // TODO Auto-generated method stub
                if(query.length()>=3){
                    clearsearch();
                    coordinator.search(query,searchmodel);
                }
                return false;
            }
        });
        String[] columns = new String[] { "_id", "text" };
        MatrixCursor cursor = new MatrixCursor(columns);

        searchAdapter = new SearchAdapter(this,cursor,true);

        search.setSuggestionsAdapter(searchAdapter);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

    }
    @OnClick(R.id.plan)
    public void plan(){

    }

    @OnClick({ R.id.date_start, R.id.date_end})
    public void pickdate(final TextView dateView) {
        Calendar c=Calendar.getInstance();
        int mYear=c.get(Calendar.YEAR);
        int mMonth=c.get(Calendar.MONTH);
        int mDay=c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month=getMonthForInt(monthOfYear);
                dateView.setText(new StringBuilder().append(dayOfMonth).append("/").append(month).append("/").append(year));
                if(dateView.equals(dateStart)) {
                    dateEnd.setText(new StringBuilder().append(dayOfMonth+2).append("/").append(month).append("/").append(year));
                }
            }
        },mYear,mMonth,mDay);
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
   public class SearchModelClass implements SearchResponse {

       @Override
       public void response(SearchModel responses) {
//           Log.i("search responses",responses.results.get(0).name);
            loadSearchData(responses);
       }

   }
    public class ActivitiesModelClass implements ActivitiesResponse{

        @Override
        public void response(ActivityModel responses) {
            loadActivities(responses);
        }
    }
    protected void clearsearch(){
//        searchAdapter.notifyDataSetInvalidated();
//        String[] columns = new String[] { "_id", "text" };
//        cursor = new MatrixCursor(columns);
//        searchAdapter.changeCursor(cursor);
//        searchAdapter.setDataset(new ArrayList<SearchResult>());
//        searchAdapter.notifyDataSetChanged();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadSearchData(SearchModel responses) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            List<SearchResult> items=responses.results;
            // Load data from list to cursor
            String[] columns = new String[] { "_id", "text","city_id" };
            Object[] temp = new Object[] { 0, "default","default" };

            MatrixCursor cursor = new MatrixCursor(columns);
            for(int i = 0; i < items.size(); i++) {

                temp[0] = i;
                temp[1] = items.get(i).name+" , "+items.get(i).state;
                temp[2] = items.get(i)._id;

                cursor.addRow(temp);
            }
            searchAdapter.notifyDataSetInvalidated();
            searchAdapter.changeCursor(cursor);
            searchAdapter.notifyDataSetChanged();

        }

    }

    private void loadActivities(ActivityModel activityModel){
        Log.i("activities loaded","activities loaded");
        android.widget.ListAdapter attractionsListAdapter=new ListAdapter(this,0,activityModel.activities);
        attractionsList.setAdapter(attractionsListAdapter);

    }

    public class SearchAdapter extends CursorAdapter {



        public SearchAdapter(Context context,Cursor cursor,Boolean autorequery) {

            super(context, cursor, autorequery);


        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // Show list item data from cursor
            final TextView text=(TextView)view.findViewById(R.id.text);
                final String id=cursor.getString(2);
                text.setText(cursor.getString(1));
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        search.setQuery(text.getText(), false);
                        coordinator.searchActivities(id,new ActivitiesModelClass());
                        Log.i("text clicked",id);
                    }
                });
            // Alternatively show data direct from database
            //text.setText(cursor.getString(cursor.getColumnIndex("column_name")));

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.item, parent, false);

            return view;

        }

    }

    public class ListAdapter extends ArrayAdapter<ActivityResp> {


        public ListAdapter(Context context, int resource,List<ActivityResp> activityResps) {
            super(context, resource,activityResps);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ActivityResp activity =getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.attractions_list, parent, false);
            }
            // Lookup view for data population
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.description);
            // Populate the data into the template view using the data object
            title.setText(activity.name);
            desc.setText(activity.description);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
