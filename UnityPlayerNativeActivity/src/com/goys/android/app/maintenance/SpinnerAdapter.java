package com.goys.android.app.maintenance;

import com.goys.android.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter  extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    String[] array;
    Context context;

    public SpinnerAdapter(Context context, String[] array) {
    	super(context, 0, array);
    	
        mInflater = LayoutInflater.from(context);
        this.array = array;
        this.context= context;
    }

    @Override
    public int getCount() {
        return array.length;
    }

   
    @Override
    public long getItemId(int position) {
        return position;
    }

    // A view to hold each row in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	return getCustomView(position, convertView, parent);
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	return getCustomView(position, convertView, parent);
    }
    
    public View getCustomView(int position, View convertView, ViewGroup parent) {
    	
    	
         View row = mInflater.inflate(R.layout.item_spinner, null);
         
         TextView text = (TextView) row.findViewById(R.id.tv_spinner);
         
         text.setText(array[position]);
         
         /*if (position==0 && array[position].equalsIgnoreCase(array[0])){
         	text.setTextColor(context.getResources().getColor(R.color.hint_color));	
         }*/
         
         
         
         return row;
    	
    }
    
    static class ViewHolder {
        TextView text;
    }
}
