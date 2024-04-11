package com.ko2ic.sample.model

import javax.inject.Inject

class Analytics @Inject constructor() {

    fun sendViewableLog(index: Int) {
        // TODO
        println("ログ送信した：$index")
    }
}