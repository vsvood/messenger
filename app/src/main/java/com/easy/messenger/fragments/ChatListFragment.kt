package com.easy.messenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.easy.messenger.databinding.FragmentChatListBinding
import com.easy.messenger.R
import com.easy.messenger.adapters.ChatListAdapter
import com.easy.messenger.network.NetworkClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatsAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentChatListBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        loadChats()
    }

    private fun setupRecyclerView() {
        chatsAdapter = ChatListAdapter(mutableListOf(), this)
        with(binding.chatsRecycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.addChatButton.setOnClickListener {
            showCreateChatDialog()
        }
    }

    private fun showCreateChatDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_chat, null)

        val editText = dialogView.findViewById<android.widget.EditText>(R.id.chat_name)

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.new_chat_title))
            setView(dialogView)
            setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.add_chat_dialog_background))
            setPositiveButton(getString(R.string.create)) { _, _ ->
                editText.text.toString().trim().takeIf { it.isNotEmpty() }?.let { name ->
                    createNewChat(name)
                }
            }
            setNegativeButton(getString(R.string.cancel), null)
            show()
        }
    }

    private fun createNewChat(name: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.chatApiService.createChat(name)
                }
                chatsAdapter.updateChats(response.chats)
            } catch (e: Exception) {
                showError(e.message)
            }
        }
    }

    private fun loadChats() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.chatApiService.getChats()
                }

                if (!isAdded) return@launch

                chatsAdapter.updateChats(response.chats)
            } catch (e: Exception) {
                if (!isAdded) return@launch
                showError(e.message)
            }
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(
            requireContext(),
            getString(R.string.error_template, message),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ChatListFragment()
    }
}