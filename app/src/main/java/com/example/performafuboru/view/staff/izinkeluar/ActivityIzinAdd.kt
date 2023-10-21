package com.example.performafuboru.view.staff.izinkeluar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.performafuboru.R
import com.example.performafuboru.api.ApiStaff
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin_add.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class ActivityIzinAdd : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var formatDate1 = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    var formatDate2 = SimpleDateFormat("YYYY-MM-dd")
    var formatTime = SimpleDateFormat("HH:mm")
    var formatY = SimpleDateFormat("YY")
    val kalender = Calendar.getInstance()
    var izin: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin_add)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        izin = arrayListOf("", "", "", "", "", "", "", "", "")
        getData()
        setData()

        cardWaktu.setOnClickListener {
            setWaktu()
        }
        cardBatas.setOnClickListener {
            setBatas()
        }
        btnPengajuan.setOnClickListener {
            alertDialog.setMessage("Apakah data form pengajuan sudah benar ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        simpanIzin()
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
        val fetchData1 = FetchData(ApiStaff.IZIN_COUNT)
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result: String = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    izin[7] = String.format("%03d", dataObject.getString("COUNT(*)").toInt() + 1)
                } catch (t: Throwable) { }
            }
        }

        val fetchData2 = FetchData(
            ApiStaff.PENGGUNA +
                "?kode=" + SP.getString("username", "").toString())
        if (fetchData2.startFetch()) {
            if (fetchData2.onComplete()) {
                val result: String = fetchData2.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaAdd.text = dataObject.getString("nama")
                } catch (t: Throwable) { }
            }
        }

        izin[8] = SP.getString("username", "").toString() + "/IKK/" +
                formatY.format(Date()).toString() + "/" + izin[7]
    }

    private fun setWaktu() {
        val tanggal = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            izin[0] = formatDate2.format(selectedDate.time)

            val waktu = TimePickerDialog(this, {
                    view, hourOfDay, minute -> val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                izin[1] = formatTime.format(selectedTime.time)
                waktuAdd.text = izin[0] + " " + izin[1] + ":00"

            }, kalender.get(Calendar.HOUR_OF_DAY), kalender.get(Calendar.MINUTE), false)
            waktu.show()
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggal.show()
    }

    private fun setBatas() {
        val tanggal = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            izin[2] = formatDate2.format(selectedDate.time)

            val waktu = TimePickerDialog(this, {
                    view, hourOfDay, minute -> val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                izin[3] = formatTime.format(selectedTime.time)
                batasAdd.text = izin[2] + " " + izin[3] + ":00"

            }, kalender.get(Calendar.HOUR_OF_DAY), kalender.get(Calendar.MINUTE), false)
            waktu.show()
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggal.show()
    }

    private fun setData() {
        hoAdd.setOnClickListener {
            hoAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            areaAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            izin[4] = "HO"
            izin[5] = "T1"
        }
        areaAdd.setOnClickListener {
            hoAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            areaAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            izin[4] = "AREA"
            izin[5] = "A"
        }

        dinasAdd.setOnClickListener {
            dinasAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            pribadiAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            izin[6] = "DINAS"
        }
        pribadiAdd.setOnClickListener {
            dinasAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            pribadiAdd.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            izin[6] = "PRIBADI"
        }
    }

    private fun simpanIzin() {
        AndroidNetworking.post(ApiStaff.IZIN_ADD)
            .addBodyParameter("kode_izin", izin[8])
            .addBodyParameter("waktu_izin", waktuAdd.text.toString())
            .addBodyParameter("waktu_kembali", batasAdd.text.toString())
            .addBodyParameter("keperluan", izin[6])
            .addBodyParameter("keterangan", alasanAdd.text.toString())
            .addBodyParameter("create_by", SP.getString("username", "").toString())
            .addBodyParameter("create_date", formatDate1.format(Date()).toString())
            .addBodyParameter("flag", izin[4])
            .addBodyParameter("approval_1", "")
            .addBodyParameter("approval_2", "")
            .addBodyParameter("status", izin[5])
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityIzinAdd, ActivityIzin::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzinAdd, ActivityIzin::class.java))
        finish()
    }
}