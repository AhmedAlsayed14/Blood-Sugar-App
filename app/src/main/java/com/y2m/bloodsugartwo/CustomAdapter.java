//package y2m.com.sugartracker;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
///**
// * Created by user on 3/6/2016.
// */
//public class CustomAdapter extends ArrayAdapter<Item> {
//    public CustomAdapter(Context context, ArrayList<Item> items) {
//        super(context, 0, items);
//        Log.d("CustomAdapter","CustomAdapter: "+items.size());
//
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        Item item = getItem(position);
//        Log.d("CustomAdapter","CustomAdapter: "+item.getType());
//
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
//        }
//        TextView unit = (TextView) convertView.findViewById(R.id.unit);
//        TextView value = (TextView) convertView.findViewById(R.id.value);
//        TextView date = (TextView) convertView.findViewById(R.id.date);
//        TextView type = (TextView) convertView.findViewById(R.id.type);
//        String font = "ae_AlYermook.ttf";
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
//        type.setTypeface(tf);
//        unit.setTypeface(tf);
//        value.setTypeface(tf);
//        date.setTypeface(tf);
//
//        type.setText(item.getType());
//        value.setText(item.getValue());
//        date.setText(item.getDate() + "  "+item.getTime());
//        return convertView;
//    }
//}

package com.y2m.bloodsugartwo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class CustomAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final ArrayList<Item> items;
    LayoutInflater inflater;
    private static final int VIEW_TYPE_COUNT = 1;
    private static final int VIEW_TYPE_SUGAR_ITEM= 0;
    private ViewHolder viewHolder ;
    public CustomAdapter(Context context, ArrayList<Item> array ) {
        super(context, 0 , array);
        this.context = context;
        this.items = array;
        Log.d("CustomAdapter","CustomAdapter: "+items.size());
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        Log.d("CustomAdapter","getCount: "+items.size());
        return items.size();
    }
    @Override
    public Item getItem(int position) {
        Log.d("CustomAdapter","getItem: "+position);
        Log.d("CustomAdapter","item: "+items.get(position).getType());

        return items.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_SUGAR_ITEM;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
    private class ViewHolder {
        private TextView unit,value,date,type;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int type = getItemViewType(position);
        Log.d("CustomAdapter","type: "+type);
        Log.d("CustomAdapter","position: "+position);
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            switch (type) {
                case VIEW_TYPE_SUGAR_ITEM:
                    convertView = inflater.inflate(R.layout.list_item,parent,false);
                    viewHolder.unit = (TextView) convertView.findViewById(R.id.unit);
                    viewHolder.value = (TextView) convertView.findViewById(R.id.value);
                    viewHolder.date = (TextView) convertView.findViewById(R.id.time);
                    viewHolder.type = (TextView) convertView.findViewById(R.id.type);
                    break;
                default:
                    break;
            }
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();
        String font = "ae_AlYermook.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
        viewHolder.type.setTypeface(tf);
        viewHolder.unit.setTypeface(tf);
        viewHolder.value.setTypeface(tf);
        viewHolder.date.setTypeface(tf);
        int item= Integer.valueOf(items.get(position).getType());
        switch(item)
        {
            case 0:
                viewHolder.type.setText(context.getString(R.string.type1));
                break;
            case 1:
                viewHolder.type.setText(context.getString(R.string.type2));
                break;
            case 2:
                viewHolder.type.setText(context.getString(R.string.type3));
                break;
        }
        viewHolder.value.setText(String.valueOf(items.get(position).getValue()));
        Calendar cl = Calendar.getInstance();
        Log.d("////////// =","TimeInSec"+items.get(position).getTimeInSeconds());
        String timeText= String.valueOf(items.get(position).getTimeInSeconds());
        Log.d("////////// =","timeText"+timeText);

        timeText+="000";
        Log.d("////////// =","timeText"+timeText);

        long timeInMillSec= Long.valueOf(timeText);
        Log.d("////////// =","timeInMillSec"+timeInMillSec);

        cl.setTimeInMillis(timeInMillSec);  //here your time in miliseconds
        String date = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + (cl.get(Calendar.MONTH)+1) + ":" + cl.get(Calendar.YEAR);
        String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE);
        viewHolder.date.setText(date + "  " + time);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("typeIndex", Integer.valueOf(items.get(position).getType()));
                i.putExtra("value", Integer.valueOf(items.get(position).getValue()) );
                i.putExtra("timeInSec",items.get(position).getTimeInSeconds());
                i.putExtra("id",items.get(position).getId());
                context.startActivity(i);
            }
        });
        return convertView;
    }
}