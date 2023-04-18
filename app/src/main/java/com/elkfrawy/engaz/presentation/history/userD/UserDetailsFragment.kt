package com.elkfrawy.engaz.presentation.history.userD

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elkfrawy.engaz.databinding.FragmentUserDetailsBinding
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.presentation.history.HistoryViewModel
import com.elkfrawy.engaz.presentation.loadImage
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentUserDetailsBinding
    val uAdapter = UserDetailsAdapter()

    @Inject
    lateinit var databaseReference: DatabaseReference
    val viewModel: UserDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewsVisability(false)
        binding.apply {
            rateRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = uAdapter
                setHasFixedSize(true)
            }
        }

        viewModel.userId = requireArguments().getString("user_details_id") ?: ""

        viewModel.userId.let {
            databaseReference.child("Users").child(it).addChildEventListener(object :
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
                }
            })
        }

        lifecycleScope.launchWhenCreated {
            getRates()
            delay(500)
            getUsers()
            delay(500)
            uAdapter.submitList(viewModel.rates, viewModel.users)
            setViewsVisability(true)
        }
    }

    fun setUserData(user: FirebaseUser) {

        binding.apply {
            duName.text = user.name
            duAddress.text = user.address
            duMobile.text = user.mobile
            loadImage(duImg, user.url)
            if (user.total_rater == 0)
                duRate.text = "${0.0}"
            else {
                val rate = (user.total_rate!! / (user.total_rater)!!.toFloat())
                duRate.text = "$rate"
            }
        }
    }

    suspend fun getRates() {
        databaseReference.child("Rate").child(viewModel.userId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val rate = snap.getValue(Rate::class.java)!!
                            viewModel.rates.add(rate)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val rate = snap.getValue(Rate::class.java)!!
                            viewModel.rates.add(rate)

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

    suspend fun getUsers() {
        if (viewModel.rates.isNotEmpty()) {
            var sz = viewModel.rates.size - 1
            if (sz > 3) sz = 2

            for (i in 0..sz) {
                val id = viewModel.rates[i].from
                databaseReference.child("Users").child(id!!).addChildEventListener(object :
                    ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(FirebaseUser::class.java)!!
                            viewModel.users.add(user)
                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(FirebaseUser::class.java)!!
                            viewModel.users.add(user)
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
        }else{
            binding.noCommentTxt.show()
            binding.rateRv.hide()
        }
    }

    fun setViewsVisability(bool: Boolean) {
        if (bool) {
            binding.udPb.hide()
            binding.udVg.show()
        } else {
            binding.udPb.show()
            binding.udVg.hide()
        }
    }

}