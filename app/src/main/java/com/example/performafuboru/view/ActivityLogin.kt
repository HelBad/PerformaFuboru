package com.example.performafuboru.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.performafuboru.R
import com.example.performafuboru.model.Pengguna
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: DatabaseReference
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("pengguna")
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)

        btnLogin.setOnClickListener {
            Toast.makeText(this@ActivityLogin, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
            if(validate()) {
                login()
            }
        }
        daftarLogin.setOnClickListener {
            startActivity(Intent(this@ActivityLogin, ActivityRegister::class.java))
            finish()
        }
        lupaLogin.setOnClickListener {
            startActivity(Intent(this@ActivityLogin, ActivityPassword::class.java))
            finish()
        }
    }

    private fun validate(): Boolean {
        if(userLogin.text.toString() == "") {
            Toast.makeText(this, "Username kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passLogin.text.toString() == "") {
            Toast.makeText(this, "Password kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun login() {
        ref.orderByChild("username").equalTo(userLogin.text.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (h in p0.children) {
                            val us = h.getValue(Pengguna::class.java)
                            if(us!!.password == passLogin.text.toString()) {
                                val editor = SP.edit()
                                editor.putString("username", us.username)
                                editor.putString("password", us.password)
                                editor.putString("level", us.level)
                                editor.apply()

                                if(SP.getString("level", "") == "HRGA") {
                                    startActivity(Intent(this@ActivityLogin,
                                        com.example.performafuboru.view.admin.ActivityBeranda::class.java))
                                    finish()
                                } else if(SP.getString("level", "") == "SECURITY") {
                                    startActivity(Intent(this@ActivityLogin,
                                        com.example.performafuboru.view.security.ActivityBeranda::class.java))
                                    finish()
                                } else {
                                    startActivity(Intent(this@ActivityLogin,
                                        com.example.performafuboru.view.staff.ActivityBeranda::class.java))
                                    finish()
                                }
                            } else {
                                Toast.makeText(this@ActivityLogin, "Password salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@ActivityLogin, "Username belum terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }
            })
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