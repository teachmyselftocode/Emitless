package com.companyname.emitless2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_page2.*
import android.view.View
import org.json.JSONObject



class Page2 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page2)

        val message0 = intent.getStringExtra("totalText")



        // Capture the layout's TextView and set the string as its text
//        idPrintText.text = message0 + message1 + message2 + message3 + message4 + message5
        idPrintText.text = message0

    }


    fun buTest3(view: View){
        val arrayDistance= intent.getStringArrayListExtra("listOfDistance")
        val arrayDuration= intent.getStringArrayListExtra("listOfDuration")
//        (arrayDistance,arrayDuration)
        val mainActivity = MainActivity()
        mainActivity.loadTransportList(arrayDistance,arrayDuration)
    }

//    }



}
