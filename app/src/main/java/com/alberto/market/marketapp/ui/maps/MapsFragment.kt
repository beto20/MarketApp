package com.alberto.market.marketapp.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentMapsBinding
import com.alberto.market.marketapp.util.toast
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    val coarsePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> map.isMyLocationEnabled = true
                else -> requireContext().toast("Denied")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapsBinding.bind(view)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        createMarker()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()
    }


    private fun createMarker() {
        val place = LatLng(-12.08, -77.05)
        val market = MarkerOptions().position(place).title("Mock")

        map.addMarker(market)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 18f), 1000, null)

    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::map.isInitialized) return

        if (isLocationPermissionGranted()) {
            // Tiene el permiso de ACCESS_COARSE_LOCATION
            map.isMyLocationEnabled = true
            onMyLocation()
        } else {
            coarsePermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        }

    }

    override fun onMyLocationClick(location: Location) {
        requireContext().toast("${location.latitude} - ${location.longitude}")
    }

    override fun onMyLocationButtonClick(): Boolean {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showSettingAlert().show()
            return true
        }

        return false
    }


    private fun showSettingAlert(): AlertDialog {

        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle("Configuracion GPS")

        alertDialog.setMessage("GPS esta deshabilitado. ¿Desea habilitar esta configuracion?")

        alertDialog.setPositiveButton("Configuracion") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        alertDialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        return alertDialog.create()
    }

    @SuppressLint("MissingPermission", "UseRequireInsteadOfGet")
    private fun onMyLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        try{
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if(it != null){
                    printLocation(it)
                }else{
                    requireContext().toast("No se pudo obtener la ubicacion")
                }
            }

            val locationRequest = LocationRequest.create().apply{
                interval = 1000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback(){
                override fun onLocationResult(locationResult : LocationResult) {
                    locationResult ?: return
                    println("Se recibio una actualizacion")
                    for(location in locationResult.locations){
                        printLocation(location)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())

        }catch (ex:Exception){

        }

    }

    private fun printLocation(location: Location) {
        println("lat ${location?.latitude} - lon ${location?.longitude}")
    }
}