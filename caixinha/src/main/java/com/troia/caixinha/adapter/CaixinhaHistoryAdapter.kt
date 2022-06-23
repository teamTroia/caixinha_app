package com.troia.caixinha.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.troia.caixinha.databinding.HistoryItemBinding
import com.troia.core.types.Purchase
import java.text.SimpleDateFormat

class CaixinhaHistoryAdapter(
    val purchaseList: ArrayList<Purchase>
): RecyclerView.Adapter<CaixinhaHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = purchaseList[position]
        val formater = SimpleDateFormat("dd/MM/yyyy")
        holder.date.text = formater.format(item.date)
        holder.value.text = String.format("R$ %.2f", item.value)
    }


    override fun getItemCount(): Int {
        return purchaseList.size
    }

    fun updateList(purchases: Collection<Purchase>){
        purchaseList.clear()
        purchaseList.addAll(purchases)
        notifyDataSetChanged()
    }

    class HistoryViewHolder(view: HistoryItemBinding): RecyclerView.ViewHolder(view.root){
        val date = view.date
        val value = view.value
    }
}