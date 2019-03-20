package com.example.pshmakov.blacksurfaceviewsample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Transition

import com.example.pshmakov.blacksurfaceviewsample2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this@MainActivity, TargetActivity::class.java)
        overridePendingTransition(0, 0)
        startActivity(intent, null)
        overridePendingTransition(0, 0)
    }
}
