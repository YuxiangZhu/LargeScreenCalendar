package com.ly.tinycalendar.bean;

import android.graphics.Color;

import com.ly.tinycalendar.R;

public class AttrsBean {

    private int[] startDate;//日历的开始年、月
    private int[] endDate;//日历的结束年、月
    private int[] singleDate;//单选是默认选中的日期（年、月、日）
    private int[] disableStartDate;//单选时默认选中的年、月、日disableStar
    private int[] disableEndDate;//单选时默认选中的年、月、日
    private boolean showLastNext = false;//是否显示上个月、下个月
    private boolean switchChoose = true;//单选时切换月份，是否选中上次的日期
    private int colorSolar = Color.BLACK;//阳历的日期颜色
    private int colorChoose = Color.WHITE;//选中的日期文字颜色
    private int sizeSolar = 15;//阳历日期文字尺寸
    private int dayBg = R.drawable.date_sel_bg;//选中的背景

    public int[] getStartDate() {
        return startDate;
    }

    public void setStartDate(int[] startDate) {
        this.startDate = startDate;
    }

    public int[] getEndDate() {
        return endDate;
    }

    public void setEndDate(int[] endDate) {
        this.endDate = endDate;
    }

    public int[] getSingleDate() {
        return singleDate;
    }

    public void setSingleDate(int[] singleDate) {
        this.singleDate = singleDate;
    }

    public int[] getDisableStartDate() {
        return disableStartDate;
    }

    public void setDisableStartDate(int[] disableStartDate) {
        this.disableStartDate = disableStartDate;
    }

    public int[] getDisableEndDate() {
        return disableEndDate;
    }

    public void setDisableEndDate(int[] disableEndDate) {
        this.disableEndDate = disableEndDate;
    }

    public boolean isShowLastNext() {
        return showLastNext;
    }

    public void setShowLastNext(boolean showLastNext) {
        this.showLastNext = showLastNext;
    }

    public boolean isSwitchChoose() {
        return switchChoose;
    }

    public void setSwitchChoose(boolean switchChoose) {
        this.switchChoose = switchChoose;
    }

    public int getColorSolar() {
        return colorSolar;
    }

    public void setColorSolar(int colorSolar) {
        this.colorSolar = colorSolar;
    }

    public int getColorChoose() {
        return colorChoose;
    }

    public void setColorChoose(int colorChoose) {
        this.colorChoose = colorChoose;
    }

    public int getSizeSolar() {
        return sizeSolar;
    }

    public void setSizeSolar(int sizeSolar) {
        this.sizeSolar = sizeSolar;
    }

    public int getDayBg() {
        return dayBg;
    }

    public void setDayBg(int dayBg) {
        this.dayBg = dayBg;
    }
}
