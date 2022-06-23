package com.troia.app.database

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.troia.core.database.DataNotifier
import com.troia.core.repository.CaixinhaRepository
import com.troia.core.repository.ProductsRepository
import com.troia.core.database.NotificationType
import com.troia.core.types.Product
import com.troia.core.types.UserProduct
import com.troia.core.utils.FirebaseUtils

class FirebaseAccess(context: Context) : FirebaseUtils.FirebaseUtilsAdapter {
    companion object {
        const val PRODUCTS_KEY = "keyProducts"
        const val USERS_KEY = "keyUsers"
        const val USER_DATA_KEY = "keyUserData"

        const val NAME_KEY = "name"
        const val PASS_KEY = "pass"
        const val ADMIN_KEY = "admin"
    }

    var database: FirebaseDatabase

    init {
        FirebaseApp.initializeApp(context)
        database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
    }

    override fun save_product(products: Product) {
        products.let {
            database.reference.child(PRODUCTS_KEY).child(it.name).setValue(it.price)
        }
    }

    override fun save_user_cart(user: String, products: ArrayList<UserProduct>) {
        products.forEach {
            database.reference.child(USERS_KEY).child(PRODUCTS_KEY).child(user).child(it.name).setValue(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun get_all_products() {
        val products: ArrayList<Product> = arrayListOf()
        val productListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) return
                (p0.value as HashMap<String, Double>).forEach {
                    products.add(Product(it.key,it.value))
                }
                ProductsRepository.addProduct(products)
                DataNotifier.notifyData(NotificationType.ProductsLoad)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(PRODUCTS_KEY).addValueEventListener(productListener)

    }

    override fun get_quantity_for_user(user: String) {
        val productUserListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) {
                    DataNotifier.notifyData(NotificationType.QuantityLoad, false)
                    return
                }
                val list: ArrayList<UserProduct> = arrayListOf()
                p0.children.forEach {
                    it.getValue(UserProduct::class.java)?.let { list.add(it) }
                }
                CaixinhaRepository.setList(list)
                DataNotifier.notifyData(NotificationType.QuantityLoad, true)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(PRODUCTS_KEY).child(user).addListenerForSingleValueEvent(productUserListener)
    }

    override fun validate_email(email:String) {
        val productUserListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                DataNotifier.notifyData(NotificationType.EmailVerification, p0.value == null)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(email).addListenerForSingleValueEvent(productUserListener)
    }

    override fun get_user_data(email:String) {
        val productUserListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) {
                    DataNotifier.notifyData(NotificationType.UserData, null)
                } else {
                    val map = p0.value as HashMap<String, String>
                    DataNotifier.notifyData(NotificationType.UserData,Triple(map[NAME_KEY], map[PASS_KEY], map[ADMIN_KEY]))
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(email).addListenerForSingleValueEvent(productUserListener)
    }

    override fun register_user(email: String, name: String, pass: String) {
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(email).child(NAME_KEY).setValue(name)
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(email).child(PASS_KEY).setValue(pass)
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(email).child(ADMIN_KEY).setValue("0")
    }

}