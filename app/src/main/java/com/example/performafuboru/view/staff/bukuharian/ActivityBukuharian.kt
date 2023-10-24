package com.example.performafuboru.view.staff.bukuharian

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.performafuboru.R
import com.example.performafuboru.view.staff.ActivityBeranda
import kotlinx.android.synthetic.main.staff_activity_bukuharian.*

class ActivityBukuharian : AppCompatActivity() {
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_bukuharian)

        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)

        toolbarHarian.text = intent.getStringExtra("title").toString()
        backHarian.setOnClickListener {
            webHarian.webViewClient = WebViewClient()
            webHarian.loadUrl("http://dev.fuboru.co.id:8080/bukuharian/logout.php")

            startActivity(Intent(this@ActivityBukuharian, ActivityBeranda::class.java))
            finish()
        }

        webHarian.webViewClient = WebViewClient()
        webHarian.loadUrl("http://dev.fuboru.co.id:8080/bukuharian/login.php" +
                "?username=" + SP.getString("username", "").toString() +
                "&password=password123&mode=android")
        webHarian.settings.blockNetworkLoads = false
        webHarian.settings.javaScriptEnabled = true
        webHarian.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {}
}