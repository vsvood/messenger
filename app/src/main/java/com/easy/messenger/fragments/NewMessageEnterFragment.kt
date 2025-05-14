package com.easy.messenger.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.easy.messenger.R

interface OnMessageSendListener {
    fun onMessageSend(text: String)
}

class NewMessageEnterFragment : Fragment(R.layout.fragment_new_message) {

    private lateinit var chatMessage: EditText
    private lateinit var sendButton: Button
    private var listener: OnMessageSendListener? = null

    companion object {
        const val NEW_MESSAGE_TEXT = "new_message_text"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatMessage = view.findViewById(R.id.new_message_text)
        sendButton = view.findViewById(R.id.message_send_button)
        listener = parentFragment as? OnMessageSendListener

        sendButton.setOnClickListener {
            val message = chatMessage.text.toString()
            if (message.isNotEmpty()) {
                listener?.onMessageSend(message)
                chatMessage.setText("")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(NEW_MESSAGE_TEXT, chatMessage.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?. let {
            chatMessage.setText(it.getString(NEW_MESSAGE_TEXT))
        }
    }
}
