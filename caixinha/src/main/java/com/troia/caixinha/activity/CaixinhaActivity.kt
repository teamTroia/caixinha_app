package com.troia.caixinha.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.troia.cadastro_produtos.activity.ActivityCadastro
import com.troia.caixinha.databinding.ActivityCaixinhaBinding
import com.troia.caixinha.R
import com.troia.caixinha.adapter.CaixinhaPagerAdapter
import com.troia.caixinha.fragment.BottomSheetSettings
import com.troia.caixinha.viewmodel.ViewModelCaixinha
import com.troia.core.database.DataNotifier
import com.troia.core.fragment.GeneralDialog
import java.io.Serializable

class CaixinhaActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCaixinhaBinding
    private lateinit var viewModel: ViewModelCaixinha

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaixinhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.root)
        setupNavMenu()
        setupViewPager()
        viewModel = ViewModelProvider(this).get(ViewModelCaixinha::class.java)
    }

    fun setupNavMenu() {
        val actionBarToggle = ActionBarDrawerToggle(this, binding.root, 0, 0)
        binding.root.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
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

    override fun onResume() {
        super.onResume()
        viewModel.loadProductsList()
    }

    override fun onStart() {
        super.onStart()
        DataNotifier.subscribe(viewModel)
    }

    override fun onStop() {
        super.onStop()
        DataNotifier.unsubscribe(viewModel)
    }

    private fun setupViewPager(){
        binding.viewPager.adapter = CaixinhaPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = true

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.text = when (position){
                0 -> getString(com.troia.core.R.string.title_caixinha)
                else -> getString(com.troia.core.R.string.title_history)
            }
        }.also {
            it.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_caixinha, menu)
        return true
    }

    private fun updateCallback(){
        if (viewModel.getTotal() > 0.0) {
            GeneralDialog.newInstance(
                getString(com.troia.core.R.string.labelWarning),
                getString(com.troia.core.R.string.warningNeedCheckout),
                getString(com.troia.core.R.string.labelOk),
                {}
            ).show(supportFragmentManager, GeneralDialog.TAG)
        }
        else {
            viewModel.getNewList()
        }
    }


    private fun checkoutCallback(){
        if (viewModel.getTotal() > 0.0) {
            GeneralDialog.newInstance(
                getString(com.troia.core.R.string.checkout),
                String.format("Você esta fechando sua conta em um valor de R$ %.2f.\nConfirma?", viewModel.getTotal()),
                getString(com.troia.core.R.string.confirm),
                {viewModel.checkout()},
                getString(com.troia.core.R.string.cancel),
                {}
            ).show(supportFragmentManager, GeneralDialog.TAG)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val bundle = Bundle()
                bundle.putSerializable(BottomSheetSettings.UPDATE_LISTENER, ::updateCallback as Serializable)
                bundle.putSerializable(BottomSheetSettings.CHECKOUT_LISTENER, ::checkoutCallback as Serializable?)
                val sheet = BottomSheetSettings.newInstance(bundle)
                sheet.show(supportFragmentManager, BottomSheetSettings.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}