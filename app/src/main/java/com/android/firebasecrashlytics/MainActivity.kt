package com.android.firebasecrashlytics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
    }

    private fun initData(): List<String> {
        val random = Random()
        val list = mutableListOf<String>()
        for (i in 1..100) {
            list.add(random.nextLong().toString())
        }
        return list
    }
}