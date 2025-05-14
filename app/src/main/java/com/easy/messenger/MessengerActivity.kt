package com.easy.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.easy.messenger.databinding.ActivityMainBinding
import com.easy.messenger.fragments.ChatListFragment

class MessengerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val chatInfoMap = mutableMapOf<Int, Pair<Int, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeFragments()
    }

    private fun initializeFragments() {
        val fragment = ChatListFragment.newInstance()

        supportFragmentManager.commit {
            when {
                hasFragmentContainer() -> replace(R.id.fragment_container, fragment)
                hasChatsListContainer() -> replace(R.id.chats_list, fragment)
            }
        }
    }

    private fun hasFragmentContainer() = binding.root.findViewById<View>(R.id.fragment_container) != null
    private fun hasChatsListContainer() = binding.root.findViewById<View>(R.id.chats_list) != null

    companion object {
        fun newIntent(context: Context) = Intent(context, MessengerActivity::class.java)
    }
}