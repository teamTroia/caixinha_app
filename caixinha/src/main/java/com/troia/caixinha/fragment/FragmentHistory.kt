package com.troia.caixinha.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troia.caixinha.adapter.CaixinhaHistoryAdapter
import com.troia.caixinha.databinding.HistoryFragmentBinding
import com.troia.caixinha.viewmodel.ViewModelCaixinha
import com.troia.core.types.SpaceItemDecoration

class FragmentHistory: Fragment() {

    private lateinit var binding: HistoryFragmentBinding
    private lateinit var viewModel: ViewModelCaixinha
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
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelCaixinha::class.java)
        setupRecyclerView()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        historyAdapter.updateList(viewModel.getPurchaseList())
    }

    fun setupListeners(){
        viewModel.updatedPurchaseList.observe(viewLifecycleOwner){
            if(viewModel.getPurchaseList().isEmpty()){
                binding.tvNothing.visibility = View.VISIBLE
            } else {
                historyAdapter.updateList(viewModel.getPurchaseList())
                binding.tvNothing.visibility = View.GONE
            }
        }
    }

    fun setupRecyclerView(){
        historyAdapter = CaixinhaHistoryAdapter(viewModel.getPurchaseList())
        binding.recyclerView.apply{
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(15, RecyclerView.VERTICAL, 0, 15))
        }
    }
}