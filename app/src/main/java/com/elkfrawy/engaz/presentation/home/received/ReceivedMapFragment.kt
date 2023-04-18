package com.elkfrawy.engaz.presentation.home.received

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentReceivedMapBinding
import com.elkfrawy.engaz.domain.model.UserCluster
import com.elkfrawy.engaz.presentation.util.ClusterRender
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.model.DirectionsResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceivedMapFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentReceivedMapBinding
    lateinit var clusterManager: ClusterManager<UserCluster>
    lateinit var clusterRender: ClusterRender
    lateinit var geoApiContext: GeoApiContext

    private val lat1 = 31.01522844902963
    private val lng1 = 31.37765946383279
    private val lat2 = 31.04065731450524
    private val lng2 = 31.351440426956238

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReceivedMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.receivedMap.onCreate(savedInstanceState)
        binding.receivedMap.getMapAsync(this)


    }



    override fun onMapReady(p0: GoogleMap) {

        clusterManager = ClusterManager(requireContext(), p0)
        clusterRender = ClusterRender(requireContext(), p0, clusterManager)
        clusterManager.renderer = clusterRender
        geoApiContext = GeoApiContext.Builder().apiKey(getString(R.string.map_api_key)).build()
        p0.setOnCameraIdleListener(clusterManager)
        p0.setOnMarkerClickListener(clusterManager)

        addItem()
        calculateDirection()
    }



    private fun addItem(){
        var lat = 30.9493905
        var long = 31.1753501
        val user = UserCluster(LatLng(lat, long), "Ahmed", "this is snippet", R.drawable.user2, null)
        val user2 = UserCluster(LatLng(lat2, lng2), "Mohamed", "who needs help", R.drawable.user2, null)
        val user3 = UserCluster(LatLng(lat1, lng1), "Me", "who will help mohamed", R.drawable.user2, null)
        clusterManager.setAnimation(true)
        clusterManager.addItem(user)
        clusterManager.addItem(user2)
        clusterManager.addItem(user3)
    }

    fun calculateDirection(){

        val direction = com.google.maps.model.LatLng(lat2, lng2)
        val dirApi = DirectionsApiRequest(geoApiContext)
        dirApi.alternatives(true)
        dirApi.origin(com.google.maps.model.LatLng(lat1, lng1))

        dirApi.destination(direction).setCallback(object : PendingResult.Callback<DirectionsResult> {

            override fun onResult(result: DirectionsResult?) {
                Log.d("Direction result api", "result duration: ${result!!.routes[0].legs[0].duration}")
                Log.d("Direction result api", "result distance: ${result.routes[0].legs[0].distance}")
            }

            override fun onFailure(e: Throwable?) {
                Log.d("Direction result api", "onFailure: ${e?.message}")
            }
        })

    }

    override fun onStop() {
        super.onStop()
        binding.receivedMap.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.receivedMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.receivedMap.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.receivedMap.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.receivedMap.onDestroy()
    }

}