package com.ly.tinycalendar.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ly.tinycalendar.R;
import com.ly.tinycalendar.bean.AttrsBean;
import com.ly.tinycalendar.bean.DateBean;
import com.ly.tinycalendar.listener.CalendarViewAdapter;
import com.ly.tinycalendar.listener.OnSingleChooseListener;
import com.ly.tinycalendar.utils.CalendarUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonthView extends ViewGroup {

    private static final int ROW = 6;
    private static final int COLUMN = 7;

    private static final int COLOR_RESET = 0;//重置文字颜色
    private static final int COLOR_SET = 1;//设置文字颜色

    private Context mContext;

    private View lastClickedView;//记录上次点击的Item
    private int currentMonthDays;//记录当月天数
    private int lastMonthDays;//记录当月显示的上个月天数
    private int nextMonthDays;//记录当月显示的下个月天数

    private Set<Integer> chooseDays = new HashSet<>();//记录多选时当前页选中的日期
    private AttrsBean mAttrsBean;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        mContext = context;
        setBackgroundColor(Color.WHITE);
    }

    /**
     * @param dates            需要展示的日期数据
     * @param currentMonthDays 当月天数
     */
    public void setDateList(List<DateBean> dates, int currentMonthDays) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        lastMonthDays = 0;
        nextMonthDays = 0;
        chooseDays.clear();

        this.currentMonthDays = currentMonthDays;
        for (int i = 0; i < dates.size(); i++) {
            final DateBean date = dates.get(i);

            if (date.getType() == 0) {
                lastMonthDays++;
                if (!mAttrsBean.isShowLastNext()) {
                    addView(new View(mContext), i);
                    continue;
                }
            }

            if (date.getType() == 2) {
                nextMonthDays++;
                if (!mAttrsBean.isShowLastNext()) {
                    addView(new View(mContext), i);
                    continue;
                }
            }

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_month_layout, null);
            TextView solarDay = view.findViewById(R.id.solar_day);

            solarDay.setTextColor(mAttrsBean.getColorSolar());
            solarDay.setTextSize(TypedValue.COMPLEX_UNIT_PX,mAttrsBean.getSizeSolar());

            //设置上个月和下个月的阳历颜色
            if (date.getType() == 0 || date.getType() == 2) {
                solarDay.setTextColor(Color.parseColor("#e6e6e6"));
            }
            solarDay.setText(String.valueOf(date.getSolar()[2]));

            //找到单选时默认选中的日期，并选中（如果有）

