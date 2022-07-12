package com.troia.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.troia.app.fragment.FragmentCaixinha
import com.troia.app.fragment.FragmentHistory
import com.troia.core.utils.UserUtils

class CaixinhaPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment){

    private val fragmentCaixinha = FragmentCaixinha()
    private val fragmentHistory = FragmentHistory.newInstance(UserUtils.getUser())

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> fragmentCaixinha
            else -> fragmentHistory
        }
    }



}