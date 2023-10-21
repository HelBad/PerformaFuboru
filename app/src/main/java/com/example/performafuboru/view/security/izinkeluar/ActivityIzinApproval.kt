package com.example.performafuboru.view.security.izinkeluar

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.performafuboru.R
import com.example.performafuboru.api.ApiSecurity
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.security_activity_izin_approval.*
import org.json.JSONObject

class ActivityIzinApproval : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.security_activity_izin_approval)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        getData()

        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivityIzinApproval, ActivityIzin::class.java))
            finish()
        }
        btnSetuju.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menyetujui pengajuan ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        status = "A"
                        addApproval()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnTolak.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menolak pengajuan ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        status = "C"
                        addApproval()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }

    private fun getData() {
        val fetchData = FetchData(
            ApiSecurity.IZIN_DETAIL +
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
                    keperluanDetail.text = dataObject.getString("keperluan")
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

    private fun addApproval(){
        AndroidNetworking.post(ApiSecurity.IZIN_APPROVAL_2)
            .addBodyParameter("kode_izin", intent.getStringExtra("kode").toString())
            .addBodyParameter("approval_2", SP.getString("username", "").toString())
            .addBodyParameter("status", status)
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityIzinApproval, ActivityIzin::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzinApproval, ActivityIzin::class.java))
        finish()
    }
}