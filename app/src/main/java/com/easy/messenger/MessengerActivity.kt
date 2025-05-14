package com.easy.messenger

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.easy.messenger.fragments.ChatListFragment

class MessengerActivity : AppCompatActivity() {
    val chatInfoMap: MutableMap<Int, Pair<Int, String>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (findViewById<FrameLayout>(R.id.fragment_container) != null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, ChatListFragment())
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.chats_list, ChatListFragment())
            }
        }
    }
}
