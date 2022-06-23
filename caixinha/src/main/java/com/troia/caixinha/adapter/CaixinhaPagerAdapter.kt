package com.troia.caixinha.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.troia.caixinha.fragment.FragmentCaixinha
import com.troia.caixinha.fragment.FragmentHistory

class CaixinhaPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment){

    private val fragmentCaixinha = FragmentCaixinha()
    private val fragmentHistory = FragmentHistory()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> fragmentCaixinha
            else -> fragmentHistory
        }
    }



}