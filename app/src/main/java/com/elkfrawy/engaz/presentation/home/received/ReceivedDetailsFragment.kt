package com.elkfrawy.engaz.presentation.home.received

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentReceivedDetailsBinding
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.Problem
import com.elkfrawy.engaz.domain.model.UserCluster
import com.elkfrawy.engaz.presentation.history.userD.UserDetailsFragment
import com.elkfrawy.engaz.presentation.home.HomeViewModel
import com.elkfrawy.engaz.presentation.loadImage
import com.elkfrawy.engaz.presentation.util.ClusterRender
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReceivedDetailsFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentReceivedDetailsBinding
    private var lat = 31.01522844902963
    private var lng = 31.37765946383279
    lateinit var clusterRender: ClusterRender
    lateinit var clusterManager: ClusterManager<UserCluster>

    @Inject
    lateinit var databaseReference: DatabaseReference
    val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReceivedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewsVisibility(false)

        val notificationToken = requireArguments().getString("token")
        val receivedToken = requireArguments().getString("userId")
        binding.receivedMap.onCreate(savedInstanceState)
        viewModel.problemToken = receivedToken ?: notificationToken ?: ""

        Log.d("Notification Error", "rd token: ${viewModel.problemToken}")
        if (viewModel.problemToken.isEmpty()){
            setNoData()
        }

        databaseReference.child("Problem").child(viewModel.problemToken)
            .addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val problem = snapshot.getValue(Problem::class.java)!!
                        viewModel.problem = problem
                        lat = problem.latitude!!.toDouble()
                        lng = problem.longitude!!.toDouble()
                        binding.receivedMap.getMapAsync(this@ReceivedDetailsFragment)
                        setProblemData(problem)
                    }else
                        setNoData()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                    setNoData()
                }
            })

        databaseReference.child("Users").child(viewModel.problemToken).addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(FirebaseUser::class.java)!!
                    setUserData(user)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(FirebaseUser::class.java)!!
                    setUserData(user)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                setNoData()
            }
        })


        binding.acceptHelp.setOnClickListener {

            val i = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(("http://maps.google.com/maps?daddr=${lat},${lng}"))
            )
            startActivity(i)
            val history = databaseReference.child("History").child(viewModel.problemToken).child("class").push()
            val history2 = databaseReference.child("History").child(viewModel.token).child("class").push()
            viewModel.problem?.let {
                history.child("title").setValue(it.title)
                history.child("description").setValue(it.description)
                history.child("date").setValue(it.date)
                history.child("latitude").setValue(it.latitude)
                history.child("longitude").setValue(it.longitude)
                history.child("problem_type").setValue(it.problem_type)
                history.child("user_token").setValue(viewModel.token)
                history.child("state").setValue(true)

                history2.child("title").setValue(it.title)
                history2.child("description").setValue(it.description)
                history2.child("date").setValue(it.date)
                history2.child("latitude").setValue(it.latitude)
                history2.child("longitude").setValue(it.longitude)
                history2.child("problem_type").setValue(it.problem_type)
                history2.child("user_token").setValue(viewModel.problemToken)
                history2.child("state").setValue(false)

                databaseReference.child("Problem").child(viewModel.problemToken).removeValue()
                findNavController().popBackStack()

            }
        }


        binding.rdUserImg.setOnClickListener { openUserDetailsDialog() }
        binding.rdUserName.setOnClickListener { openUserDetailsDialog() }

        binding.receivedDetailsToolbar.setNavigationOnClickListener { findNavController().popBackStack() }



    }

    fun setUserData(user: FirebaseUser) {

        binding.rdUserName.text = user.name
        binding.rdUserNumber.text = "${user.mobile}"
        loadImage(binding.rdUserImg, user.url)
        setViewsVisibility(true)
    }

    fun setNoData(){
        binding.apply {
            receivedVg.hide()
            noProblemVg.show()
            receivedProgress.hide()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        p0.isMyLocationEnabled = true
        p0.isBuildingsEnabled = true

        val bottomBoundary = lat - 0.002
        val leftBoundary = lng - 0.002
        val topBoundary = lat + 0.002
        val rightBoundary = lng + 0.002


        clusterManager = ClusterManager(requireContext(), p0)
        clusterRender = ClusterRender(requireContext(), p0, clusterManager)
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

    override fun onStart() {
        super.onStart()
        binding.receivedMap.onStart()
    }

    private fun openUserDetailsDialog() {
        val b = bundleOf()
        b.putString("user_details_id", viewModel.problemToken)
        childFragmentManager.beginTransaction().add(UserDetailsFragment::class.java, b, "userInfo").commit()
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

    fun setProblemData(problem: Problem) {
        binding.apply {
            receivedTitle.text = problem.title
            receivedDesc.text = problem.description
            receivedType.text = problem.problem_type
        }
    }

    fun setViewsVisibility(bool: Boolean){
        if (bool){
            binding.receivedVg.show()
            binding.receivedProgress.hide()
            binding.noProblemVg.hide()
        }else{
            binding.receivedVg.hide()
            binding.receivedProgress.show()
            binding.noProblemVg.hide()
        }
    }

}