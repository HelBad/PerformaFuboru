package com.example.performafuboru.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.performafuboru.R
import com.example.performafuboru.model.Izin

class AdapterIzin(dataList: ArrayList<Izin>?): RecyclerView.Adapter<AdapterIzin.IzinViewHolder>() {
    private val dataList: ArrayList<Izin>?
    lateinit var SP: SharedPreferences

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IzinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_izin, parent, false)
        return IzinViewHolder(view)
    }

    override fun onBindViewHolder(holder: IzinViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaIzin.text = dataList!![position].nama
        holder.waktuIzin.text = "DARI : " + dataList[position].waktu_izin
        holder.selesaiIzin.text = "SAMPAI : " + dataList[position].waktu_kembali
        when (dataList[position].status) {
            "A" -> { holder.statusIzin.text = "STATUS : DISETUJUI" }
            "C" -> { holder.statusIzin.text = "STATUS : SELESAI" }
            else -> { holder.statusIzin.text = "STATUS : MENUNGGU" }
        }

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "HRGA") {
                if(dataList[position].status == "T1") {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.admin.izinkeluar.ActivityIzinApproval::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                } else if(dataList[position].status == "C") {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.admin.izinkeluar.ActivityIzinDetail::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                }
            } else if(SP.getString("level", "").toString() == "SECURITY") {
                if(dataList[position].status == "T2") {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.security.izinkeluar.ActivityIzinApproval::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                } else if(dataList[position].status == "T3") {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.security.izinkeluar.ActivityIzinDetail::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                }
            } else {
                if(dataList[position].status == "T1" || dataList[position].status == "T2") {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.staff.izinkeluar.ActivityIzinApproval::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                } else if(dataList[position].status == "A") {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.staff.izinkeluar.ActivityIzinAbsensi::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                } else {
                    val intent = Intent(v.context,
                        com.example.performafuboru.view.staff.izinkeluar.ActivityIzinDetail::class.java)
                    intent.putExtra("kode", dataList[position].kode_izin)
                    v.context.startActivity(intent)
                    (v.context as AppCompatActivity).finish()
                }
            }

//            if(dataList[position].status == "T1" || dataList[position].status == "T2") {
//                val intent = Intent(v.context, ActivityIzinApproval::class.java)
////                intent.putExtra("kode", dataList[position].kode)
////                intent.putExtra("approval", dataList[position].approval)
//                v.context.startActivity(intent)
//                (v.context as AppCompatActivity).finish()
//            } else if(dataList[position].status == "A") {
//                val intent = Intent(v.context, ActivityIzinAbsensi::class.java)
//                intent.putExtra("kode", dataList[position].kode_izin)
//                v.context.startActivity(intent)
//                (v.context as AppCompatActivity).finish()
//            } else {
//                val intent = Intent(v.context, ActivityIzinDetail::class.java)
////                intent.putExtra("kode", dataList[position].kode)
////                intent.putExtra("approval", dataList[position].approval)
//                v.context.startActivity(intent)
//                (v.context as AppCompatActivity).finish()
//            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class IzinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaIzin: TextView
        val waktuIzin: TextView
        val selesaiIzin: TextView
        val statusIzin: TextView
        val viewHolder: View

        init {
            namaIzin = itemView.findViewById<View>(R.id.namaIzin) as TextView
            waktuIzin = itemView.findViewById<View>(R.id.waktuIzin) as TextView
            selesaiIzin = itemView.findViewById<View>(R.id.selesaiIzin) as TextView
            statusIzin = itemView.findViewById<View>(R.id.statusIzin) as TextView
            viewHolder = itemView
        }
    }
}