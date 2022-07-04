package com.troia.app.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.troia.app.databinding.SettingsBottomsheetBinding
import com.troia.core.R

class BottomSheetSettings: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "BottomSheetSettings"
        const val UPDATE_LISTENER = "UpdateListener"
        const val CHECKOUT_LISTENER = "CheckoutListener"
        fun newInstance(bundle: Bundle) = BottomSheetSettings().apply {
            arguments = bundle
        }
    }

    lateinit var binding: SettingsBottomsheetBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(),R.style.BottomSheetStyle);
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsBottomsheetBinding.inflate(layoutInflater)
        binding.root.setBackgroundColor(Color.TRANSPARENT)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.checkout.setOnClickListener {
            val checkout: () -> Unit = arguments?.getSerializable(CHECKOUT_LISTENER) as () -> Unit
            checkout()
            dismiss()
        }

        binding.updateProducts.setOnClickListener {
            val update: () -> Unit = arguments?.getSerializable(UPDATE_LISTENER) as () -> Unit
            update()
            dismiss()
        }
    }
}