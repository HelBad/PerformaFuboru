package com.example.performafuboru.api

class ApiAdmin {
    companion object {
        private val SERVER = "http://10.0.2.2/fuboru_performa/admin/"
        val IZIN = SERVER + "izin.php"
        val IZIN_APPROVAL_1 = SERVER + "izin_approval_1.php"
        val IZIN_DETAIL = SERVER + "izin_detail.php"
        val PENGGUNA = SERVER + "pengguna.php"
    }
}