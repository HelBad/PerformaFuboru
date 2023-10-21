package com.example.performafuboru.api

class ApiSecurity {
    companion object {
        private val SERVER = "http://10.0.2.2/fuboru_performa/security/"
        val IZIN = SERVER + "izin.php"
        val IZIN_APPROVAL_2 = SERVER + "izin_approval_2.php"
        val IZIN_APPROVAL_3 = SERVER + "izin_approval_3.php"
        val IZIN_DETAIL = SERVER + "izin_detail.php"
        val PENGGUNA = SERVER + "pengguna.php"
    }
}