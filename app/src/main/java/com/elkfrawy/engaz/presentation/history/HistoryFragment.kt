package com.elkfrawy.engaz.presentation.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentHistoryBinding
import com.elkfrawy.engaz.domain.model.FirebaseHistory
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
class HistoryFragment : Fragment(), HistoryAdapter.OnHistoryClicked {

    lateinit var binding: FragmentHistoryBinding
    lateinit var hAdapter: HistoryAdapter

    @Inject
    lateinit var databaseReference: DatabaseReference
    val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hAdapter = HistoryAdapter(this)
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setProgressBarVisibillity(false)
        viewModel.historyItem.clear()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hAdapter
            setHasFixedSize(true)
        }
        //viewModel.getHistory()
        lifecycleScope.launch {
            databaseReference.child("History").child(viewModel.token)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            for (snap in snapshot.children) {
                                val historyItem = snap.getValue(FirebaseHistory::class.java)!!
                                viewModel.historyItem.add(historyItem)

                                if (viewModel.historyItem.isEmpty())
                                    setNoHistory()
                                else {
                                    hAdapter.itemAdded(
                                        viewModel.historyItem,
                                        viewModel.historyItem.size - 1
                                    )
                                    setProgressBarVisibillity(true)
                                }
                            }
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            val historyItem = snapshot.getValue(FirebaseHistory::class.java)
                            viewModel.historyItem.add(historyItem!!)
                            if (viewModel.historyItem.isEmpty())
                                setNoHistory()
                            else {
                                hAdapter.itemAdded(
                                    viewModel.historyItem,
                                    viewModel.historyItem.size - 1
                                )
                                setProgressBarVisibillity(true)
                            }
                        }
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Error Handling", "history: ${error.message}")
                    }
                })

            binding.historyToolbar.setOnMenuItemClickListener {

                when(it.itemId){
                    R.id.filter_asked->{
                        val newList = viewModel.historyItem.filter {
                            it.state!!
                        }
                        hAdapter.submitList(newList)
                    }
                    R.id.filter_received->{
                        val newList = viewModel.historyItem.filter {
                            !it.state!!
                        }
                        hAdapter.submitList(newList)
                    }

                }

                true
            }

            delay(500)
            if (viewModel.historyItem.size <= 0)
                setNoHistory()

        }
    }

    override fun onHistoryItemClicked(position: Int) {
        val i = Intent(requireActivity(), HistoryDetailsActivity::class.java)
        i.putExtra("historyItem", viewModel.historyItem[position])
        startActivity(i)
    }

    private fun setNoHistory() {
        binding.rvHistory.hide()
        binding.historyPb.hide()
        binding.noHistoryGroup.show()
        binding.historyAppbar.hide()
    }

    private fun showExceptionView(bool: Boolean) {
        binding.noHistoryTxt.hide()
        binding.historyPb.hide()
        binding.rvHistory.hide()
        binding.noHistoryImg.show()
        if (bool)
            binding.noHistoryImg.setImageResource(R.drawable.server_error)
        else
            binding.noHistoryImg.setImageResource(R.drawable.no_internet)
    }

    private fun setProgressBarVisibillity(bool: Boolean) {

        if (bool) {
            binding.rvHistory.show()
            binding.historyPb.hide()
            binding.historyAppbar.show()
            binding.noHistoryGroup.hide()

        } else {
            binding.rvHistory.hide()
            binding.historyPb.show()
            binding.historyAppbar.hide()
            binding.noHistoryGroup.hide()
        }

    }


}