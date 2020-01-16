package com.example.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.sqlite.DatabaseHelper.listCol_1;
import static com.example.sqlite.DatabaseHelper.listCol_2;
import static com.example.sqlite.DatabaseHelper.listCol_3;
import static com.example.sqlite.DatabaseHelper.listCol_4;
import static com.example.sqlite.DatabaseHelper.listCol_5;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public CustomArrayAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder dataModel, int i) {
        if(!mCursor.moveToPosition(i)){
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(listCol_2));
        String speed = mCursor.getString(mCursor.getColumnIndex(listCol_4));
        String time = mCursor.getString(mCursor.getColumnIndex(listCol_3));
        String road = mCursor.getString(mCursor.getColumnIndex(listCol_5));
        long id = mCursor.getLong(mCursor.getColumnIndex(listCol_1));

        dataModel.nameText.setText(name);
        dataModel.speedText.setText(speed);
        dataModel.timeText.setText(time);
        dataModel.roadText.setText(road);
        dataModel.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView speedText;
        public TextView timeText;
        public TextView roadText;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            speedText = itemView.findViewById(R.id.textSpeed);
            timeText = itemView.findViewById(R.id.textTime);
            roadText = itemView.findViewById(R.id.textRoad);
        }
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }

        mCursor =  newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }








}
