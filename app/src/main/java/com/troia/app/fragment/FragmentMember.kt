package com.troia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.troia.app.R
import com.troia.app.databinding.MemberFragmentBinding
import com.troia.core.repository.CaixinhaRepository
import com.troia.core.types.User
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.UserUtils.clean
import java.io.Serializable


class FragmentMember: DialogFragment() {
    companion object {
        val USER = "keyUSER"
        val TAG = "fragmentMember"
        fun newInstance(user: User?): FragmentMember {
            val bundle = bundleOf(
                USER to user as Serializable
            )

            return FragmentMember().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var binding: MemberFragmentBinding
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MemberFragmentBinding.inflate(layoutInflater)
        user = arguments?.get(USER) as User
        childFragmentManager.beginTransaction().apply {
            replace(binding.framePurchases.id, FragmentHistory.newInstance(user))
            commit()
        }
        return binding.root
    }

    private fun updateMemberDebt() {
        val purchases = user?.email?.let { CaixinhaRepository.getPurchases(it.clean()) }
        val debt = purchases?.sumOf { it.debt } ?: 0.0
        binding.memberDebt.text = getString(com.troia.core.R.string.member_debpt, String.format("R$ %.2f", debt))
    }

    private fun setInfo() {
        binding.name.text = user?.name
        binding.adminSwitch.isChecked = user?.admin ?: false
        updateMemberDebt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInfo()

        binding.adminSwitch.setOnCheckedChangeListener { _, isChecked ->
            user?.let { user ->
                user.admin = isChecked
                FirebaseUtils.registerUser(user)
            }
        }

        binding.addPayment.setOnClickListener { _ ->
            user?.email?.clean()?.let { email ->
                val list = CaixinhaRepository.getPurchases(email).apply { sortBy { it.date } }
                var value = binding.editValue.text.toString().toDouble()
                list.forEach {
                    value = it.debbit(value)
                    FirebaseUtils.savePurchase(email, it)
                }
            }
            updateMemberDebt()
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}