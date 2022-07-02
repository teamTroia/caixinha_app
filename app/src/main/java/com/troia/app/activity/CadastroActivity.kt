package com.troia.app.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.troia.app.adapter.ProductAdapter
import com.troia.app.viewmodel.ProductsViewModel
import com.troia.app.databinding.ActivityCadastroBinding
import com.troia.core.database.DataNotifier
import com.troia.core.types.Product
import com.troia.core.utils.FirebaseUtils
import com.troia.core.types.SpaceItemDecoration
import com.troia.core.utils.UserUtils
import java.lang.Exception

class CadastroActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.root as Toolbar)
        setupNavMenu()
        setupAdapter()
        setupListeners()
    }

    fun setupNavMenu() {
        val actionBarToggle = ActionBarDrawerToggle(this, binding.root, 0, 0)
        binding.root.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        binding.navView.root.menu.findItem(com.troia.core.R.id.menu_products).isEnabled = UserUtils.isUserAdmin()
        binding.navView.root.menu.findItem(com.troia.core.R.id.menu_members).isEnabled = UserUtils.isUserAdmin()

        (binding.navView.root as NavigationView).setNavigationItemSelectedListener {
            when (it.itemId) {
                com.troia.core.R.id.menu_cart -> {
                    startActivity(Intent(this, CaixinhaActivity::class.java))
                    finish()
                    true
                }
                else -> {false}
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        binding.root.openDrawer(binding.navView.root)
        return true
    }

    override fun onBackPressed() {
        if (this.binding.root.isDrawerOpen(GravityCompat.START)) {
            this.binding.root.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun setupListeners() {
        binding.buttonAdd.setOnClickListener {
            try {
                val name = binding.editProduct.text.toString()
                val price = binding.editPrice.text.toString().toDouble()
                val product = Product(name, price)
                FirebaseUtils.saveProduct(product)
            } catch (e: Exception) {
                Log.println(Log.ERROR, "EXCEPTION", e.toString())
            }
        }

        viewModel.updatedList.observe(this) {
            adapter.updateList(viewModel.getProducts())
        }
    }

    override fun onStart() {
        super.onStart()
        DataNotifier.subscribe(viewModel)
    }

    override fun onStop() {
        super.onStop()
        DataNotifier.unsubscribe(viewModel)
    }

    fun setupAdapter() {
        binding.recyclerView.apply {
            itemAnimator = null
            this@CadastroActivity.adapter = ProductAdapter(viewModel.getProducts())
            adapter = this@CadastroActivity.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(15, RecyclerView.VERTICAL, 0, 15))
        }
    }
}