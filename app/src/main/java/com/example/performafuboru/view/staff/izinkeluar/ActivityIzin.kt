package com.example.performafuboru.view.staff.izinkeluar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performafuboru.R
import com.example.performafuboru.adapter.AdapterIzin
import com.example.performafuboru.api.ApiStaff
import com.example.performafuboru.model.Izin
import com.example.performafuboru.view.staff.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin.*
import org.json.JSONObject

class ActivityIzin : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var adapter: AdapterIzin
    lateinit var dataArrayList: ArrayList<Izin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin)

        (this as AppCompatActivity).setSupportActionBar(toolbarIzin)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loadIzin()

        adapter = AdapterIzin(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerIzin.layoutManager = layoutManager
        recyclerIzin.adapter = adapter

        backIzin.setOnClickListener {
            startActivity(Intent(this@ActivityIzin, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadIzin() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiStaff.IZIN +
                "?create_by=" + SP.getString("username", "").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Izin(
                            dataArray.getJSONObject(i).getString("kode_izin"),
                            dataArray.getJSONObject(i).getString("nama"),
                            dataArray.getJSONObject(i).getString("waktu_izin"),
                            dataArray.getJSONObject(i).getString("waktu_kembali"),
                            dataArray.getJSONObject(i).getString("kembali"),
                            dataArray.getJSONObject(i).getString("flag"),
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_tambah, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.tambah) {
            startActivity(Intent(this@ActivityIzin, ActivityIzinAdd::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzin, ActivityBeranda::class.java))
        finish()
    }
}