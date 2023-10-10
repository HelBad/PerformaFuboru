package com.example.performafuboru.model

class Pengguna {
    lateinit var username: String
    lateinit var password: String

    constructor() {}
    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}