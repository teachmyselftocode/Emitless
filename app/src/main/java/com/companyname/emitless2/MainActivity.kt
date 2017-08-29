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
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.android.gms.common.api.Status
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_ticket.view.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment


class MainActivity : AppCompatActivity() {

    private var isOriginShown : Boolean = false
    var originPlaceID: String = ""
    var destinationPlaceID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Checks permission once for the at the beginning only.
        checkPermission()

        //Origin Fragment
        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                originPlaceID = place.id
//                Log.i(FragmentActivity.TAG, "Place: " + place.name)
            }

            override fun onError(status: Status) {
//                Log.i(FragmentActivity.TAG, "An error occurred: " + status)
            }
        })

        //Destination Fragment
        val autocompleteFragment2 = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment2) as PlaceAutocompleteFragment

        autocompleteFragment2.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                destinationPlaceID = place.id
//                Log.i(FragmentActivity.TAG, "Place: " + place.name)
            }

            override fun onError(status: Status) {
//                Log.i(FragmentActivity.TAG, "An error occurred: " + status)
            }
        })
    }

    //Code to toggle Origin Input
    fun buShowOriginLayout(view: View){
        if(!isOriginShown) {
            try {
                idOriginLayout.visibility = View.VISIBLE
                Thread.sleep(100)
            } catch (e: Exception) {}
            idBuOriginToggle.text = "Use Current Location"
            isOriginShown = true
        } else {
            try {
                idOriginLayout.visibility = View.GONE
                Thread.sleep(100)
            } catch (e: Exception) {}
            idBuOriginToggle.text = "Input Origin"
            isOriginShown = false
        }
    }

    fun showToastEnd(){
        Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show()
    }
    fun showToastStart(){
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show()
    }

    inner class MyAsyncTask : AsyncTask<String,String,String>(){

        private val progressBar = idProgressBar
        override fun onPreExecute() {
            showToastStart()
            progressBar.visibility = View.VISIBLE //not showing
        }
        override fun doInBackground(vararg p0: String?): String? { //Cannot access to UI from this block
            Start()
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
        }

        override fun onPostExecute(result: String?) {
            progressBar.visibility = View.GONE //not showing
            showToastEnd()
        }
    }

    private var responseText = ArrayList<String>()
    private var isOriginClear = false

    fun buGo(view: View){
        val doTask = MyAsyncTask()
        doTask.execute()
    }
    fun Start(){
        if (isOriginClear){
            responseText.clear()
            totalText= ""
        }
//        val locationDestination = idInputDestination.text.toString()
        val locationDestination = "place_id:$destinationPlaceID"
        var locationOrigin: String
//        if(idInputOrigin.text.isBlank()){
//            locationOrigin = "${currentLocation!!.latitude},${currentLocation!!.longitude}"
//        else{
//            locationOrigin = idInputOrigin.text.toString()
//        }
        if (isOriginShown){
            locationOrigin = "place_id:$originPlaceID"
        } else {
            locationOrigin = "${currentLocation!!.latitude},${currentLocation!!.longitude}"
        }
        val myAPIUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="
        val myAPIKey = "AIzaSyCjwCoN0Cqg3gK14wORXOgdee-WpigZWow"
        val transportMode = arrayListOf("&mode=driving", "&mode=walking",
                "&mode=transit&transit_mode=bus", "&mode=transit&transit_mode=subway", "&mode=transit&transit_mode=train")
        for (i in 0 until transportMode.size) {
//            val requestURL = "$myAPIUrl$locationCoordinatesLat,$locationCoordinatesLong&destinations=$locationDestination${transportMode[i]}&key=$myAPIKey"
            val requestURL = "$myAPIUrl$locationOrigin&destinations=$locationDestination${transportMode[i]}&key=$myAPIKey"

            val volleyRequests = VolleyRequestsJava(this)
            //TODO add the if(isCompleted) flag here and use while instead of for? delete try?
            try {
                volleyRequests.yourFunctionForVolleyRequest({ result -> responseText.add(result.toString()) }, requestURL)
                Thread.sleep(2000) //sleep for 3 seconds!! every requests
            } catch (e:Exception){responseText.add("ERROR HERE!")}
        }

//        Handler().postDelayed({parseAll()
//                                //Insert Base Adapter here or insert in start intent function?
//                                }, 2000)
        parseAll()

    }


    private var totalText = ""
    fun parseAll(){
        
        //            val totalText = responseText2[0] + responseText2[1] + responseText2[2] + responseText2[3] + responseText2[4] + responseText2[5]
        for (i in 0 until responseText.size) {
            var vehicleType = ""
            when (i) {
                0 -> vehicleType = "car"
                1 -> vehicleType = "walking"
                2 -> vehicleType = "bus"
                3 -> vehicleType = "subway"
                4 -> vehicleType = "train"
            }
            totalText += parseJSONObject(responseText[i]) + " when travelling by $vehicleType. "

        }

//        var newText = " "
//        listOfDistance.forEach { newText+it }
//        listOfDuration.forEach { newText+it }
        listOfDistance.forEach { totalText+= "  " + it }
        listOfDuration.forEach { totalText+= "  " + it }

//        idTextView2.text = totalText
//        Handler().postDelayed({
            startIntent()
            isOriginClear = true
            totalText=""
            listOfDistance.clear()
        listOfDuration.clear()
//            listOfDuration.clear()}, 2000)
    }

    private val listOfDistance = ArrayList<String>()
    private val listOfDuration = ArrayList<String>()
    private var originGlobal : String = ""
    private var destinationGlobal : String = ""
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
            listOfDistance.add(cleanCodeDistance(distance))
            listOfDuration.add(duration)

            val origin = JSONObject(parseString).get("origin_addresses").toString()
            val destination = JSONObject(parseString).get("destination_addresses").toString()
            val cleanOrigin = cleanCode(origin)
            val cleanDestination = cleanCode(destination)
            originGlobal = cleanOrigin
            destinationGlobal = cleanDestination


            newString = "From $cleanOrigin to $cleanDestination, the distance is $distance and the duration is $duration "
        }catch (e:Exception){
            return " NULL "
        }
        return  newString
    }

    fun startIntent(){
        val intent = Intent(this, Page2::class.java)
        intent.putExtra("totalText",totalText )
        intent.putExtra("listOfDistance",listOfDistance )
        intent.putExtra("listOfDuration",listOfDuration )
        intent.putExtra("originGlobal",originGlobal )
        intent.putExtra("destinationGlobal",destinationGlobal )

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

    fun cleanCodeDistance(str: String): String {
        val stringAppender = StringBuilder()
        for (a in 0 until str.length) {
            if (str[a] != 'k' && str[a] != 'm') {
                stringAppender.append(str[a])
            }
        }
        return stringAppender.toString().replace(" ","")
    }


    fun loadTransportList(listOfDistance : ArrayList<String>, listOfDuration : ArrayList<String>) : ArrayList<TransportModes>{

        val listOfTransport = ArrayList<TransportModes>()
        listOfTransport.add(TransportModes("Car",R.drawable.car,"Distance : ${listOfDistance[0]}km","Duration : " +listOfDuration[0],"CO2: ${listOfDistance[0].toDouble() *0.3416} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Electric Car",R.drawable.ecar,"Distance : ${listOfDistance[0]}km","Duration : " +listOfDuration[0],"CO2: ${listOfDistance[0].toDouble() *0.2425} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Motor Scooter",R.drawable.motor,"Distance : ${listOfDistance[0]}km","Duration : " +listOfDuration[0],"CO2: ${listOfDistance[0].toDouble() *0.1473} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Electric Scooter",R.drawable.escooter,"Distance : ${listOfDistance[0]}km","Duration : " +listOfDuration[0],"CO2: ${listOfDistance[0].toDouble() *0.05515} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Walking",R.drawable.walking,"Distance : ${listOfDistance[1]}km","Duration : " +listOfDuration[1],"CO2: ${listOfDistance[1].toDouble() *0} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Coach",R.drawable.coach,"Distance : ${listOfDistance[2]}km","Duration : " +listOfDuration[2],"CCO2: ${listOfDistance[2].toDouble() *0.05516} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Bus",R.drawable.bus,"Distance : ${listOfDistance[2]}km","Duration : " +listOfDuration[2],"CO2: ${listOfDistance[2].toDouble() *0.1093} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Subway",R.drawable.subway,"Distance : ${listOfDistance[3]}km","Duration : " +listOfDuration[3],"CO2: ${listOfDistance[3].toDouble() *0.07691} kg CO2-Eq"))
        listOfTransport.add(TransportModes("Train",R.drawable.train,"Distance : ${listOfDistance[4]}km","Duration : " +listOfDuration[4],"CO2: ${listOfDistance[4].toDouble() *0.07464} kg CO2-Eq"))
        return listOfTransport


    }


     class TransportAdapter : BaseAdapter{
        private var listOfTransportLocal = ArrayList<TransportModes>() // local array. Why set it to another local?
        private var context : Context? = null

        constructor(listOfAnimals : ArrayList<TransportModes>, mContext : Context):super(){
            this.listOfTransportLocal= listOfAnimals //set the data to a local array
            this.context = mContext
        }



        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val transport = listOfTransportLocal[p0]
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val transportView = inflater.inflate(R.layout.activity_ticket, null)
            transportView.ticketTransportType.text = transport.transportType
            transportView.ticketImage.setImageResource(transport.image)
            transportView.ticketDistance.text = transport.distance
            transportView.ticketDuration.text = transport.duration
            transportView.ticketCarbon.text = transport.carbonEmission
            return transportView

        }

        override fun getItem(p0: Int): Any {
            return listOfTransportLocal[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listOfTransportLocal.size
        }
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

        }
        override fun onProviderEnabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates. 
        }
        override fun onProviderDisabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }


}

/*
OLD CODES. Not Working as desired

//THIS CODE USES ASYNCTASK BUT FAILED TO SORT OUT THE RESPONSE

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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//THIS CODE IS USING MAP. FROM DISCORD FRIEND STILL NOT SORTING THE RESPONSE

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


*/