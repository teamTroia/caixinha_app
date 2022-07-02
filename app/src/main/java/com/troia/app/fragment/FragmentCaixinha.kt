package com.troia.app.fragment

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troia.app.adapter.CaixinhaListAdapter
import com.troia.app.databinding.CaixinhaFragmentBinding
import com.troia.app.viewmodel.ViewModelCaixinha
import com.troia.core.types.UserProduct
import com.troia.core.types.SpaceItemDecoration
import com.troia.core.utils.UserUtils
import java.io.Serializable

class FragmentCaixinha: Fragment(), CaixinhaListAdapter.CaixinhaAdapterListener {

    private lateinit var binding: CaixinhaFragmentBinding
    private lateinit var caixinhaAdapter: CaixinhaListAdapter
    private lateinit var viewModel: ViewModelCaixinha

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CaixinhaFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelCaixinha::class.java)
        setupAdapter()
        setupEditTextListener()
        setupViewModelListeners()
        setTotal()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveProducts()
    }

    private fun setupEditTextListener(){
        binding.editSearch.addTextChangedListener {
            caixinhaAdapter.setProductList(viewModel.getProductsList(it.toString()))
        }
    }

    private fun setupViewModelListeners() {
        viewModel.updatedProductsList.observe(viewLifecycleOwner) {
            if(it){
                val list = viewModel.getProductsList("")
                if (list.size > 0) {
                    caixinhaAdapter.setProductList(list)
                    setTotal()
                }
            }
        }
    }

    private fun setupAdapter(){
        viewModel.getProductsList("").let {
            caixinhaAdapter = CaixinhaListAdapter(it, this)
        }
        binding.viewItens.apply {
            itemAnimator = null
            clearOnScrollListeners()
            adapter = caixinhaAdapter
            layoutManager = LinearLayoutManager(context).apply {
                isAutoMeasureEnabled = false
            }
            addItemDecoration(SpaceItemDecoration(15, RecyclerView.VERTICAL, 0, 15))
        }
    }

    override fun onProductInfoClick(produto: UserProduct) {
        val bundle = Bundle()
        bundle.putSerializable(ProductDialog.PRODUCT_KEY, produto as Serializable)
        val sheet = ProductDialog().apply {
            setProduct(bundle)
        }
        sheet.show(childFragmentManager, ProductDialog.TAG)
    }

    override fun onProductPlusClick(produto: UserProduct) {
        produto.increaseQuantity(1)
        caixinhaAdapter.updateItem(produto)
        setTotal()
    }

    override fun onProductMinusClick(produto: UserProduct) {
        produto.increaseQuantity(-1)
        caixinhaAdapter.updateItem(produto)
        setTotal()
    }

    private fun setTotal(){
        binding.textTotal.text = String.format("R$ %.2f",viewModel.getTotal())
    }
}
