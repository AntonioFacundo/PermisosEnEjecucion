package iplace.net.transmetrodc

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.e("Mapa", "mapa listo")
        updateMyLocation()
    }

    /**Metodo para agregar Marcadores en el mapa*/
    private fun addMarker(latitud: Double, longitud: Double){

        val coordinates = LatLng(latitud, longitud)
        val myLocation = CameraUpdateFactory.newLatLngZoom(coordinates, 15f)

        if(marker != null) marker.remove()
        marker = mMap.addMarker(MarkerOptions()
                .position(coordinates)
                .title("¡Yo!")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)))

        mMap.animateCamera(myLocation)
    }

    private fun updateLocation(location: Location){

        latitude = location.latitude
        longitude = location.longitude
        addMarker(latitude,longitude)
    }

    override fun onLocationChanged(location: Location) {
       updateLocation(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun updateMyLocation(){
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        Log.e("Antes de los permisos", "sin permisos")
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Sin permisos", Toast.LENGTH_SHORT).show()
            return
        }
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        updateLocation(location)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0f,this)

        Log.e("Con permisos", "con permisos")
    }
}