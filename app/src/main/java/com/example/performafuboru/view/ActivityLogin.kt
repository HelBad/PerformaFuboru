package com.example.performafuboru.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.performafuboru.R
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class ActivityLogin : AppCompatActivity() {
    var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            Toast.makeText(this@ActivityLogin,
                "http://dev.fuboru.co.id:8080/bukuharian/login.php" +
                        "?username=" + userLogin.text.toString() +
                        "&password=" + passLogin.text.toString() + "&mode=android",
                Toast.LENGTH_SHORT).show()

//            getView()
            val intent = Intent(this@ActivityLogin, ActivityBeranda::class.java)
            intent.putExtra("http://dev.fuboru.co.id:8080/bukuharian/login.php" +
                    "?username=" + userLogin.text.toString() +
                    "&password=" + passLogin.text.toString() + "&mode=android", url)
            startActivity(intent)
            finish()
        }
    }

    private fun getView() {
//        btnLogin.setOnClickListener {
//            val username = userLogin.text.toString()
//            val password = passLogin.text.toString()
//            if (username != "" && password != "") {
//                val handler = Handler(Looper.getMainLooper())
//                handler.post {
//                    val field = arrayOfNulls<String>(2)
//                    field[0] = "username"
//                    field[1] = "password"
//                    val data = arrayOfNulls<String>(2)
//                    data[0] = username
//                    data[1] = password
//                    val putData = PutData("http://dev.fuboru.co.id:8080/bukuharian/login.php", "POST", field, data)
//
//                    if (putData.startPut()) {
//                        if (putData.onComplete()) {
//                            var result: JSONObject?
//                            try {
//                                result = JSONObject(putData.result)
//                                if (result.getString("status") == "true") {
//                                    Toast.makeText(applicationContext, result.getString("message"), Toast.LENGTH_SHORT).show()
//                                    val intent = Intent(this@ActivityLogin, ActivityBeranda::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                } else {
//                                    Toast.makeText(applicationContext, result.getString("message"), Toast.LENGTH_SHORT).show()
//                                }
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//                }
//            } else {
//                Toast.makeText(applicationContext, "All file required", Toast.LENGTH_SHORT).show()
//            }
//        }

        val fetchData = FetchData("http://dev.fuboru.co.id:8080/bukuharian/login.php" +
                "?username=" + userLogin.text.toString() +
                "&password=" + passLogin.text.toString() + "&mode=android")
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")

                } catch (t: Throwable) { }
            }
        }
    }
}