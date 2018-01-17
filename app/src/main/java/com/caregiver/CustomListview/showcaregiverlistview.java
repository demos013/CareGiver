package com.caregiver.CustomListview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Demos on 1/17/2018.
 */

public class showcaregiverlistview extends BaseAdapter {

    Context mContext;
    String[] strName;

    public showcaregiverlistview(Context mContext, String[] strName) {
        this.mContext = mContext;
        this.strName = strName;
    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
