package com.example.UI.Screens.Home.Components.ChatPanel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.UI.Components.ChatMessage.SocietasChatMessage
import com.example.UI.Components.Input.SocietasMessageInput
import com.example.UI.Screens.Home.SocietasHomeScreenModel
import org.koin.compose.koinInject

@Composable
fun ChatPanel(
    modifier: Modifier = Modifier,
    model: SocietasHomeScreenModel,
    selectedAgent: SocietasHomeScreenModel.Agent?
) {

    val viewModel: ChatPanelViewModel = koinInject()
    val messageState by viewModel.messageState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val chatId by viewModel.chatId.collectAsState()

    LaunchedEffect(selectedAgent) {
        selectedAgent?.let {
            viewModel.loadMessages(
                "6qDU3re3ejbpIdman0WL",
                it.id
            )
        }
    }

    Column(
        modifier = modifier
            .background(Color(0xFF353739))
            .padding(16.dp)
    ) {
        chatPanelHeader(model.userName)
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val state = messageState) {
                is ChatPanelViewState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ChatPanelViewState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { message ->
                            SocietasChatMessage(message = message)
                        }
                    }
                }
                is ChatPanelViewState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ChatPanelViewState.Idle -> { /* Do nothing */ }
            }
        }
        SocietasMessageInput(
            modifier = Modifier.fillMaxWidth(),
            onSendMessage = {
                viewModel.sendMessage(
                    "6qDU3re3ejbpIdman0WL",
                    chatId = chatId,
                    message = it)
            }
        )
    }
}

@Composable
private fun chatPanelHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(userName, color = Color.White)
        Row() {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Account",
                tint = Color.Gray
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.Gray
            )
        }
    }
}