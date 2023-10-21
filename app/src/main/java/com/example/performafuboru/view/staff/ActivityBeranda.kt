package com.example.performafuboru.view.staff

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.performafuboru.R
import com.example.performafuboru.api.ApiStaff
import com.example.performafuboru.view.ActivityLogin
import com.example.performafuboru.view.staff.bukuharian.ActivityBukuharian
import com.example.performafuboru.view.staff.izinkeluar.ActivityIzin
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_beranda.harianBeranda
import kotlinx.android.synthetic.main.staff_activity_beranda.idBeranda
import kotlinx.android.synthetic.main.staff_activity_beranda.izinBeranda
import kotlinx.android.synthetic.main.staff_activity_beranda.logoutBeranda
import kotlinx.android.synthetic.main.staff_activity_beranda.namaBeranda
import org.json.JSONObject

class ActivityBeranda : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    val PERM_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_beranda)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        checkAndRequestPermissions()
        getData()

        harianBeranda.setOnClickListener {
            val intent = Intent(this@ActivityBeranda, ActivityBukuharian::class.java)
            intent.putExtra("title", "Buku Harian")
            startActivity(intent)
            finish()
        }
        izinBeranda.setOnClickListener {
            val intent = Intent(this@ActivityBeranda, ActivityIzin::class.java)
            intent.putExtra("title", "Izin Keluar Kantor")
            startActivity(intent)
            finish()
        }
        logoutBeranda.setOnClickListener {
            alertDialog.setTitle("Keluar Akun")
            alertDialog.setMessage("Apakah anda ingin keluar dari akun ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        val editor = SP.edit()
                        editor.putString("username", "")
                        editor.putString("password", "")
                        editor.putString("level", "")
                        editor.apply()

                        startActivity(Intent(this@ActivityBeranda, ActivityLogin::class.java))
                        finish()
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
        val fetchData = FetchData(ApiStaff.PENGGUNA +
                "?kode=" + SP.getString("username", "").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaBeranda.text = dataObject.getString("nama")
                    idBeranda.text = SP.getString("username", "").toString()
                } catch (t: Throwable) { }
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(listPermissionsNeeded.toTypedArray(), PERM_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERM_CODE) {
            if (grantResults.isNotEmpty()) {
                for (i in permissions.indices) {
                    if (permissions[i] == Manifest.permission.CAMERA) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda ingin keluar aplikasi ?").setCancelable(false)
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}