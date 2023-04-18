package com.elkfrawy.engaz.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentRatesBinding
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class RatesFragment : Fragment() {

    lateinit var binding: FragmentRatesBinding

    @Inject
    lateinit var databaseReference: DatabaseReference
    val viewModel: ProfileViewModel by viewModels()
    lateinit var rAdapter:RatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewVisability(false)
        viewModel.rates.clear()
        viewModel.rateUsers.clear()
        rAdapter = RatesAdapter()
        binding.ratesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.ratesRv.adapter = rAdapter
        binding.ratesRv.setHasFixedSize(true)

        binding.ratesToolbar.setNavigationOnClickListener { }

        lifecycleScope.launch {
            getRates()
            delay(300)
            getUsers()
            delay(300)
            if (viewModel.rates.isEmpty())
                setNoRate()
            else
                rAdapter.submitList(viewModel.rates, viewModel.rateUsers)
        }

    }

    fun setNoRate(){
        binding.ratesPb.hide()
        binding.noRatesTxt.show()
        binding.ratesRv.hide()
        binding.ratesAppbar.hide()
    }

    fun setViewVisability(boolean: Boolean){
        binding.apply {
            if (boolean){
                ratesRv.show()
                ratesAppbar.show()
                ratesPb.hide()
            }else{
                ratesRv.hide()
                ratesAppbar.hide()
                ratesPb.show()
            }
        }
    }

    suspend fun getUsers(){
        if (viewModel.rates.isNotEmpty()) {
            var sz = viewModel.rates.size - 1
            for (i in 0..sz) {
                val id = viewModel.rates[i].from
                databaseReference.child("Users").child(id!!).addChildEventListener(object :
                    ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(FirebaseUser::class.java)!!
                            viewModel.rateUsers.add(user)
                            //rAdapter.submitList(viewModel.rates, viewModel.rateUsers)
                            setViewVisability(true)
                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(FirebaseUser::class.java)!!
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
        }
    }


    suspend fun getRates(){
        databaseReference.child("Rate").child(viewModel.userToken!!)
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
                    viewModel.rates.clear()
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
}