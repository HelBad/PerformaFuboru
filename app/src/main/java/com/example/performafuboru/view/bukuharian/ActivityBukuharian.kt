package com.example.performafuboru.view.bukuharian

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.performafuboru.R
import com.example.performafuboru.view.ActivityBeranda
import kotlinx.android.synthetic.main.activity_bukuharian.*

class ActivityBukuharian : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukuharian)

        alertDialog = AlertDialog.Builder(this)
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