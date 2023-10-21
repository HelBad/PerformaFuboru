package com.example.performafuboru.view.staff.izinkeluar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.performafuboru.R
import com.example.performafuboru.api.ApiStaff
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin_detail.*
import org.json.JSONObject

class ActivityIzinDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin_detail)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        getData()

        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivityIzinDetail, ActivityIzin::class.java))
            finish()
        }
    }

    private fun getData() {
        val fetchData = FetchData(
            ApiStaff.IZIN_DETAIL +
                "?kode_izin=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")

                    clockinDetail.text = dataObject.getString("clockin")
                    clockoutDetail.text = dataObject.getString("clockout")
                    locinDetail.text = "(" + dataObject.getString("gps_in") + ")"
                    locoutDetail.text = "(" + dataObject.getString("gps_out") + ")"
                    kodeDetail.text = dataObject.getString("create_by")
                    namaDetail.text = dataObject.getString("nama")
                    keperluanDetail.text = dataObject.getString("keperluan") + " - " + dataObject.getString("keterangan")
                    ketDetail.text = dataObject.getString("catatan")
                    when (dataObject.getString("status")) {
                        "A" -> { statusDetail.text = "STATUS : DISETUJUI" }
                        "C" -> { statusDetail.text = "STATUS : SELESAI" }
                        else -> { statusDetail.text = "STATUS : MENUNGGU" }
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzinDetail, ActivityIzin::class.java))
        finish()
    }
}