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

        val message0 = intent.getStringExtra("JSONObject[0]")
//        val message1 = intent.getStringExtra("JSONObject[1]")
//        val message2 = intent.getStringExtra("JSONObject[2]")
//        val message3 = intent.getStringExtra("JSONObject[3]")
//        val message4 = intent.getStringExtra("JSONObject[4]")
//        val message5 = intent.getStringExtra("JSONObject[5]")
        // Capture the layout's TextView and set the string as its text
//        idPrintText.text = message0 + message1 + message2 + message3 + message4 + message5
        idPrintText.text = message0
    }


    fun buReturn(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
//        finish()

    }

    fun buTest3(view: View) {
//        parseJSONObject()
        idPrintText.text = ""
//        idPrintText2.text = afterParsingJSON
    }




//    //this is using klaxon (deprecated by ado)
//    fun parseJSONObject(){
//
//        val message =intent.getStringExtra("JSONObject")
//        val parser : Parser = Parser()
//        val stringBuilder : StringBuilder = StringBuilder(message)
//        val obj : JsonArray<JsonObject> = parser.parse(stringBuilder) as JsonArray<JsonObject>
//        origin = obj.int("distance")
//    }
//    fun parse(name:String) : Any?{
//        val cls = Parser::class.java
//        return cls.getResourceAsStream(name)?.let{inputStream -> return Parser().parse(inputStream) }
//    }

}
