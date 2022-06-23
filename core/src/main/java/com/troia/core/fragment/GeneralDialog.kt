package com.troia.core.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.troia.core.databinding.GeneralAlertDialogBinding
import java.io.Serializable

class GeneralDialog: DialogFragment() {

    companion object {
        const val TITLE = "dialogTitle"
        const val BODY = "dialogBody"
        const val CONFIRM = "dialogConfirm"
        const val REFUSE = "dialogRefuse"
        const val CONFIRM_TEXT = "dialogConfirmText"
        const val REFUSE_TEXT = "dialogRefuseText"
        const val TAG = "GeneralDialog"

        fun newInstance(
            title: String,
            body: String,
            confirmText: String,
            confirmListener: () -> Unit,
            refuseText: String? = null,
            refuseListener: (() -> Unit)? = null
        ): GeneralDialog {
             val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(BODY, body)
            bundle.putString(CONFIRM_TEXT, confirmText)
            bundle.putString(REFUSE_TEXT, refuseText)
            bundle.putSerializable(CONFIRM, confirmListener as Serializable)
            bundle.putSerializable(REFUSE, refuseListener as Serializable?)
            return GeneralDialog().apply {
                arguments = bundle
            }
        }
    }

    lateinit var binding: GeneralAlertDialogBinding
    private lateinit var confirm: ()->Unit
    private var refuse: (()->Unit)? = null

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
        binding = GeneralAlertDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmText = arguments?.getString(CONFIRM_TEXT)
        val refuseText = arguments?.getString(REFUSE_TEXT)
        binding.btnConfirm.text = confirmText
        val title = arguments?.getString(TITLE)
        val body = arguments?.getString(BODY)
        binding.textTitle.text = title
        binding.textContent.text = body
        setupListeners()

        confirm = arguments?.getSerializable(CONFIRM) as ()->Unit
        refuse = arguments?.getSerializable(REFUSE) as (()->Unit)?
        if(refuse == null) {
            binding.btnRefuse.visibility = View.GONE
        } else {
            binding.btnRefuse.text = refuseText
        }
    }

    fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            confirm()
            dismiss()
        }

        binding.btnRefuse.setOnClickListener {
            refuse?.let{
                it()
            }
            dismiss()
        }
    }
}