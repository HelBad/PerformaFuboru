package com.example.performafuboru.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.performafuboru.R
import kotlinx.android.synthetic.main.activity_beranda.webView

class ActivityBeranda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        Toast.makeText(this@ActivityBeranda,
            intent.getStringExtra("url").toString(), Toast.LENGTH_SHORT).show()
        webView.webViewClient = WebViewClient()
        webView.loadUrl(intent.getStringExtra("url").toString())
        webView.settings.blockNetworkLoads = false
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
    }
}