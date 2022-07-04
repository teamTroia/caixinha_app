package com.troia.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.troia.app.R
import com.troia.app.databinding.HistoryItemBinding
import com.troia.core.types.Purchase
import java.text.SimpleDateFormat

class CaixinhaHistoryAdapter(
    private val purchaseList: ArrayList<Purchase>
): RecyclerView.Adapter<CaixinhaHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = purchaseList[position]
        val formater = SimpleDateFormat("dd/MM/yyyy")
        holder.date.text = formater.format(item.date)
        holder.value.text = String.format("R$ %.2f", item.value)
        holder.icon.setImageResource(if (item.paid) R.drawable.ic_payment_green else R.drawable.ic_payment_red)
        holder.icon.setOnClickListener {
            if (item.paid)
                Toast.makeText(holder.context, holder.context.getString(com.troia.core.R.string.paidAlert), Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(holder.context, holder.context.getString(com.troia.core.R.string.notPaidAlert), Toast.LENGTH_SHORT).show()

        }
    }


    override fun getItemCount(): Int {
        return purchaseList.size
    }

    fun updateList(purchases: Collection<Purchase>){
        purchaseList.clear()
        purchaseList.addAll(purchases)
        notifyDataSetChanged()
    }

    class HistoryViewHolder(view: HistoryItemBinding, val context: Context): RecyclerView.ViewHolder(view.root){
        val date = view.date
        val value = view.value
        val icon = view.paidImage
    }
}