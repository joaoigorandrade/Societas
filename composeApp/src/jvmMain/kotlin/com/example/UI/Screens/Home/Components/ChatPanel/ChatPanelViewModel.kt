package com.example.UI.Screens.Home.Components.ChatPanel

import com.example.Domain.Models.Auth.UserSession
import com.example.Domain.UseCase.Message.GetMessagesFromFirestoreUseCase
import com.example.Domain.UseCase.Message.SendMessageUseCase
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatPanelViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesFromFirestoreUseCase: GetMessagesFromFirestoreUseCase
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _chatId = MutableStateFlow("")
    val chatId = _chatId.asStateFlow()

    private val _messages = MutableStateFlow<List<SocietasChatModel.Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _messageState = MutableStateFlow<ChatPanelViewState>(ChatPanelViewState.Idle)
    val messageState = _messageState.asStateFlow()

    private val _isSendingMessage = MutableStateFlow(false)
    val isSendingMessage = _isSendingMessage.asStateFlow()

    fun loadMessages(agentId: String) {
        val userId = UserSession.getUserId() ?: return
        scope.launch {
            getMessagesFromFirestoreUseCase.execute(userId, agentId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _chatId.value = result.data.chatId
                            _messages.value = result.data.messages
                            _messageState.value = ChatPanelViewState.Success
                        }
                        is NetworkResult.Error -> {
                            _messageState.value = ChatPanelViewState.Error(result.message)
                        }
                        is NetworkResult.Loading -> {
                            _messageState.value = ChatPanelViewState.Loading
                        }
                    }
                }
        }
    }

    fun sendMessage(
        chatId: String,
        agentId: String,
        message: String
    ) {
        val userId = UserSession.getUserId() ?: return
        scope.launch {
            _isSendingMessage.value = true
            val newMessage = SocietasChatModel.Message(text = message, author = userId)
            _messages.value = _messages.value + newMessage

            try {
                when (val result = sendMessageUseCase.execute(chatId, agentId, message)) {
                    is NetworkResult.Success -> { }
                    is NetworkResult.Error -> {
                        _messages.value = _messages.value.filterNot { it == newMessage }
                        _messageState.value = ChatPanelViewState.Error("Failed to send message: ${result.message}")
                    }
                    else -> { }
                }
            } finally {
                _isSendingMessage.value = false
            }
        }
    }
}
