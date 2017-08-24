package com.companyname.emitless2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Checks permission once for the at the beginning only.
        checkPermission()
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//THIS CODE USES ASYNCTASK BUT FAILED TO SORT OUT THE RESPONSE
/*
    fun showToastEnd(){
        Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show()
    }
    fun showToastStart(){
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show()
    }

    inner class MyAsyncTask : AsyncTask<String,String,String>(){
        override fun onPreExecute() {
            showToastStart()
        }
        override fun doInBackground(vararg p0: String?): String { //Cannot access to UI from this block
            try {
                val inString = ArrayList<String>()
                for (i in 0 until p0.size) {
                    val url = URL(p0[i])

                    val urlConnect = url.openConnection() as HttpURLConnection
                    urlConnect.connectTimeout = 7000
                    inString.add(ConvertStreamToString(urlConnect.inputStream)) //Converts the response to string?
                    publishProgress(inString[i])
                    Log.d("APP_TEST", "" + p0[i])//means 1st index of array??
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {}
                }
            }catch (r:Exception){}

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonObject = ArrayList<String>()
                    for (i in 0 until values.size) {
                        jsonObject.add(values[i]!!)
                        Log.d("Testing", "" + values[i])
                    }
                idTextView2.text = jsonObject[1]
            } catch (e:Exception){}
        }

        override fun onPostExecute(result: String?) {
            showToastEnd()
        }
    }

    fun ConvertStreamToString(inputStream: InputStream): String{
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line =""
        var allString=""
        try{
            do{
                line=bufferReader.readLine()
                if (line!=null){
                    allString+=line
                }
            } while (line!=null)
            inputStream.close()
        } catch (e:Exception){}
        return allString
    }

    var afterParsingJSONLocal = ""
    fun parseJSONObjectSecond(responseTextLocal : String) {

        //TODO store the arrays here?
        val mainObject: JSONObject = JSONObject(responseTextLocal) //change to i when for loop is used
                .getJSONArray("rows")
                .getJSONObject(0)
                .getJSONArray("elements")
                .getJSONObject(0)

        val distance = mainObject.getJSONObject("distance").get("text").toString()
        val duration = mainObject.getJSONObject("duration").get("text").toString()

        val origin = JSONObject(responseTextLocal).get("origin_addresses").toString()
        val destination = JSONObject(responseTextLocal).get("destination_addresses").toString()


        //String builder to remove unnecessary characters for origin and destination string
        val cleanOrigin = cleanCode(origin)
        val cleanDestination = cleanCode(destination)


        //new code
        afterParsingJSONLocal = "From $cleanOrigin to $cleanDestination, the distance is $distance and the duration is $duration "
//
//        var vehicleType = ""
//        when (i) {
//            0 -> vehicleType = "driving"
//            1 -> vehicleType = "walking"
//            2 -> vehicleType = "bicycling"
//            3 -> vehicleType = "bus"
//            4 -> vehicleType = "subway"
//            5 -> vehicleType = "train"
//        }
//        afterParsingJSONLocal += "when travelling by $vehicleType"

    }

    fun buTest2 (view:View){
        //        val locationInput = idInputDestination.text.toString()
        val locationInput = "AsiaWorldExpo"
        val locationCoordinatesLong = currentLocation!!.longitude
        val locationCoordinatesLat = currentLocation!!.latitude

        val myAPIKey = "AIzaSyAcGKihEcRIKWOQ-SNEhgAOG6uL5C1bcdQ"
        val transportMode = arrayListOf("&mode=driving" ,"&mode=walking", "&mode=bicycling",
                "&mode=transit&transit_mode=bus", "mode=transit&transit_mode=subway", "mode=transit&transit_mode=train")

        var requestURLArray = ArrayList<String>()
        for (i in 0 until transportMode.size) {

            requestURLArray.add("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins= " +
                    "$locationCoordinatesLat,$locationCoordinatesLong &destinations=$locationInput ${transportMode[i]} &key= $myAPIKey")
        }
        MyAsyncTask().execute(requestURLArray[0],requestURLArray[1],requestURLArray[2]
                     ,requestURLArray[3], requestURLArray[4], requestURLArray[5])
    }
*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   //THIS CODE IS USING MAP. FROM DISCORD FRIEND STILL NOT SORTING THE RESPONSE
/*    var beforeParsingText = ArrayList<String>()
    fun buTest(view:View){
        volleyRequests2()

        var totalText =""
        for (i in 0 until responses.size){
//            beforeParsingText.add(responses[i].toString())
            totalText += responses[i] //for printing and testing the responses only
            }

//        parseJSONObject2()
        fun startIntent(){
            val intent = Intent(this, Page2::class.java)
            intent.putExtra("JSONObject", totalText)

            startActivity(intent)
        }

//        Handler().postDelayed({idTextView2.text= afterParsingJSON2[0]}, 2000)
        Handler().postDelayed({idTextView2.text= totalText}, 5000)
//        Handler().postDelayed({startIntent()}, 5000)

    }

    val responses = mutableListOf<String>()

    //NEW CODE AUG 18 DISCORD HELP
    fun volleyRequests2() {
        val locationInput = "WanChai"
        val locationCoordinatesLong = currentLocation!!.longitude
        val locationCoordinatesLat = currentLocation!!.latitude

        val myAPIUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="
        val myAPIKey = "AIzaSyAcGKihEcRIKWOQ-SNEhgAOG6uL5C1bcdQ"
        val transportMode = listOf("&mode=driving", "&mode=walking", "&mode=bicycling",
                "&mode=transit&transit_mode=bus", "mode=transit&transit_mode=subway", "mode=transit&transit_mode=train")

        val queue = Volley.newRequestQueue(this)

        transportMode.map { "$myAPIUrl$locationCoordinatesLat,$locationCoordinatesLong&destinations=$locationInput$it&key=$myAPIKey" }
                .map {
                    JsonObjectRequest(Request.Method.GET, it, null, Response.Listener {
                        responses.add(it.toString()) }, Response.ErrorListener {})
                }.forEach { queue.add(it) }
    }

    //    var afterParsingJSON : String = ""
    var afterParsingJSON2 = arrayListOf<String>()
    fun parseJSONObject2() {

        for (i in 0 until 5) {
            val mainObject: JSONObject = JSONObject(beforeParsingText[i]) //change to i when for loop is used
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)

            val distance = mainObject.getJSONObject("distance").get("text").toString()
            val duration = mainObject.getJSONObject("duration").get("text").toString()

            val origin = JSONObject(beforeParsingText[i]).get("origin_addresses").toString()
            val destination = JSONObject(beforeParsingText[i]).get("destination_addresses").toString()


            //String builder to remove unnecessary characters for origin and destination string
            val cleanOrigin = cleanCode2(origin)
            val cleanDestination = cleanCode2(destination)
            
            //new code
            afterParsingJSON2[i] = "From $cleanOrigin to $cleanDestination, the distance is $distance and the duration is $duration "
            var vehicleType = ""
            when (i) {
                0 -> vehicleType = "driving"
                1 -> vehicleType = "walking"
                2 -> vehicleType = "bicycling"
                3 -> vehicleType = "bus"
                4 -> vehicleType = "subway"
                5 -> vehicleType = "train"
            }
            afterParsingJSON2[i] += "when travelling by $vehicleType"

        }
    }


    //Function to clean code for origins and destinations
    fun cleanCode2(str: String): String {
        val stringAppender = StringBuilder()
        for (a in 0 until str.length) {
            if (str[a] != '[' && str[a] != ']' && str[a] != '"') {
                stringAppender.append(str[a])
            }
        }
        return stringAppender.toString()
    }

*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//THIS CODE IS TO SORT VOLLEY REQUESTS
    var responseText = ArrayList<String>()

    fun buGo(view:View){

        val locationInput = idInputDestination.text.toString()
        val locationCoordinatesLong = currentLocation!!.longitude
        val locationCoordinatesLat = currentLocation!!.latitude
        val myAPIUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="
        val myAPIKey = "AIzaSyAcGKihEcRIKWOQ-SNEhgAOG6uL5C1bcdQ"
        val transportMode = arrayListOf("&mode=driving", "&mode=walking", "&mode=bicycling",
                "&mode=transit&transit_mode=bus", "&mode=transit&transit_mode=subway", "&mode=transit&transit_mode=train")
        for (i in 0 until transportMode.size) {
            val requestURL = "$myAPIUrl$locationCoordinatesLat,$locationCoordinatesLong&destinations=$locationInput${transportMode[i]}&key=$myAPIKey"
            val volleyRequests = VolleyRequestsJava(this)
            try {
                volleyRequests.yourFunctionForVolleyRequest({ result -> responseText.add(result.toString()) }, requestURL)
                Thread.sleep(1000) //sleep for 3 seconds!! every requests
            } catch (e:Exception){responseText.add("ERROR HERE!")}
        }

        Handler().postDelayed({parseAll()}, 2000)


//        val spinner = idProgressBar
//        spinner.visibility = View.VISIBLE //not showing

        // Instantiate AsyncTask
//        val doTask = MyAsyncTask()
//        doTask.execute()

//        spinner.visibility = View.GONE //not showing

    }
    var totalText = ""
    fun parseAll(){
        //            val totalText = responseText2[0] + responseText2[1] + responseText2[2] + responseText2[3] + responseText2[4] + responseText2[5]
        for (i in 0 until responseText.size) {
            var vehicleType = ""
            when (i) {
                0 -> vehicleType = "driving"
                1 -> vehicleType = "walking"
                2 -> vehicleType = "bicycling"
                3 -> vehicleType = "bus"
                4 -> vehicleType = "subway"
                5 -> vehicleType = "train"
            }
            totalText += parseJSONObject(responseText[i]) + " when travelling by $vehicleType. "
        }
        idTextView2.text = totalText
    }


    fun parseJSONObject(parseString : String) : String {
        var newString = ""
        try {
            val mainObject: JSONObject = JSONObject(parseString) //change to i when for loop is used
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
            val distance = mainObject.getJSONObject("distance").get("text").toString()
            val duration = mainObject.getJSONObject("duration").get("text").toString()

            val origin = JSONObject(parseString).get("origin_addresses").toString()
            val destination = JSONObject(parseString).get("destination_addresses").toString()
            val cleanOrigin = cleanCode(origin)
            val cleanDestination = cleanCode(destination)

            newString = "From $cleanOrigin to $cleanDestination, the distance is $distance and the duration is $duration "
        }catch (e:Exception){
            return " NULL "
        }
        return  newString
    }

    fun startIntent(){
        val intent = Intent(this, Page2::class.java)
        intent.putExtra("JSONObject[]",totalText )
//        intent.putExtra("JSONObject[1]", afterParsingJSON[1])
//        intent.putExtra("JSONObject[2]", afterParsingJSON[2])
//        intent.putExtra("JSONObject[3]", afterParsingJSON[3])
//        intent.putExtra("JSONObject[4]", afterParsingJSON[4])
//        intent.putExtra("JSONObject[5]", afterParsingJSON[5])
        startActivity(intent)
    }

    //Function to clean code for origins and destinations
    fun cleanCode(str: String): String {
        val stringAppender = StringBuilder()
        for (a in 0 until str.length) {
            if (str[a] != '[' && str[a] != ']' && str[a] != '"') {
                stringAppender.append(str[a])
            }
        }
        return stringAppender.toString()
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val myRequestCode = 123
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.
                    ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                val permissionArray = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(permissionArray,myRequestCode)
                return
            }
        }
        getUserLocation()
    }

    //Mandatory code
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            myRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "User location access not granted", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun getUserLocation(){

        Toast.makeText(this, "User location access granted", Toast.LENGTH_LONG).show()


        val locationListener = MyLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 , 3f, locationListener) // for emulator
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 , 0f, locationListener) // for PHone
//        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null)     //Does this save battery? for Phone
    }


    var currentLocation: Location? = null
    inner class MyLocationListener: LocationListener{

        init {
            currentLocation = Location("Start")
            currentLocation!!.longitude = 0.0
            currentLocation!!.latitude = 0.0
        }
        //Mandatory code
        override fun onLocationChanged(p0: Location?) {
            currentLocation=p0
        }
        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            TODO("not implemented, Have to know what is happening inside!") //To change body of created functions use File | Settings | File Templates.
        }
        override fun onProviderEnabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates. 
        }
        override fun onProviderDisabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }


}
