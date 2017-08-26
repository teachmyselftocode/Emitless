package com.companyname.emitless2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gridayout.*

class GridayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gridayout)

        val arrayDistance= intent.getStringArrayListExtra("arrayDistance")
        val arrayDuration= intent.getStringArrayListExtra("arrayDuration")
        val originGlobal = intent.getStringExtra("originGlobal")
        val destinationGlobal = intent.getStringExtra("destinationGlobal")

        idGridTextView.text = "From $originGlobal to $destinationGlobal"

        //Call function loadTransportList from Main activity class
        val newList = MainActivity().loadTransportList(arrayDistance,arrayDuration)

        //Instantiate Transport Adapter from Main Activity Class
        idGridView.adapter = MainActivity.TransportAdapter(newList, this)
    }
}
