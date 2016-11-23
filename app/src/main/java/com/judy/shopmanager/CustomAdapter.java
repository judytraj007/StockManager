package com.judy.shopmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Judy T Raj on 15-11-2016.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> brands= new ArrayList<>();
    int[] ard,chmd,brandTotal;
    LayoutInflater inflater;
    public CustomAdapter(Context context,ArrayList<String> brands,int[] ard, int[] chmd,int[] brandTotal){
        this.context=context;
        this.brands=brands;
        this.ard=new int[brands.size()];
        this.chmd=new int[brands.size()];
        this.brandTotal=new int[brands.size()];
        this.ard=ard;
        this.chmd=chmd;
        this.brandTotal=brandTotal;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return brands.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    public class Holder
    {
        TextView brand;
        TextView ard;
        TextView chmd;
        TextView total;

    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.reportview, null);
     holder.brand=(TextView)rowView.findViewById(R.id.brandText);
        holder.ard=(TextView) rowView.findViewById(R.id.ardCount);
        holder.chmd=(TextView) rowView.findViewById(R.id.chmdCount);
        holder.total=(TextView) rowView.findViewById(R.id.totalCount);
        holder.brand.setText(brands.get(position));
        holder.ard.setText(String.valueOf(ard[position]));
        holder.chmd.setText(String.valueOf(chmd[position]));
        holder.total.setText(String.valueOf(brandTotal[position]));
        return rowView;
    }
}
