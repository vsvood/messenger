package com.easy.messenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.easy.messenger.R
import com.easy.messenger.adapters.ChatListAdapter
import com.easy.messenger.network.NetworkClient

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private lateinit var recyclerChats: RecyclerView
    private lateinit var chatsAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsAdapter = ChatListAdapter(mutableListOf(), this)
        displayChats(view)
        fetchChats()
    }

    private fun displayChats(view: View) {
        recyclerChats = view.findViewById(R.id.chats_recycler)

        recyclerChats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerChats.adapter = chatsAdapter

        view.findViewById<FloatingActionButton>(R.id.add_chat_button).setOnClickListener {
            showAddChatDialog()
        }
    }

    private fun showAddChatDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_chat, null)

        val chatName = dialogView.findViewById<EditText>(R.id.chat_name)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("New chat")
            .setView(dialogView)
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.add_chat_dialog_background))
            .setPositiveButton("Create") { _, _ ->
                val name = chatName.text.toString().trim()
                if (name.isNotEmpty()) {
                    createChat(name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createChat(name: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.chatApiService.createChat(name)
                }
                chatsAdapter.updateChats(response.chats)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchChats() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.chatApiService.getChats()
                }

                if (!isAdded) return@launch

                chatsAdapter.updateChats(response.chats)
            } catch (e: Exception) {
                if (!isAdded) return@launch

                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
