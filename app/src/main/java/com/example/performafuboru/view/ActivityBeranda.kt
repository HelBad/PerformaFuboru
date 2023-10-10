package com.example.performafuboru.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.performafuboru.R
import com.example.performafuboru.view.bukuharian.ActivityBukuharian
import kotlinx.android.synthetic.main.activity_beranda.*

class ActivityBeranda : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)

        harianBeranda.setOnClickListener {
            val intent = Intent(this@ActivityBeranda, ActivityBukuharian::class.java)
            intent.putExtra("title", "Buku Harian")
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