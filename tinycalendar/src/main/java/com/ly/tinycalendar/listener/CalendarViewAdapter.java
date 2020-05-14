package com.ly.tinycalendar.listener;

import android.view.View;
import android.widget.TextView;

import com.ly.tinycalendar.bean.DateBean;

public interface CalendarViewAdapter {
    /**
     * 返回阳历TextView
     *
     * @param view
     * @param date
     * @return
     */
    TextView convertView(View view, DateBean date);
}
