package com.companyname.emitless2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_page2.*
import android.view.View
import com.companyname.emitless2.MainActivity.TransportAdapter
import kotlinx.android.synthetic.main.activity_gridayout.*
import org.json.JSONObject



class Page2 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page2)

        val totalText = intent.getStringExtra("totalText")
        idPrintText.text = totalText

    }


    fun buTest3(view: View){
        val arrayDistance= intent.getStringArrayListExtra("listOfDistance")
        val arrayDuration= intent.getStringArrayListExtra("listOfDuration")
        val originGlobal = intent.getStringExtra("originGlobal")
        val destinationGlobal = intent.getStringExtra("destinationGlobal")



        val intent = Intent(this, GridayoutActivity::class.java)
        intent.putExtra("arrayDistance",arrayDistance )
        intent.putExtra("arrayDuration",arrayDuration )
        intent.putExtra("originGlobal",originGlobal )
        intent.putExtra("destinationGlobal",destinationGlobal )
        startActivity(intent)
        }

        //TODO change to another class

    }

//    }




