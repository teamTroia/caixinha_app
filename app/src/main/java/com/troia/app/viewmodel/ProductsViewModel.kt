package com.troia.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troia.core.database.DataObserver
import com.troia.core.repository.ProductsRepository
import com.troia.core.database.NotificationType
import com.troia.core.types.Product
import com.troia.core.utils.FirebaseUtils

class ProductsViewModel: ViewModel(), DataObserver {
    val updatedList = MutableLiveData(false)

    fun getProducts(): ArrayList<Product> {
        return arrayListOf<Product>().apply {
            addAll(ProductsRepository.get_products())
        }
    }

    override fun notify(type: NotificationType, data: Any?) {
        when (type) {
            NotificationType.ProductsLoad -> {
                updatedList.postValue(true)
            }
            else -> {}
        }

    }


}