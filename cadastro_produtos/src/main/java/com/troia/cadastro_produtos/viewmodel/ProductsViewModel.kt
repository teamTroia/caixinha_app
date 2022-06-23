package com.troia.cadastro_produtos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troia.core.database.DataObserver
import com.troia.core.repository.ProductsRepository
import com.troia.core.database.NotificationType
import com.troia.core.types.Product
import com.troia.core.utils.FirebaseUtils

class ProductsViewModel: ViewModel(), DataObserver {
    var productsList: ArrayList<Product> = arrayListOf()
    val updatedList = MutableLiveData(false)

    fun load_products() {
        productsList.addAll(ProductsRepository.get_products())
        FirebaseUtils.get_all_products()
    }

    fun getProducts(): ArrayList<Product> {
        return arrayListOf<Product>().apply {
            addAll(productsList)
        }
    }

    override fun notify(type: NotificationType, data: Any?) {
        when (type) {
            NotificationType.ProductsLoad -> {
                productsList.clear()
                productsList.addAll(ProductsRepository.get_products())
                updatedList.postValue(true)
            }
            else -> {}
        }

    }


}