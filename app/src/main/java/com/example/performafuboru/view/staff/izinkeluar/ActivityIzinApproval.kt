package com.example.performafuboru.view.staff.izinkeluar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.performafuboru.R
import com.example.performafuboru.api.ApiStaff
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin_approval.*
import org.json.JSONObject

class ActivityIzinApproval : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin_approval)

        getData()
        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivityIzinApproval, ActivityIzin::class.java))
            finish()
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiStaff.IZIN_DETAIL +
                "?kode_izin=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")

                    clockinDetail.text = dataObject.getString("waktu_izin")
                    clockoutDetail.text = dataObject.getString("waktu_kembali")
                    kodeDetail.text = dataObject.getString("create_by")
                    namaDetail.text = dataObject.getString("nama")
                    if(dataObject.getString("kembali") == "0") {
                        keperluanDetail.text = dataObject.getString("keperluan") + " - TIDAK KEMBALI"
                    } else {
                        keperluanDetail.text = dataObject.getString("keperluan") + " - KEMBALI"
                    }
                    ketDetail.text = dataObject.getString("keterangan")
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
        startActivity(Intent(this@ActivityIzinApproval, ActivityIzin::class.java))
        finish()
    }
}