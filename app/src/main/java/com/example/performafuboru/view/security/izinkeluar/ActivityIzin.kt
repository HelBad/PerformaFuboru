package com.example.performafuboru.view.security.izinkeluar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performafuboru.R
import com.example.performafuboru.adapter.AdapterIzin
import com.example.performafuboru.api.ApiSecurity
import com.example.performafuboru.model.Izin
import com.example.performafuboru.view.security.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.security_activity_izin.*
import org.json.JSONObject

class ActivityIzin : AppCompatActivity() {
    lateinit var adapter: AdapterIzin
    lateinit var dataArrayList: ArrayList<Izin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.security_activity_izin)

        (this as AppCompatActivity).setSupportActionBar(toolbarIzin2)
        loadNama()

        adapter = AdapterIzin(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerIzin.layoutManager = layoutManager
        recyclerIzin.adapter = adapter

        backIzin1.setOnClickListener {
            startActivity(Intent(this@ActivityIzin, ActivityBeranda::class.java))
            finish()
        }
        backIzin2.setOnClickListener {
            startActivity(Intent(this@ActivityIzin, ActivityBeranda::class.java))
            finish()
        }

        lanjutIzin.setOnClickListener {
            if(namaIzin.text.toString() == "") {
                toolbarIzin2.visibility = View.VISIBLE
                toolbarIzin1.visibility = View.GONE
                Toast.makeText(this@ActivityIzin, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaIzin.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarIzin2.visibility = View.VISIBLE
                toolbarIzin1.visibility = View.GONE
                Toast.makeText(this@ActivityIzin, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityIzin, ActivityIzin::class.java)
                intent.putExtra("nama", namaIzin.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalIzin.setOnClickListener {
            namaIzin.setText("")
        }
    }

    private fun loadNama() {
        namaIzin.setText(intent.getStringExtra("nama").toString())
        if(namaIzin.text.toString() == "null") {
            namaIzin.setText("")
            loadIzin()
        } else {
            namaIzin.setText(intent.getStringExtra("nama").toString())
            searchIzin()
        }
    }

    private fun loadIzin() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiSecurity.IZIN)
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
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchIzin() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(
            ApiSecurity.IZIN +
                "?nama=" + intent.getStringExtra("nama").toString())
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
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_cari, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.cari) {
            toolbarIzin2.visibility = View.GONE
            toolbarIzin1.visibility = View.VISIBLE
            loadNama()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzin, ActivityBeranda::class.java))
        finish()
    }
}