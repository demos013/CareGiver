package com.caregiver.CustomListview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.caregiver.R;


/**
 * Created by Demos on 11/27/2017.
 */

public class navigationlistview extends BaseAdapter {

    Context mContext;
    String[] strName;
    String userkey;
    boolean checked;


    public navigationlistview(Context mContext, String[] strName) {
        this.mContext = mContext;
        this.strName = strName;

    }

    @Override
    public int getCount() {
        return strName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = mInflater.inflate(R.layout.listview_row_navigation, viewGroup, false);
        TextView itemname = view.findViewById(R.id.item_navi);

        itemname.setText(strName[i]);

        /*if(i==3){
            HashMap<String,String> request = userdb.getFriendsrequest();
            itemname.setText(strName[i]+" ("+request.size()+")");*/





        return view;
    }
}


