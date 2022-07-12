package com.troia.app.database

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.troia.core.database.DataNotifier
import com.troia.core.repository.ProductsRepository
import com.troia.core.database.NotificationType
import com.troia.core.repository.CaixinhaRepository
import com.troia.core.types.Product
import com.troia.core.types.Purchase
import com.troia.core.types.User
import com.troia.core.types.UserProduct
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.UserUtils
import com.troia.core.utils.UserUtils.clean

class FirebaseAccess(context: Context) : FirebaseUtils.FirebaseUtilsAdapter {
    companion object {
        const val PRODUCTS_KEY = "keyProducts"
        const val PURCHASES_KEY = "keyPurchases"
        const val USERS_KEY = "keyUsers"
        const val USER_DATA_KEY = "keyUserData"
    }

    var database: FirebaseDatabase
    private var purchasesListeners: ArrayList<ValueEventListener> = arrayListOf()
    private lateinit var productUserListener: ValueEventListener
    private lateinit var membersListener: ValueEventListener
    init {
        FirebaseApp.initializeApp(context)
        database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
    }

    override fun removeListeners() {
        purchasesListeners.forEach {
            database.reference.removeEventListener(it)
        }
        database.reference.removeEventListener(productUserListener)
        database.reference.removeEventListener(membersListener)
    }

    override fun saveProduct(products: Product) {
        products.let {
            database.reference.child(PRODUCTS_KEY).child(it.name).setValue(it.price)
        }
    }

    override fun removeProduct(product: Product) {
        database.reference.child(PRODUCTS_KEY).child(product.name).removeValue()
        DataNotifier.notifyData(NotificationType.ProductsLoad, true)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getAllProducts() {
        val products: ArrayList<Product> = arrayListOf()
        val productListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) return
                products.clear()
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

    override fun getUserCart(user: String) {
        productUserListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) {
                    DataNotifier.notifyData(NotificationType.UserCartLoad, false)
                    return
                }
                val list: ArrayList<UserProduct> = arrayListOf()
                p0.children.forEach { dataset ->
                    dataset.getValue(UserProduct::class.java)?.let { list.add(it) }
                }
                UserUtils.setProductsList(list)
                DataNotifier.notifyData(NotificationType.UserCartLoad, true)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(PRODUCTS_KEY).child(user).addValueEventListener(productUserListener)
    }

    override fun saveUserCart(user: String, products: ArrayList<UserProduct>) {
        products.forEach {
            database.reference.child(USERS_KEY).child(PRODUCTS_KEY).child(user).child(it.name).setValue(it)
        }
    }

    override fun clearUserCart(user: String) {
        database.reference.child(USERS_KEY).child(PRODUCTS_KEY).child(user).removeValue()
    }

    override fun validateEmail(email:String) {
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

    override fun getUserData(email: String) {
        val productUserListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) {
                    DataNotifier.notifyData(NotificationType.UserData, null)
                } else {
                    val user = p0.getValue(User::class.java)
                    DataNotifier.notifyData(NotificationType.UserData, user)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(email).addListenerForSingleValueEvent(productUserListener)
    }

    override fun registerUser(user: User) {
        user.email?.clean()?.let { database.reference.child(USERS_KEY).child(USER_DATA_KEY).child(it).setValue(user)}
    }

    override fun savePurchase(user:String, purchase: Purchase) {
        database.reference.child(USERS_KEY).child(PURCHASES_KEY).child(user).child(purchase.date?.time.toString()).setValue(purchase)
    }



    override fun getPurchases(user: String) {
        val purchasesListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) {
                    DataNotifier.notifyData(NotificationType.PurchasesLoad, false)
                    return
                }
                val list: ArrayList<Purchase> = arrayListOf()
                p0.children.forEach { dataset ->
                    dataset.getValue(Purchase::class.java)?.let { list.add(it) }
                }
                CaixinhaRepository.setPurchaseList(user,list)
                DataNotifier.notifyData(NotificationType.PurchasesLoad, true)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(PURCHASES_KEY).child(user).addValueEventListener(purchasesListener)
        purchasesListeners.add(purchasesListener)
    }

    override fun getMembersList() {
        membersListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value == null) {
                    DataNotifier.notifyData(NotificationType.MembersLoaded, false)
                    return
                }
                val list: ArrayList<User> = arrayListOf()
                p0.children.forEach { dataset ->
                    dataset.getValue(User::class.java)?.let { list.add(it) }
                }
                CaixinhaRepository.setMembersList(list)
                DataNotifier.notifyData(NotificationType.MembersLoaded, true)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.println(Log.ERROR, "ERROR_DATABASE", p0.toString())
            }
        }
        database.reference.child(USERS_KEY).child(USER_DATA_KEY).addValueEventListener(membersListener)
    }
}