package com.troia.caixinha.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.troia.caixinha.databinding.DialogProductDetailsBinding
import com.troia.core.types.UserProduct

class ProductDialog() : DialogFragment() {

    companion object {
        const val TAG = "ProductDialog"
        const val PRODUCT_KEY = "PRODUCT_KEY"
    }

    lateinit var binding: DialogProductDetailsBinding
    var productLGC: UserProduct? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogProductDetailsBinding.inflate(layoutInflater)
        binding.btnOk.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    fun setProduct(bundle: Bundle){
        productLGC = bundle.getSerializable(PRODUCT_KEY) as UserProduct
    }

    fun setData(){
        binding.productName.text = productLGC?.name
        binding.productQuantity.text = productLGC?.quantity.toString()
        binding.productPrice.text = String.format("R$ %.2f",productLGC?.price)
        binding.productTotal.text = String.format("R$ %.2f",(productLGC?.price?.times(productLGC?.quantity?:0)))
    }
}