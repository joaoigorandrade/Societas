import com.example.Domain.UseCase.Home.SocietasHomeUseCase
import com.example.Domain.UseCase.Message.GetMessagesUseCase
import com.example.Domain.UseCase.Message.SendMessageUseCase
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatMessageModel
import com.example.UI.Screens.Home.MessageViewState
import com.example.UI.Screens.SocietasViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SocietasHomeScreenViewModel(
    private val homeUseCase: SocietasHomeUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<SocietasViewState>(SocietasViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<SocietasChatMessageModel>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _messageState = MutableStateFlow<MessageViewState>(MessageViewState.Idle)
    val messageState = _messageState.asStateFlow()

    init {
        loadData()
        loadMessages("user-12345", "chat-67890") // Fake IDs
    }

    fun sendMessage(agentId: String, message: String) {
        scope.launch {
            val result = sendMessageUseCase.execute(agentId, message)
            println("Message send result: $result")
            // Here you would typically add the new message to the _messages StateFlow
        }
    }

    private fun loadMessages(userId: String, chatId: String) {
        scope.launch {
            _messageState.value = MessageViewState.Loading
            when (val result = getMessagesUseCase.execute(userId, chatId)) {
                is NetworkResult.Success -> {
                    _messages.value = result.data
                    _messageState.value = MessageViewState.Success
                }
                is NetworkResult.Error -> {
                    _messageState.value = MessageViewState.Error(result.message)
                }
                else -> { /* No-op for loading state */ }
            }
        }
    }

    private fun loadData() {
        scope.launch {
            _uiState.value = homeUseCase.execute()
        }
    }
}