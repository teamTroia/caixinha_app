package com.troia.cadastro_produtos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.troia.cadastro_produtos.databinding.ProdutoItenBinding
import com.troia.core.types.Product

class ProductAdapter(
    private val productList: ArrayList<Product> = arrayListOf()
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProdutoItenBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.name.text = item.name
        holder.price.text = String.format("R$ %.2f", item.price)
    }

    fun addProduct(product: Product) {
        if(productList.find { it.name == product.name } == null) {
            productList.add(product)
            notifyItemInserted(itemCount - 1)
        } else {
            val mproduct = productList.find { it.name == product.name }
            mproduct?.price = product.price
            val index = productList.indexOf(mproduct)
            if(index != -1)
                notifyItemChanged(index)
        }
    }

    fun updateList(newList: ArrayList<Product>){
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(view: ProdutoItenBinding): RecyclerView.ViewHolder(view.root) {
        val name = view.productName
        val price = view.productPrice
    }
}