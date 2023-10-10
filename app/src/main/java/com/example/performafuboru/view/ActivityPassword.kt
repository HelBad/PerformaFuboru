package com.example.performafuboru.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.performafuboru.R
import com.example.performafuboru.model.Pengguna
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_password.*

class ActivityPassword : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: DatabaseReference
    var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("pengguna")

        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah data yang dimasukkan sudah benar ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            ref.orderByChild("username").equalTo(userLupa.text.toString())
                                .addListenerForSingleValueEvent( object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        if(p0.exists()) {
                                            dataAkun()
                                        } else {
                                            Toast.makeText(this@ActivityPassword,
                                                "Username belum terdaftar", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onCancelled(p0: DatabaseError) { }
                                })
                        }
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        loginLupa.setOnClickListener {
            startActivity(Intent(this@ActivityPassword, ActivityLogin::class.java))
            finish()
        }
    }

    private fun dataAkun() {
        FirebaseDatabase.getInstance().getReference("pengguna")
            .orderByChild("username").equalTo(userLupa.text.toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    for (snapshot2 in datasnapshot.children) {
                        val allocation = snapshot2.getValue(Pengguna::class.java)
                        username = allocation!!.username
                        lupaPassword()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun validate(): Boolean {
        if(userLupa.text.toString() == "") {
            Toast.makeText(this, "Username kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passLupa.text.toString() == "") {
            Toast.makeText(this, "Password kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(konfirmLupa.text.toString() != passLupa.text.toString()) {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun lupaPassword() {
        Toast.makeText(this, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
        ref.child(username).child("password").setValue(passLupa.text.toString())
            .addOnCompleteListener {
                startActivity(Intent(this@ActivityPassword, ActivityLogin::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal tersimpan", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityPassword, ActivityLogin::class.java))
        finish()
    }
}