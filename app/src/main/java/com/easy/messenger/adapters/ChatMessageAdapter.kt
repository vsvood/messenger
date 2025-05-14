package com.easy.messenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easy.messenger.databinding.FragmentMessageBarBinding
import com.easy.messenger.model.entities.MessageEntity

class ChatMessageAdapter(
    private val messages: MutableList<MessageEntity> = mutableListOf()
) : RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>() {

    // ViewHolder using View Binding
    inner class MessageViewHolder(
        private val binding: FragmentMessageBarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageEntity) {
            with(binding) {
                messageText.text = message.text
                // Add any additional view bindings here if needed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = FragmentMessageBarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<MessageEntity>) {
        messages.apply {
            clear()
            addAll(newMessages)
        }
        notifyDataSetChanged()
    }
}