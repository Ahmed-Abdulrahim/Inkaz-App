package com.elkfrawy.engaz.presentation.home.received

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentReceivedBinding
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.Problem
import com.elkfrawy.engaz.presentation.home.HomeViewModel
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReceivedFragment : Fragment(), ReceivedAdapter.OnReceivedClicked {

    lateinit var binding: FragmentReceivedBinding
    @Inject
    lateinit var databaseReference: DatabaseReference
    val viewModel: HomeViewModel by activityViewModels()
    val mAdapter = ReceivedAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReceivedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.problemList.clear()
        binding.receivedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = mAdapter
            setHasFixedSize(true)
        }

       lifecycleScope.launch {
           databaseReference.child("Problem")
               .addChildEventListener(object : ChildEventListener {
                   override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                       Log.d("Notification Error", "Problem Called")
                       if (snapshot.exists()) {

                           if (!snapshot.key.equals(viewModel.token)){
                               val problem = snapshot.child("class").getValue(Problem::class.java)
                               viewModel.ids.add(snapshot.key!!)
                               viewModel.problemList.add(problem!!)
                               if (viewModel.problemList.size <= 0)
                                   setNoData()
                               else{
                                   mAdapter.itemAdded(viewModel.problemList, viewModel.problemList.size - 1)
                                   setViewsVisibility(true)
                               }
                           }
                       }
                   }

                   override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                       viewModel.problemList.clear()
                       if (snapshot.exists()) {
                           viewModel.ids.add(snapshot.key!!)
                           if (!snapshot.key.equals(viewModel.token)){
                               Log.d("removed item", "Edit: ${snapshot.child("class").getValue(Problem::class.java)}")
                               val problem = snapshot.child("class").getValue(Problem::class.java)
                               viewModel.problemList.add(problem!!)
                               /*if (viewModel.problemList.size <= 0)
                                   setNoData()
                               else{
                                   mAdapter.itemAdded(viewModel.problemList, viewModel.problemList.size - 1)
                                   setViewsVisibility(true)
                               }*/
                           }
                       }else
                           Log.d("removed item", "No Data")
                   }
                   override fun onChildRemoved(snapshot: DataSnapshot) {
                       Log.d("removed item", "1")
                       viewModel.problemList.clear()
                       Log.d("removed item", "2")
                       if (snapshot.exists()) {
                           Log.d("removed item", "3")
                           viewModel.ids.add(snapshot.key!!)
                           Log.d("removed item", "4")
                           if (!snapshot.key.equals(viewModel.token)){
                               val problem = snapshot.child("class").getValue(Problem::class.java)
                               Log.d("removed item", "Remove: ${ snapshot.child("class").getValue(Problem::class.java)}")
                               viewModel.problemList.remove(problem!!)
                               Log.d("removed item", "Remove: ${viewModel.problemList.size}")
                               if (viewModel.problemList.size <= 0)
                                   setNoData()
                               else{
                                   Log.d("removed item", "is error here")
                                   mAdapter.itemAdded(viewModel.problemList, viewModel.problemList.size - 1)
                                   setViewsVisibility(true)
                               }
                           }
                       }else
                        Log.d("removed item", "No Data")
                   }

                   override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                   }

                   override fun onCancelled(error: DatabaseError) {
                       Log.d("Notification Error", "received cancelled")
                       Log.d("Notification Error", "message: ${error.message}")
                   }
               })
           delay(650)
           if (viewModel.problemList.size <= 0)
               setNoData()
       }

        binding.receivedToolbar.setNavigationOnClickListener { requireActivity().finish() }

    }

    override fun onReceivedItemClicked(position: Int) {
        val problem = viewModel.problemList[position]
        val id = viewModel.ids[position]
        Log.d("Notification Error", "length: ${viewModel.problemList.size}")
        val bundle = bundleOf()
        //bundle.putParcelable("problem", problem)
        bundle.putString("userId", id)
        findNavController().navigate(R.id.receivedDetailsFragment, bundle)
    }

    fun setNoData(){
        binding.viewsVg.hide()
        binding.receivedProblemPb.hide()
        binding.noProblemImg.show()
    }
    
    fun setViewsVisibility(bool: Boolean){
        if (bool){
            binding.viewsVg.show()
            binding.receivedProblemPb.hide()
            binding.noProblemImg.hide()
        }else{
            binding.viewsVg.hide()
            binding.receivedProblemPb.show()
            binding.noProblemImg.hide()
        }
    }

}