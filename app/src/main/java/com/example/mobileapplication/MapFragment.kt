package com.example.mobileapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    private var mapView: MapView? = null
    private var mMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.map)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("mobilemap", mMap.toString())
        val SEOUL = LatLng(37.5665, 126.9780) // 서울의 위도와 경도 좌표
//        val markerOptions = MarkerOptions()
//        markerOptions.position(SEOUL)
//        markerOptions.title("서울")
//        markerOptions.snippet("한국 수도")
        mMap?.addMarker(MarkerOptions().position(SEOUL).title("서울").snippet("한국 수도"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10f)) // 초기 확대/축소 수준 설정
    }


    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}
