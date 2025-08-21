package com.example.UI.Screens.Home.Components.ChatPanel

import com.example.Domain.UseCase.Message.GetMessagesUseCase
import com.example.Domain.UseCase.Message.SendMessageUseCase
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatMessageModel
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

    private val _messages = MutableStateFlow<List<SocietasChatMessageModel>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _messageState = MutableStateFlow<ChatPanelViewState>(ChatPanelViewState.Idle)
    val messageState = _messageState.asStateFlow()

    fun loadMessages(userId: String, agentId: String) {
        scope.launch {
            _messageState.value = ChatPanelViewState.Loading
            when (val result = getMessagesUseCase.execute(userId, agentId)) {
                is NetworkResult.Success -> {
                    _messages.value = result.data
                    _messageState.value = ChatPanelViewState.Success
                }
                is NetworkResult.Error -> {
                    _messageState.value = ChatPanelViewState.Error(result.message)
                }
                else -> { /* No-op for loading state */ }
            }
        }
    }

    fun sendMessage(agentId: String, message: String) {
        scope.launch {
            val result = sendMessageUseCase.execute(agentId, message)
            println("Message send result: $result")
            // Here you would typically add the new message to the _messages StateFlow
        }
    }
}
