package com.ly.largescreencalendar

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.ly.largescreencalendar.databinding.FragmentCalendarBinding
import com.ly.tinycalendar.utils.CalendarUtil
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者： Alex
 * 日期： 2020-04-28
 * 签名： 保持学习

 * ----------------------------------------------------------------
 *日历
 */
class CalendarDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentCalendarBinding
    private var cDate = CalendarUtil.getCurrentDate()
    private lateinit var selectedDate: IntArray
    private var selectedCalendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy年MM月")
    private val sdf1 = SimpleDateFormat("M月")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        selectedDate = CalendarUtil.getCurrentDate()


    }


    /**
     * 设置dialog宽度为屏幕的的百分比
     */
    override fun onStart() {
        super.onStart()

        dialog?.let {

            /**
             * 背景透明
             */
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            it.window?.setLayout(
                960,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)

        //点击屏幕其他地方消失
        dialog?.setCanceledOnTouchOutside(true)

        initView(20)

        binding.ivCancel.setOnClickListener {
            dismiss()
        }


        /**
         * 跳到上一个月
         */
        binding.layoutPre.setOnClickListener {

            binding.calendar.lastMonth()

        }


        /**
         * 跳到下一个月
         */
        binding.layoutNext.setOnClickListener {

            binding.calendar.nextMonth()
        }

        /**
         * 跳转到当天
         */
        binding.btnGoToday.setOnClickListener {

            binding.calendar.today()
        }


        return binding.root

    }


    /**
     * 当前月的UI
     */
    private fun currentMonthStyle() {

        binding.layoutPre.setBackgroundResource(R.drawable.shape_bg_round_base_unenable4)
        binding.layoutNext.setBackgroundResource(R.drawable.shape_bg_round_base_normal4)

        binding.tvNextMonth.setTextColor(Color.WHITE)
        binding.tvPreMonth.setTextColor(resources.getColor(R.color.main_base))

        binding.ivPre.setImageResource(R.drawable.ic_left_main)
        binding.ivNext.setImageResource(R.drawable.ic_right_white)

    }


    /**
     * 下一个月月的UI
     */
    private fun nextMonthStyle() {

        binding.layoutNext.setBackgroundResource(R.drawable.shape_bg_round_base_unenable4)
        binding.layoutPre.setBackgroundResource(R.drawable.shape_bg_round_base_normal4)

        binding.tvPreMonth.setTextColor(Color.WHITE)
        binding.tvNextMonth.setTextColor(resources.getColor(R.color.main_base))

        binding.ivPre.setImageResource(R.drawable.ic_left_white)
        binding.ivNext.setImageResource(R.drawable.ic_right_main)

    }


    private fun initView(day: Int) {

        //当前月的下一个月
        val cal = Calendar.getInstance()

        cal.add(Calendar.MONTH, 1)

        val cal4Day = Calendar.getInstance()
        cal4Day.add(Calendar.DAY_OF_YEAR, +day)

        binding.tvCurrentMonth.text = sdf.format(selectedCalendar.time)

        val pre = Calendar.getInstance()
        pre.set(Calendar.YEAR, selectedCalendar.get(Calendar.YEAR))
        pre.set(Calendar.MONTH, selectedCalendar.get(Calendar.MONTH))
        pre.add(Calendar.MONTH, -1)
        binding.tvPreMonth.text = sdf1.format(pre.time)

        pre.add(Calendar.MONTH, 2)
        binding.tvNextMonth.text = sdf1.format(pre.time)

        currentMonthStyle()


        binding.calendar.setStartEndDate(
            cDate[0].toString() + "." + cDate[1],
            cal[Calendar.YEAR]
                .toString() + "." + (cal[Calendar.MONTH] + 1)
        )
            .setDisableStartEndDate(
                cDate[0].toString() + "." + cDate[1] + "." + cDate[2],
                String.format(
                    "%d.%d.%d",
                    cal4Day[Calendar.YEAR],
                    cal4Day[Calendar.MONTH] + 1,
                    cal4Day[Calendar.DAY_OF_MONTH]
                )
            )
            .setInitDate(selectedDate[0].toString() + "." + selectedDate[1])
            .setSingleDate(selectedDate[0].toString() + "." + selectedDate[1] + "." + selectedDate[2])
            .init()


//月份切换回调
        binding.calendar.setOnPagerChangeListener { date ->
            binding.tvCurrentMonth.text = date[0].toString() + "年" + date[1] + "月"
            val pre = Calendar.getInstance()
            pre.set(Calendar.YEAR, date[0])
            pre.set(Calendar.MONTH, date[1] - 2)
            binding.tvPreMonth.text = sdf1.format(pre.time)

            pre.add(Calendar.MONTH, 2)
            binding.tvNextMonth.text = sdf1.format(pre.time)


            if (date[0] == cDate[0] && date[1] == cDate[1]) {

                currentMonthStyle()

            } else {

                nextMonthStyle()
            }


        }

//单选回调
        binding.calendar.setOnSingleChooseListener { view, dateBean ->
            binding.tvCurrentMonth.text =
                dateBean.solar[0].toString() + "年" + dateBean.solar[1] + "月"
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val strDate = String.format(
                "%d-%02d-%02d",
                dateBean.solar[0],
                dateBean.solar[1],
                dateBean.solar[2]
            )
            try {
                //拿到数据
                val daystart = df.parse(strDate)

//                dismiss()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }
}