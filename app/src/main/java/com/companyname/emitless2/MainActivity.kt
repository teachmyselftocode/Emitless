package com.companyname.emitless2

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
import android.view.View
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import com.android.volley.RequestQueue
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.BasicNetwork
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

    fun showToasT(){
        Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show()
    }
    inner class MyAsyncTask : AsyncTask<String,String,String>(){
        override fun onPreExecute() {
        }
        override fun doInBackground(vararg p0: String?): String { //Cannot access to UI from this block
            try {
                for (i in 0 until p0.size) {
                    val url = URL(p0[i]) //means 1st index of array??
                    val urlConnect = url.openConnection() as HttpURLConnection
                    urlConnect.connectTimeout = 7000

                    val inString = ArrayList<String>()
                    inString.add(ConvertStreamToString(urlConnect.inputStream)) //Converts the response to string?
                    publishProgress(inString[i])
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {}
                }
            }catch (e:Exception){}
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                val jsonObject = ArrayList<String>()
                for (i in 0 until values.size){

                    jsonObject.add(values[i]!!)
                }
                idTextView2.text =  jsonObject[0]
            }catch (e:Exception){}
        }
        override fun onPostExecute(result: String?) {
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
        val locationInput = "WanChai"
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
    fun loadUrls(){
        val locationInput = "WanChai"
        val locationCoordinatesLong = 113.947
        val locationCoordinatesLat = 22.2913
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


    fun buTest(view:View){
        volleyRequests()
        Handler().postDelayed({idTextView2.text=responseText[1]}, 2000)


    }

    fun buGo(view:View){
        idTextView2.text = "The longitude is ${currentLocation!!.longitude.toString()} and the latitude is ${currentLocation!!.latitude.toString()}"

//        val spinner = idProgressBar
//        spinner.visibility = View.VISIBLE //not showing

        // Instantiate AsyncTask
//        val doTask = MyAsyncTask()
//        doTask.execute()

        volleyRequests()

        //Delays the Parsing for 2 second.
        Handler().postDelayed({parseJSONObject()}, 2000)

        //Delays the intent to Page2 for 2 second.
        Handler().postDelayed({startIntent()}, 2000) //TODO Use a request accept method not delay with time.


//        spinner.visibility = View.GONE //not showing

    }


    var responseText = ArrayList<String>() // stores the response 6 times, i.e. 6 different url response
    val jsonObjectRequestArray = ArrayList<JsonObjectRequest>()
    fun volleyRequests(){

//        val locationInput = idInputDestination.text.toString()
        val locationInput = "WanChai"
        val locationCoordinatesLong = currentLocation!!.longitude
        val locationCoordinatesLat = currentLocation!!.latitude

        //Using Volley to send request
        //TODO Singleton? or leave it like this?
        val cache : Cache= DiskBasedCache(cacheDir, 1024 * 1024)
        val network : Network= BasicNetwork(HurlStack())
        val mRequestQueue : RequestQueue= RequestQueue(cache, network)
        mRequestQueue.start()

        val myAPIKey = "AIzaSyAcGKihEcRIKWOQ-SNEhgAOG6uL5C1bcdQ"
        val transportMode = arrayListOf("&mode=driving" ,"&mode=walking", "&mode=bicycling",
                "&mode=transit&transit_mode=bus", "mode=transit&transit_mode=subway", "mode=transit&transit_mode=train")


        //For loop for sending requests 6 times, sending 6 different urls
        //TODO why does not work??
        for(i in 0 until transportMode.size) {

            val requestURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins= " +
                    "$locationCoordinatesLat,$locationCoordinatesLong &destinations=$locationInput ${transportMode[i]} &key= $myAPIKey"


            jsonObjectRequestArray.add( JsonObjectRequest(Request.Method.GET, requestURL,
                    null, Response.Listener<JSONObject> { response ->
                responseText.add(response.toString())
            },
                    Response.ErrorListener { responseText.add("That didn't work!") }))

            //TODO add next queue!?? Ans: No need seems working
        }
        mRequestQueue.add(jsonObjectRequestArray[0])
        mRequestQueue.add(jsonObjectRequestArray[1])
        mRequestQueue.add(jsonObjectRequestArray[2])
        mRequestQueue.add(jsonObjectRequestArray[3])
        mRequestQueue.add(jsonObjectRequestArray[4])
        mRequestQueue.add(jsonObjectRequestArray[5])

    }


//    var afterParsingJSON : String = ""
    var afterParsingJSON = arrayListOf<String>()
    fun parseJSONObject() {

    //TODO store the arrays here
        for (i in 0 until 5) {
            val mainObject: JSONObject = JSONObject(responseText[i]) //change to i when for loop is used
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)

            val distance = mainObject.getJSONObject("distance").get("text").toString()
            val duration = mainObject.getJSONObject("duration").get("text").toString()

            val origin = JSONObject(responseText[i]).get("origin_addresses").toString()
            val destination = JSONObject(responseText[i]).get("destination_addresses").toString()


            //String builder to remove unnecessary characters for origin and destination string
            val cleanOrigin = cleanCode(origin)
            val cleanDestination = cleanCode(destination)


            //new code
            afterParsingJSON[i] = "From $cleanOrigin to $cleanDestination, the distance is $distance and the duration is $duration "
            var vehicleType = ""
            when (i) {
                0 -> vehicleType = "driving"
                1 -> vehicleType = "walking"
                2 -> vehicleType = "bicycling"
                3 -> vehicleType = "bus"
                4 -> vehicleType = "subway"
                5 -> vehicleType = "train"
            }
            afterParsingJSON[i] += "when travelling by $vehicleType"
        }

    }

    fun startIntent(){
        val intent = Intent(this, Page2::class.java)
        intent.putExtra("JSONObject[]", afterParsingJSON[0])
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
