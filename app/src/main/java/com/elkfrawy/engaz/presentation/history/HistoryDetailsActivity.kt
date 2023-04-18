package com.elkfrawy.engaz.presentation.history

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.ActivityHistoryDetailsBinding
import com.elkfrawy.engaz.domain.model.FirebaseHistory
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.model.UserCluster
import com.elkfrawy.engaz.presentation.history.rate.RateFragment
import com.elkfrawy.engaz.presentation.history.userD.UserDetailsFragment
import com.elkfrawy.engaz.presentation.loadImage
import com.elkfrawy.engaz.presentation.util.ClusterRender
import com.elkfrawy.engaz.presentation.util.EDIT_RATE_ID
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityHistoryDetailsBinding
    val viewModel: HistoryViewModel by viewModels()
    var lat = 0.0
    var lng = 0.0
    lateinit var clusterRender: ClusterRender
    lateinit var clusterManager: ClusterManager<UserCluster>

    @Inject
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.hdMapView.onCreate(savedInstanceState)
        setViewsVisability(false)
        viewModel.getToken()

        setReview(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.item =
                intent.extras?.getParcelable("historyItem", FirebaseHistory::class.java)!!
        } else
            viewModel.item = intent.extras?.getParcelable("historyItem")!!

        lifecycleScope.launch {
            getRate()
            getUser()
        }

        lat = viewModel.item.latitude!!.toDouble()
        lng = viewModel.item.longitude!!.toDouble()
        setViewData(viewModel.item)
        binding.hdUserImg.setOnClickListener { openUserDetailsDialog() }
        binding.hdUserName.setOnClickListener { openUserDetailsDialog() }
        binding.hdMapView.getMapAsync(this)
        binding.reviewGroup.setOnClickListener {
            val b = bundleOf()
            b.putString("rate_user_id", viewModel.item.user_token)
            b.putInt("editRate", EDIT_RATE_ID)
            b.putFloat("rating_number", viewModel.rate.rate!!)
            b.putString("rating_message", viewModel.rate.message!!)
            b.putFloat("total_rate_user", viewModel.user.total_rate!!)
            b.putString("rate_key", viewModel.rateKey)
            supportFragmentManager.beginTransaction().add(RateFragment::class.java, b, "Rate")
                .commit()
        }
        binding.typeReview.setOnClickListener {
            val b = bundleOf()
            b.putString("rate_user_id", viewModel.item.user_token)
            b.putFloat("total_rate_user", viewModel.user.total_rate!!)
            b.putInt("total_rater_user", viewModel.user.total_rater!!)
            supportFragmentManager.beginTransaction().add(RateFragment::class.java, b, "Rate")
                .commit()
        }
        binding.historyDetailsToolbar.setNavigationOnClickListener { finish() }

    }

    fun setRateDate(rate: Rate) {
        binding.apply {
            setReview(true)
            hdRateBar.rating = rate.rate!!
            hdComment.text = rate.message!!
        }
    }

    fun setViewData(item: FirebaseHistory) {

        binding.apply {
            hdTitle.text = item.title
            hdDesc.text = item.description
            hdDate.text = item.date
            hdProblemType.text = item.problem_type
            setViewsVisability(true)
        }
    }

    suspend fun getUser() {

        databaseReference.child("Users").child(viewModel.item.user_token!!)
            .addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(FirebaseUser::class.java)!!
                        viewModel.user = user
                        setUserData(user)

                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(FirebaseUser::class.java)!!
                        viewModel.user = user
                        setUserData(user)
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    suspend fun getRate() {
        databaseReference.child("Rate").child(viewModel.item.user_token!!)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val rate = snap.getValue(Rate::class.java)!!
                            if (rate.from.equals(viewModel.token)) {
                                viewModel.rateKey = snap.key!!
                                viewModel.rate = rate
                                setRateDate(rate)
                            }
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            Log.d("Error Handling", "rate: ${snap.getValue()}")
                            Log.d("Error Handling", "rate: ${snap.getValue(Rate::class.java)!!}")

                            val rate = snap.getValue(Rate::class.java)!!
                            if (rate.from.equals(viewModel.token)) {
                                viewModel.rate = rate
                                setRateDate(rate)
                            }
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun setUserData(user: FirebaseUser) {
        binding.apply {
            hdUserNumber.text = "${user.mobile}"
            hdUserName.text = "${user.name}"
            loadImage(hdUserImg, user.url)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        p0.isBuildingsEnabled = true

        val bottomBoundary = lat - 0.0009
        val leftBoundary = lng - 0.0009
        val topBoundary = lat + 0.0009
        val rightBoundary = lng + 0.0009

        clusterManager = ClusterManager(this@HistoryDetailsActivity, p0)
        clusterRender = ClusterRender(this@HistoryDetailsActivity, p0, clusterManager)
        clusterManager.renderer = clusterRender
        p0.setOnCameraIdleListener(clusterManager)
        p0.setOnMarkerClickListener(clusterManager)
        clusterManager.setAnimation(true)
        clusterManager.addItem(UserCluster(LatLng(lat, lng), "Person", "", R.drawable.user2, null))

        val cUpdate =
            LatLngBounds(LatLng(bottomBoundary, leftBoundary), LatLng(topBoundary, rightBoundary))
        p0.setOnMapLoadedCallback {
            p0.moveCamera(CameraUpdateFactory.newLatLngBounds(cUpdate, 0))
        }

    }

    fun setReview(bool: Boolean) {
        if (bool) {
            binding.typeReview.hide()
            binding.reviewGroup.show()
        } else {
            binding.typeReview.show()
            binding.reviewGroup.hide()
        }
    }

    fun setViewsVisability(bool: Boolean) {
        if (bool) {
            binding.allViewVg.show()
            binding.hdProgress.hide()

        } else {
            binding.allViewVg.hide()
            binding.hdProgress.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)


    }

    private fun openUserDetailsDialog() {
        val b = bundleOf()
        b.putString("user_details_id", viewModel.item.user_token)
        supportFragmentManager.beginTransaction()
            .add(UserDetailsFragment::class.java, b, "userInfo").commit()

    }


    override fun onStop() {
        super.onStop()
        binding.hdMapView.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.hdMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.hdMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.hdMapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.hdMapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.hdMapView.onDestroy()
    }


}