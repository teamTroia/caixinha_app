package com.troia.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.troia.app.adapter.MembersAdapter
import com.troia.app.databinding.ActivityMembrosBinding
import com.troia.app.viewmodel.MembersViewModel
import com.troia.core.types.SpaceItemDecoration
import com.troia.core.types.User
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.PreferencesManager
import com.troia.core.utils.UserUtils

class MembrosActivity: AppCompatActivity() {

    lateinit var binding: ActivityMembrosBinding
    lateinit var membersAdapter: MembersAdapter
    val viewModel: MembersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.root)
        setupAdapter()
        setupNavMenu()
        setupListeners()
    }

    private fun setupListeners() {
        viewModel.updatedMembers.observe(this) {
            membersAdapter.setMembers(viewModel.getMembersList())
        }
    }

    private fun setupNavMenu() {
        val actionBarToggle = ActionBarDrawerToggle(this, binding.root, 0, 0)
        binding.root.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        binding.navView.root.menu.findItem(com.troia.core.R.id.menu_products).isEnabled = UserUtils.isUserAdmin()
        binding.navView.root.menu.findItem(com.troia.core.R.id.menu_members).isEnabled = UserUtils.isUserAdmin()

        (binding.navView.root as NavigationView).setNavigationItemSelectedListener {
            when (it.itemId) {
                com.troia.core.R.id.menu_products -> {
                    startActivity(Intent(this, CadastroActivity::class.java))
                    finish()
                    true
                }
                com.troia.core.R.id.menu_logout -> {
                    FirebaseUtils.removeListeners()
                    PreferencesManager.eraseLogin()
                    val myIntent = Intent(this, LoginActivity::class.java)
                    startActivity(myIntent)
                    finish()
                    true
                }
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

    private fun setupAdapter() {
        membersAdapter = MembersAdapter(viewModel.getMembersList())
        binding.recyclerView.apply{
            adapter = membersAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(15, RecyclerView.VERTICAL, 0, 15))
        }
    }
}