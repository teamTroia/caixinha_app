package com.troia.app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.troia.app.databinding.CaixinhaItemBinding
import com.troia.core.types.UserProduct

class CaixinhaListAdapter(
    var itensList: ArrayList<UserProduct>,
    val listener: CaixinhaAdapterListener
) : RecyclerView.Adapter<CaixinhaListAdapter.CaixinhaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaixinhaViewHolder {
        return CaixinhaViewHolder(CaixinhaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            this.btnMinus.setOnClickListener {
                listener.onProductMinusClick(itensList[adapterPosition])
            }

            this.btnPlus.setOnClickListener {
                listener.onProductPlusClick(itensList[adapterPosition])
            }

            this.btnInfo.setOnClickListener {
                listener.onProductInfoClick(itensList[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: CaixinhaViewHolder, position: Int) {
        holder.name.text = itensList[position].name
        holder.quantity.text = itensList[position].quantity.toString()
    }

    override fun getItemCount(): Int {
        return itensList.size
    }

    fun updateItem(produto: UserProduct){
        val index = itensList.indexOfFirst { it.name == produto.name }
        if(index != -1)
            notifyItemChanged(index)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(list: ArrayList<UserProduct>){
        itensList.clear()
        itensList.addAll(list)
        notifyDataSetChanged()
    }

    class CaixinhaViewHolder(view: CaixinhaItemBinding): RecyclerView.ViewHolder(view.root){
        val name = view.productName
        val quantity = view.editQty
        val btnPlus = view.btnPlus
        val btnMinus = view.btnMinus
        val btnInfo = view.btnInfo
    }

    interface CaixinhaAdapterListener {
        fun onProductInfoClick(produto: UserProduct)
        fun onProductPlusClick(produto: UserProduct)
        fun onProductMinusClick(produto: UserProduct)
    }
}