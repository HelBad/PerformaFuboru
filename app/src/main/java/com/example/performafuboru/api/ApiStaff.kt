package com.example.performafuboru.api

class ApiStaff {
    companion object {
        private val SERVER = "http://10.0.2.2/fuboru_performa/staff/"
        val IZIN = SERVER + "izin.php"
        val IZIN_ADD = SERVER + "izin_add.php"
        val IZIN_CLOCKIN = SERVER + "izin_clockin.php"
        val IZIN_CLOCKOUT = SERVER + "izin_clockout.php"
        val IZIN_COUNT = SERVER + "izin_count.php"
        val IZIN_DETAIL = SERVER + "izin_detail.php"
        val PENGGUNA = SERVER + "pengguna.php"
    }
}