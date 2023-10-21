package com.example.performafuboru.model

class Pengguna {
    lateinit var username: String
    lateinit var password: String
    lateinit var level: String

    constructor() {}
    constructor(username: String, password: String, level: String) {
        this.username = username
        this.password = password
        this.level = level
    }
}