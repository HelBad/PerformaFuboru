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
import kotlinx.android.synthetic.main.activity_register.*

class ActivityRegister : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("pengguna")

        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah data yang dimasukkan sudah benar ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            ref.orderByChild("username").equalTo(userRegister.text.toString())
                                .addListenerForSingleValueEvent( object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        if(p0.exists()) {
                                            Toast.makeText(this@ActivityRegister,
                                                "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                                        } else {
                                            register()
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
        loginRegister.setOnClickListener {
            startActivity(Intent(this@ActivityRegister, ActivityLogin::class.java))
            finish()
        }
    }

    private fun validate(): Boolean {
        if(userRegister.text.toString() == "") {
            Toast.makeText(this, "Username kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passRegister.text.toString() == "") {
            Toast.makeText(this, "Password kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(konfirmRegister.text.toString() != passRegister.text.toString()) {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun register() {
        Toast.makeText(this, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
        val dataUser = Pengguna(userRegister.text.toString(), passRegister.text.toString(), "STAFF")
        ref.child(userRegister.text.toString()).setValue(dataUser).addOnCompleteListener {
            startActivity(Intent(this@ActivityRegister, ActivityLogin::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal tersimpan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityRegister, ActivityLogin::class.java))
        finish()
    }
}