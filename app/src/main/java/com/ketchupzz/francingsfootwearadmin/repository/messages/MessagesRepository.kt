package com.ketchupzz.francingsfootwearadmin.repository.messages

import com.ketchupzz.francingsfootwearadmin.model.messages.Messages
import com.ketchupzz.francingsfootwearadmin.utils.UiState

interface MessagesRepository {
    fun sendMessage(messages: Messages,result : (UiState<String>) -> Unit)
    fun getAllMyMessages(result : (UiState<List<Messages>>) -> Unit)
}