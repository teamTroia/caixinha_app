package com.troia.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.troia.core.R
import com.troia.app.databinding.ProdutoItenBinding
import com.troia.core.fragment.GeneralDialog
import com.troia.core.repository.ProductsRepository
import com.troia.core.types.Product

class ProductAdapter(
    private val productList: ArrayList<Product> = arrayListOf(),
    private val context: AppCompatActivity
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProdutoItenBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.name.text = item.name
        holder.price.text = String.format("R$ %.2f", item.price)
        holder.deleteIcon.setOnClickListener {
            GeneralDialog.newInstance(
                context.getString(R.string.delete_title),
                context.getString(R.string.delete_body, item.name),
                context.getString(R.string.confirm),
                {
                    ProductsRepository.deleteProduct(item)
                },
                context.getString(R.string.cancel),
                {}
            ).show(context.supportFragmentManager, GeneralDialog.TAG)
        }
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
        val deleteIcon = view.delete
    }
}