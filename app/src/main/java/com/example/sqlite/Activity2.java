package com.example.sqlite;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    private Button logoutButton;
    private CustomArrayAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView search;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //Database Intialization
        openHelper = new DatabaseHelper(this);

        //Fields Intialization
        logoutButton = (Button) findViewById(R.id.logoutButton);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        search = (SearchView) findViewById(R.id.searchView);


        // Set recycler View
        setRecyclerView();

        // Item Touch Helper
        setItemTouchHelper();

        //setting loading Task
        setAsyncTask();

        //setting Search View
        setSearchView();


        //Seting logout Button
        setLogoutButton();

    }


    public void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomArrayAdapter(Activity2.this, getAllItems());
        recyclerView.setAdapter(adapter);

    }

    public void setItemTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
                Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
    private void removeItem(long id){
        db.delete(DatabaseHelper.listTableName,
                DatabaseHelper.listCol_1 + "=" + id, null);
        adapter.swapCursor(getAllItems());
    }

    public Cursor getListByKeyword(String search) {
        //Open connection to read only
        db = openHelper.getReadableDatabase();
        String selectQuery =  "SELECT  rowid as " +
                DatabaseHelper.listCol_1 + "," +
                DatabaseHelper.listCol_2 + "," +
                DatabaseHelper.listCol_3 + "," +
                DatabaseHelper.listCol_4 + "," +
                DatabaseHelper.listCol_5 +
                " FROM " + DatabaseHelper.listTableName +
                " WHERE " +  DatabaseHelper.listCol_2 + " LIKE '%" + search + "%' "
                ;


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;

    }

    public void setSearchView(){

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cursor = getListByKeyword(newText);
                if(cursor != null){
                    adapter.swapCursor(cursor);
                }
                return false;
            }
        });
    }

    private Cursor getAllItems() {
        db = openHelper.getReadableDatabase();
        return db.query(
                DatabaseHelper.listTableName,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.listCol_1 + " asc"
        );
    }

    public void addData(String label, String timestamp, String speed, String road){
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.listCol_2, label);
        contentValues.put(DatabaseHelper.listCol_3, timestamp);
        contentValues.put(DatabaseHelper.listCol_4, speed);
        contentValues.put(DatabaseHelper.listCol_5, road);
        long id =  db.insert(DatabaseHelper.listTableName, null, contentValues);

    }

    public void setAsyncTask(){
        MyLodingTask mAsyncTask = new MyLodingTask();
        mAsyncTask.execute();
    }

    private class MyLodingTask extends AsyncTask<Integer, String, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... arg0) {
            return loading();
        }

    }

    private boolean loading(){
        try {
            HttpHandler sh = new HttpHandler();
            String xml = sh.makeServiceCall("http://203.125.7.40/V3nity4/AssetController?type=track&action=last&by=user&asset=1113,1115");
            if (!xml.trim().equals("")) {
                JSONObject json = new JSONObject(xml);
                String resultString = json.getString("result");
                if (resultString.equals("true")) {
                    // Getting JSON ARRAY NOODE
                    JSONArray data = json.getJSONArray("data");


                    //Looping Through all DATA
                    for(int i=0; i<data.length(); i++){
                        JSONObject d = data.getJSONObject(i);
                        String name = d.getString("label");
                        String time = "TimeStamp : " + d.getString("timestamp");
                        String road = d.getString("road");
                        String speed  = "Speed : " + d.getString("speed");
                        addData(name, time, speed, road);

                    }

                    return true;
                }
            }

        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }

        return false;

    }

    public void setLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
