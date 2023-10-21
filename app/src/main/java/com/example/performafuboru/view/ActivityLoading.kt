package com.example.performafuboru.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.performafuboru.R

class ActivityLoading : AppCompatActivity() {
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loading()
    }

    private fun loading() {
        val backgrond = object : Thread() {
            override fun run() {
                try {
                    sleep(2500)
                    if(SP.getString("level", "") == "") {
                        startActivity(Intent(this@ActivityLoading, ActivityLogin::class.java))
                        finish()
                    } else if(SP.getString("level", "") == "HRGA") {
                        startActivity(Intent(this@ActivityLoading,
                            com.example.performafuboru.view.admin.ActivityBeranda::class.java))
                        finish()
                    } else if(SP.getString("level", "") == "SECURITY") {
                        startActivity(Intent(this@ActivityLoading,
                            com.example.performafuboru.view.security.ActivityBeranda::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@ActivityLoading,
                            com.example.performafuboru.view.staff.ActivityBeranda::class.java))
                        finish()
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        backgrond.start()
    }

    override fun onBackPressed() {}
}