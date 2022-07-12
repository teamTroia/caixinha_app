package com.troia.core.repository

import com.troia.core.types.Purchase
import com.troia.core.types.User
import com.troia.core.utils.FirebaseUtils

object CaixinhaRepository {
    val membersList: ArrayList<User> = arrayListOf()
    private val usersPurchases: HashMap<String, ArrayList<Purchase>> = hashMapOf()

    fun addPurchase(userEmail:String, purchase: Purchase){
        usersPurchases[userEmail]?.add(purchase)
        userEmail.let { FirebaseUtils.savePurchase(it,purchase) }
    }

    fun setPurchaseList(userEmail:String, list: ArrayList<Purchase>){
        if (usersPurchases[userEmail] == null)
            usersPurchases[userEmail] = ArrayList<Purchase>()
        usersPurchases[userEmail]?.clear()
        usersPurchases[userEmail]?.addAll(list)
    }

    fun getPurchases(userEmail: String): ArrayList<Purchase> {
        return usersPurchases[userEmail] ?: arrayListOf()
    }

    fun setMembersList(list: ArrayList<User>) {
        membersList.clear()
        membersList.addAll(list)
    }
}