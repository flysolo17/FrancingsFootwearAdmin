package com.ketchupzz.francingsfootwearadmin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwearadmin.model.messages.Messages
import com.ketchupzz.francingsfootwearadmin.repository.messages.MessagesRepository
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(val messagesRepository: MessagesRepository) : ViewModel() {
    private val _messages = MutableLiveData<UiState<List<Messages>>>()
    val messages : LiveData<UiState<List<Messages>>> get() = _messages
    fun getAllMessages() {
        messagesRepository.getAllMyMessages {
            _messages.value = it
        }
    }
}