package com.easy.messenger.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.easy.messenger.databinding.FragmentNewMessageBinding
import com.easy.messenger.R

interface OnMessageSendListener {
    fun onMessageSend(text: String)
}

class NewMessageEnterFragment : Fragment(R.layout.fragment_new_message) {

    private var _binding: FragmentNewMessageBinding? = null
    private val binding get() = _binding!!

    private val listener: OnMessageSendListener?
        get() = parentFragment as? OnMessageSendListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewMessageBinding.bind(view)

        setupSendButton()
        restoreSavedState(savedInstanceState)
    }

    private fun setupSendButton() {
        binding.messageSendButton.setOnClickListener {
            val message = binding.newMessageText.text.toString().trim()
            if (message.isNotEmpty()) {
                listener?.onMessageSend(message)
                clearMessageInput()
            }
        }
    }

    private fun clearMessageInput() {
        binding.newMessageText.text.clear()
    }

    private fun restoreSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(SAVED_MESSAGE_KEY)?.let { savedMessage ->
            binding.newMessageText.setText(savedMessage)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_MESSAGE_KEY, binding.newMessageText.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SAVED_MESSAGE_KEY = "saved_message_text"

        fun newInstance() = NewMessageEnterFragment()
    }
}