package com.troia.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.troia.app.databinding.MemberItemBinding
import com.troia.app.fragment.FragmentMember
import com.troia.core.types.User

class MembersAdapter(
    private val membersList: ArrayList<User>,
    private var context: AppCompatActivity
): RecyclerView.Adapter<MembersAdapter.MembersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        return MembersViewHolder(MemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val item = membersList[position]
        holder.name.text = item.name
        holder.root.setOnClickListener {
            FragmentMember.newInstance(item).show(context.supportFragmentManager, FragmentMember.TAG)
        }
    }

    override fun getItemCount() = membersList.size

    fun setMembers(list: ArrayList<User>) {
        membersList.clear()
        membersList.addAll(list)
        notifyDataSetChanged()
    }

    class MembersViewHolder(view: MemberItemBinding): RecyclerView.ViewHolder(view.root) {
        val name = view.name
        val root = view.root
    }
}