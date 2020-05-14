package com.ly.largescreencalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val tv =  findViewById<TextView>(R.id.tv)
        tv.setOnClickListener {

            val dialogFragment = CalendarDialogFragment()
            dialogFragment.show(supportFragmentManager,"")
        }

    }
}
