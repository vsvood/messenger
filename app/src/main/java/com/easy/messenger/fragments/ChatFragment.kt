package com.easy.messenger.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.easy.messenger.databinding.FragmentChatBinding
import com.easy.messenger.MessengerActivity
import com.easy.messenger.adapters.ChatMessageAdapter
import com.easy.messenger.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.easy.messenger.R

class ChatFragment : Fragment(R.layout.fragment_chat), OnMessageSendListener {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private var chatId: Int = -1
    private var chatName: String = ""
    private var scrollPosition: Int = 0

    private lateinit var messagesAdapter: ChatMessageAdapter

    companion object {
        private const val CHAT_ID = "chat_id"
        private const val CHAT_NAME = "chat_name"
        private const val SCROLL_POSITION = "scroll_position"

        fun newInstance(chatId: Int, chatName: String) = ChatFragment().apply {
            arguments = Bundle().apply {
                putInt(CHAT_ID, chatId)
                putString(CHAT_NAME, chatName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatId = it.getInt(CHAT_ID)
            chatName = it.getString(CHAT_NAME).orEmpty()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatBinding.bind(view)

        setupRecyclerView()
        setupNewMessageFragment()
        setupBackPressHandler()
        loadMessages()
    }

    private fun setupRecyclerView() {
        messagesAdapter = ChatMessageAdapter()
        with(binding.messagesRecycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messagesAdapter
        }
    }

    private fun setupNewMessageFragment() {
        childFragmentManager.commit {
            replace(R.id.new_message_fragment, NewMessageEnterFragment())
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.chatApiService.getMessages(chatId)
                }

                if (!isAdded) return@launch

                messagesAdapter.updateMessages(response.messages)
                scrollToSavedPosition()
            } catch (e: Exception) {
                if (!isAdded) return@launch
                showError(e.message)
            }
        }
    }

    private fun scrollToSavedPosition() {
        val scrollTo = (activity as? MessengerActivity)
            ?.chatInfoMap
            ?.get(chatId)
            ?.first ?: 0

        binding.messagesRecycler.post {
            binding.messagesRecycler.scrollBy(0, scrollTo)
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(
            requireContext(),
            getString(R.string.error_template, message),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onMessageSend(text: String) {
        sendMessage(text)
    }

    private fun sendMessage(messageText: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.chatApiService.createMessage(chatId, messageText)
                }
                messagesAdapter.updateMessages(response.messages)
                binding.messagesRecycler.scrollToPosition(response.messages.size - 1)
            } catch (e: Exception) {
                showError(e.message)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION, binding.messagesRecycler.computeVerticalScrollOffset())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            scrollPosition = it.getInt(SCROLL_POSITION, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}