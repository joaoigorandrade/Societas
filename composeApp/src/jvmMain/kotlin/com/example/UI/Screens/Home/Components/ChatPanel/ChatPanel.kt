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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.UI.Components.ChatMessage.SocietasChatMessage
import com.example.UI.Components.Input.SocietasMessageInput
import com.example.UI.DesignSystem.societasDesignSystem
import com.example.UI.DesignSystem.SocietasIcons
import com.example.UI.Screens.Home.SocietasHomeScreenModel
import org.koin.compose.koinInject

@Composable
fun ChatPanel(
    modifier: Modifier = Modifier,
    model: SocietasHomeScreenModel,
    selectedAgent: SocietasHomeScreenModel.Agent?
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing

    val viewModel: ChatPanelViewModel = koinInject()
    val messageState by viewModel.messageState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val chatId by viewModel.chatId.collectAsState()
    val isSendingMessage by viewModel.isSendingMessage.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(selectedAgent) {
        selectedAgent?.let {
            viewModel.loadMessages(
                "6qDU3re3ejbpIdman0WL",
                it.id
            )
        }
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(spacing.screenPadding)
    ) {
        chatPanelHeader(model.userName)
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val state = messageState) {
                is ChatPanelViewState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is ChatPanelViewState.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacing.itemSpacing)
                    ) {
                        items(messages) { message ->
                            SocietasChatMessage(message = message)
                        }
                    }
                }
                is ChatPanelViewState.Error -> {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.Error,
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
                    agentId = selectedAgent?.id ?: "",
                    message = it
                )
            },
            isSending = isSendingMessage
        )
    }
}

@Composable
private fun chatPanelHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.screenPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row {
            Icon(
                imageVector = SocietasIcons.PERSON.filled,
                contentDescription = "User Account",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(spacing.sm))
            Icon(
                imageVector = SocietasIcons.ADD.filled,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}