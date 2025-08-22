package com.example.UI.Screens.Home.Components.ChatPanel

import com.example.Domain.UseCase.Message.GetMessagesUseCase
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
    private val getMessagesUseCase: GetMessagesUseCase
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _chatId = MutableStateFlow("")
    val chatId = _chatId.asStateFlow()

    private val _messages = MutableStateFlow<List<SocietasChatModel.Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _messageState = MutableStateFlow<ChatPanelViewState>(ChatPanelViewState.Idle)
    val messageState = _messageState.asStateFlow()

    fun loadMessages(userId: String, agentId: String) {
        scope.launch {
            _messageState.value = ChatPanelViewState.Loading
            when (val result = getMessagesUseCase.execute(userId, agentId)) {
                is NetworkResult.Success -> {
                    _chatId.value = result.data.chatId
                    _messages.value = result.data.messages
                    _messageState.value = ChatPanelViewState.Success
                }
                is NetworkResult.Error -> {
                    _messageState.value = ChatPanelViewState.Error(result.message)
                }
                else -> { /* No-op for loading state */ }
            }
        }
    }

    fun sendMessage(
        userId: String,
        chatId: String,
        message: String
    ) {
        scope.launch {
            val newMessage = SocietasChatModel.Message(text = message, author = userId)
            _messages.value = _messages.value + newMessage

            when (val result = sendMessageUseCase.execute(userId, chatId, message)) {
                is NetworkResult.Success -> {
                    // Message sent successfully.
                }
                is NetworkResult.Error -> {
                    _messages.value = _messages.value.filterNot { it == newMessage }
                    _messageState.value = ChatPanelViewState.Error("Failed to send message: ${result.message}")
                }
                else -> { /* No-op */ }
            }
        }
    }
}
