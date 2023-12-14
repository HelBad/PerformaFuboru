package com.example.performafuboru.view.staff.izinkeluar

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.performafuboru.R
import com.example.performafuboru.api.ApiStaff
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin_absensi.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityIzinAbsensi : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    lateinit var bitmap: Bitmap
    lateinit var encodedimage: String
    var formatDate1 = SimpleDateFormat("YYYY-MM-dd")
    var formatDate2 = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    var formatTime = SimpleDateFormat("HH:mm")
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin_absensi)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        kode = arrayListOf("", "", "", "")
        getLoc()
        getData()

        refreshAbsensi.setOnRefreshListener {
            Handler().postDelayed({
                refreshAbsensi.isRefreshing = false
                this.recreate()
            }, 1000)
        }

        backAbsensi.setOnClickListener {
            startActivity(Intent(this@ActivityIzinAbsensi, ActivityIzin::class.java))
            finish()
        }
        btnMasuk.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin clock in ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        addCheckin()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnKeluar.setOnClickListener {
            if(ketAbsensi.text.toString() != "") {
                Dexter.withContext(applicationContext).withPermission(Manifest.permission.CAMERA)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, 111)
                        }
                        override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {}
                        override fun onPermissionRationaleShouldBeShown(
                            p0: com.karumi.dexter.listener.PermissionRequest?, permissionToken: PermissionToken?) {
                            permissionToken!!.continuePermissionRequest()
                        }
                    }).check()
            } else {
                Toast.makeText(this@ActivityIzinAbsensi, "Lengkapi data dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLoc() {
        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        getLocations()
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            if(it == null) {
                Toast.makeText(this, "Lokasi gagal ditampilkan", Toast.LENGTH_SHORT).show()
            } else it.apply {
                val geocoder = Geocoder(this@ActivityIzinAbsensi, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
                kode[0] = addresses[0].latitude.toFloat().toString()
                kode[1] = addresses[0].longitude.toFloat().toString()
                kode[2] = addresses[0].getAddressLine(0).toString()
                lokasiAbsensi.text = kode[2]
            }
        }
    }

    private fun getData() {
        val fetchData1 = FetchData(ApiStaff.PENGGUNA +
                "?kode=" + SP.getString("username", "").toString())
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result: String = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaAbsensi.text = dataObject.getString("nama")
                    idAbsensi.text = SP.getString("username", "").toString()

                    tglAbsensi.text = formatDate1.format(Date()).toString()
                    waktuAbsensi.text = formatTime.format(Date()).toString()
                } catch (t: Throwable) { }
            }
        }

        val fetchData2 = FetchData(
            ApiStaff.IZIN_DETAIL +
                "?kode_izin=" + intent.getStringExtra("kode").toString())
        if (fetchData2.startFetch()) {
            if (fetchData2.onComplete()) {
                val result: String = fetchData2.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    ketAbsensi.setText(dataObject.getString("catatan"))
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            bitmap = data!!.extras!!["data"] as Bitmap
            encodebitmap(bitmap)
            if(intent.getStringExtra("kembali").toString() == "0") {
                kode[3] = "C"
            } else {
                if(intent.getStringExtra("flag").toString() == "H") {
                    kode[3] = "T3"
                } else {
                    kode[3] = "C"
                }
            }
            uploadFoto()

            startActivity(Intent(this@ActivityIzinAbsensi, ActivityIzin::class.java))
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun encodebitmap(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteofimages = byteArrayOutputStream.toByteArray()
        encodedimage = Base64.encodeToString(byteofimages, Base64.DEFAULT)
    }

    private fun uploadFoto() {
        val request: StringRequest = object : StringRequest(Method.POST, ApiStaff.IZIN_CLOCKOUT, Response.Listener {
            Toast.makeText(applicationContext, "Berhasil diupload", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error -> Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val map: HashMap<String, String> = HashMap()
                map["kode_izin"] = intent.getStringExtra("kode").toString()
                map["clockout"] = formatDate2.format(Date()).toString()
                map["gps_out"] = kode[0] + "," + kode[1]
                map["catatan"] = ketAbsensi.text.toString()
                map["foto"] = encodedimage
                map["status"] = kode[3]
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
    }

    private fun addCheckin(){
        AndroidNetworking.post(ApiStaff.IZIN_CLOCKIN)
            .addBodyParameter("kode_izin", intent.getStringExtra("kode").toString())
            .addBodyParameter("clockin", formatDate2.format(Date()).toString())
            .addBodyParameter("gps_in", kode[0] + "," + kode[1])
            .addBodyParameter("catatan", ketAbsensi.text.toString())
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityIzinAbsensi, ActivityIzin::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzinAbsensi, ActivityIzin::class.java))
        finish()
    }
}