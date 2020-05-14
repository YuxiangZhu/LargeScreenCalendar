package com.ly.tinycalendar.listener;

import android.view.View;

import com.ly.tinycalendar.bean.DateBean;

/**
 * 日期点击接口
 */
public interface OnSingleChooseListener {
    /**
     * @param view
     * @param date
     */
    void onSingleChoose(View view, DateBean date);
}
