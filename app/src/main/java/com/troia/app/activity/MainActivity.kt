package com.troia.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.troia.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdicinoar.setOnClickListener {
            val myIntent = Intent(this, ActivityCadastro::class.java)
            startActivity(myIntent)
        }

        binding.buttonLista.setOnClickListener {
            val myIntent = Intent(this, CaixinhaActivity::class.java)
            startActivity(myIntent)
        }
    }
}