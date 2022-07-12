package com.troia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troia.app.adapter.CaixinhaHistoryAdapter
import com.troia.app.databinding.HistoryFragmentBinding
import com.troia.app.viewmodel.HistoryViewModel
import com.troia.core.database.DataNotifier
import com.troia.core.types.SpaceItemDecoration
import com.troia.core.types.User
import java.io.Serializable

class FragmentHistory: Fragment() {

    companion object {
        val USER = "keyUSER"
        fun newInstance(user: User?): FragmentHistory {
            val bundle = bundleOf(USER to user as Serializable)
            return FragmentHistory().apply {
                this.arguments = bundle
            }
        }
    }

    private lateinit var binding: HistoryFragmentBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: CaixinhaHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HistoryFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HistoryViewModel::class.java)
        val user = arguments?.get(USER) as User
        viewModel.setUser(user)
        setupRecyclerView()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        historyAdapter.updateList(viewModel.getPurchaseList())
    }

    override fun onStart() {
        super.onStart()
        DataNotifier.subscribe(viewModel)
    }

    override fun onStop() {
        super.onStop()
        DataNotifier.unsubscribe(viewModel)
    }

    private fun setupListeners(){
        viewModel.updatedPurchaseList.observe(viewLifecycleOwner){
            if(viewModel.getPurchaseList().isEmpty()){
                binding.tvNothing.visibility = View.VISIBLE
            } else {
                historyAdapter.updateList(viewModel.getPurchaseList())
                binding.tvNothing.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView(){
        viewModel.getPurchaseList().let {
            if(it.isEmpty()){
                binding.tvNothing.visibility = View.VISIBLE
            } else {
                binding.tvNothing.visibility = View.GONE
            }
            historyAdapter = CaixinhaHistoryAdapter(it)
        }
        binding.recyclerView.apply{
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(15, RecyclerView.VERTICAL, 0, 15))
        }
    }
}