package com.easy.messenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.easy.messenger.DEFAULT_CHAT_IC_LINK
import com.easy.messenger.databinding.FragmentChatBarBinding
import com.easy.messenger.model.entities.ChatEntity
import com.easy.messenger.fragments.ChatFragment
import com.easy.messenger.R

class ChatListAdapter(
    private var chats: MutableList<ChatEntity> = mutableListOf(),
    private val hostFragment: Fragment
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    // Using View Binding for cleaner view access
    inner class ChatViewHolder(
        private val binding: FragmentChatBarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: ChatEntity) {
            with(binding) {
                chatName.text = chat.name

                Glide.with(hostFragment)
                    .load(DEFAULT_CHAT_IC_LINK)
                    .into(chatIcon)

                setupChatClick(chat)
            }
        }

        private fun FragmentChatBarBinding.setupChatClick(chat: ChatEntity) {
            chatBar.setOnClickListener {
                navigateToChatFragment(chat)
            }
        }

        private fun navigateToChatFragment(chat: ChatEntity) {
            val fragmentManager = hostFragment.requireActivity().supportFragmentManager
            val chatFragment = ChatFragment.newInstance(chat.id, chat.name)

            fragmentManager.commit {
                val containerId = if (hasFragmentContainer()) {
                    R.id.fragment_container
                } else {
                    R.id.current_chat
                }

                replace(containerId, chatFragment)

                if (hasFragmentContainer()) {
                    addToBackStack(null)
                }
            }
        }

        private fun hasFragmentContainer() =
            hostFragment.requireActivity().findViewById<ViewGroup>(R.id.fragment_container) != null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentChatBarBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount(): Int = chats.size

    fun updateChats(newChats: List<ChatEntity>) {
        chats = newChats.toMutableList()
        notifyDataSetChanged()
    }
}