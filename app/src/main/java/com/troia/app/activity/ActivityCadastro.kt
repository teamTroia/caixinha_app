package com.troia.app.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troia.app.adapter.ProductAdapter
import com.troia.app.viewmodel.ProductsViewModel
import com.troia.app.databinding.ActivityCadastroBinding
import com.troia.core.types.Product
import com.troia.core.utils.FirebaseUtils
import com.troia.core.types.SpaceItemDecoration
import java.lang.Exception

class ActivityCadastro: AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.root as Toolbar)
        viewModel.load_products()
        setupAdapter()
        setupListeners()
    }

    fun setupListeners() {
        binding.buttonAdd.setOnClickListener {
            try {
                val name = binding.editProduct.text.toString()
                val price = binding.editPrice.text.toString().toDouble()
                val product = Product(name, price)
                adapter.addProduct(product)
                FirebaseUtils.save_product(product)
            } catch (e: Exception) {
                Log.println(Log.ERROR, "EXCEPTION", e.toString())
            }
        }

        viewModel.updatedList.observe(this) {
            adapter.updateList(viewModel.getProducts())
        }
    }

    fun setupAdapter() {
        binding.recyclerView.apply {
            itemAnimator = null
            this@ActivityCadastro.adapter = ProductAdapter(viewModel.getProducts())
            adapter = this@ActivityCadastro.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(15, RecyclerView.VERTICAL, 0, 15))
        }
    }
}