//            LogUtils.e("mAttrsBean.getSingleDate()"+mAttrsBean.getSingleDate()+"--"+date.getType()+"----"+solarDay.getText().toString());

            if (mAttrsBean.getSingleDate() != null
                    && date.getType() == 1
                    && mAttrsBean.getSingleDate()[0] == date.getSolar()[0]
                    && mAttrsBean.getSingleDate()[1] == date.getSolar()[1]
                    && mAttrsBean.getSingleDate()[2] == date.getSolar()[2]) {
                lastClickedView = view;
                setDayColor(view, COLOR_SET);

            }


            //设置禁用日期
            view.setTag(date);
            if (mAttrsBean.getDisableStartDate() != null
                    && (CalendarUtil.dateToMillis(mAttrsBean.getDisableStartDate()) > CalendarUtil.dateToMillis(date.getSolar()))) {
                solarDay.setTextColor(Color.parseColor("#e6e6e6"));
                view.setTag(null);
                addView(view, i);
                continue;
            }


            if (mAttrsBean.getDisableEndDate() != null
                    && (CalendarUtil.dateToMillis(mAttrsBean.getDisableEndDate()) < CalendarUtil.dateToMillis(date.getSolar()))) {
                solarDay.setTextColor(Color.parseColor("#e6e6e6"));
                view.setTag(null);
                addView(view, i);
                continue;
            }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int day = date.getSolar()[2];
                    CalendarView calendarView = (CalendarView) getParent();
                    OnSingleChooseListener clickListener = calendarView.getSingleChooseListener();
                    if (date.getType() == 1) {//点击当月
                        calendarView.setLastClickDay(date);
                        if (lastClickedView != null) {
                            setDayColor(lastClickedView, COLOR_RESET);
                        }
                        setDayColor(v, COLOR_SET);
                        lastClickedView = v;

                        if (clickListener != null) {
                            clickListener.onSingleChoose(v, date);
                        }
                    } else if (date.getType() == 0) {//点击上月
                        if (mAttrsBean.isSwitchChoose()) {
                            calendarView.setLastClickDay(date);
                        }
                        calendarView.lastMonth();
                        if (clickListener != null) {
                            clickListener.onSingleChoose(v, date);
                        }
                    } else if (date.getType() == 2) {//点击下月
                        if (mAttrsBean.isSwitchChoose()) {
                            calendarView.setLastClickDay(date);
                        }
                        calendarView.nextMonth();
                        if (clickListener != null) {
                            clickListener.onSingleChoose(v, date);
                        }
                    }
                }
            });
            addView(view, i);
        }
        requestLayout();
    }


    private void setDayColor(View v, int type) {
        TextView solarDay = v.findViewById(R.id.solar_day);
        solarDay.setTextSize(TypedValue.COMPLEX_UNIT_PX,mAttrsBean.getSizeSolar());

//        LogUtils.e(solarDay.getText().toString()+"颜色: "+type);

        if (type == 0) {
            v.setBackgroundResource(0);
            solarDay.setTextColor(mAttrsBean.getColorSolar());
        } else if (type == 1) {
            v.setBackgroundResource(mAttrsBean.getDayBg());
            solarDay.setTextColor(mAttrsBean.getColorChoose());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int itemWidth = widthSpecSize / COLUMN;

        //计算日历的最大高度
        if (heightSpecSize > itemWidth * ROW) {
            heightSpecSize = itemWidth * ROW;
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);

        int itemHeight = heightSpecSize / ROW;

        int itemSize = Math.min(itemWidth, itemHeight);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }

        View childView = getChildAt(0);
        int itemWidth = childView.getMeasuredWidth();
        int itemHeight = childView.getMeasuredHeight();
        //计算列间距
        int dx = (getMeasuredWidth() - itemWidth * COLUMN) / (COLUMN * 2);

        //当显示五行时扩大行间距
        int dy = 0;
        if (getChildCount() == 35) {
            dy = itemHeight / 5;
        }

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);

            int left = i % COLUMN * itemWidth + ((2 * (i % COLUMN) + 1)) * dx;
            int top = i / COLUMN * (itemHeight + dy);
            int right = left + itemWidth;
            int bottom = top + itemHeight;
            view.layout(left, top, right, bottom);
        }
    }

    public void refresh(DateBean date, boolean flag) {
        if (lastClickedView != null) {
            setDayColor(lastClickedView, COLOR_RESET);
        }
        if (!flag) {
            return;
        }
        View destView = findDestView(date);
        if (destView == null) {
            return;
        }
        setDayColor(destView, COLOR_SET);
        lastClickedView = destView;
        invalidate();
    }

    /**
     * 查找要跳转到的页面需要展示的日期View
     *
     * @param date
     * @return
     */
    private View findDestView(DateBean date) {
        if (date == null || date.getSolar() == null || date.getSolar().length != 3) {
            return null;
        }
        View view = null;

        for (int i = lastMonthDays; i < getChildCount() - nextMonthDays; i++) {
            if (getChildAt(i).getTag() == null) {
                continue;
            }
            DateBean tag = (DateBean) getChildAt(i).getTag();
            if (tag.getSolar()[2] == date.getSolar()[2]
                    && tag.getSolar()[0] == date.getSolar()[0]
                    && tag.getSolar()[1] == date.getSolar()[1]) {
                view = getChildAt(i);
                break;
            }
        }

        if (view == null) {
            //view = getChildAt(currentMonthDays + lastMonthDays - 1);
        }

        if (view != null && view.getTag() == null) {
            view = null;
        }
        return view;
    }

    public void setAttrsBean(AttrsBean attrsBean) {
        mAttrsBean = attrsBean;
    }

}
