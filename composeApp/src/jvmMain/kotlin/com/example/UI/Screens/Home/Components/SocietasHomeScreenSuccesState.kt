package com.example.UI.Screens.Home.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.UI.Components.ChatMessage.SocietasChatMessage
import com.example.UI.Components.ChatMessage.SocietasChatMessageModel
import com.example.UI.Components.Input.SocietasMessageInput
import com.example.UI.Components.Item.SocietasItem
import com.example.UI.Screens.Home.MessageViewState
import com.example.UI.Screens.Home.SocietasHomeScreenModel

@Composable
fun SocietasHomeScreenSuccessState(
    model: SocietasHomeScreenModel,
    messages: List<SocietasChatMessageModel>,
    messageViewState: MessageViewState,
    onSendMessage: (agentId: String, message: String) -> Unit
) {
    var selectedAgent by remember { mutableStateOf(model.cBoard.firstOrNull()) }

    Row(modifier = Modifier.fillMaxSize()) {
        sideBar(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            agents = model.cBoard,
            enterprise = model.enterprise,
            selectedAgentName = selectedAgent?.name ?: "",
            onAgentSelected = { agent ->
                selectedAgent = agent
            }
        )

        chatPanel(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f),
            model = model,
            messages = messages,
            messageViewState = messageViewState,
            onSendMessage = {
                selectedAgent?.let { agent ->
                    onSendMessage(agent.name, it)
                }
            }
        )
    }
}

@Composable
private fun sideBar(
    modifier: Modifier = Modifier,
    agents: Array<SocietasHomeScreenModel.Agent>,
    enterprise: String,
    selectedAgentName: String,
    onAgentSelected: (SocietasHomeScreenModel.Agent) -> Unit
) {
    Surface(
        color = Color(0xFF353755),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 36.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = enterprise,
                color = Color.Gray,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "OPERATIONS",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            agents.forEach { agent ->
                SocietasItem(
                    text = agent.name,
                    isSelected = (selectedAgentName == agent.name),
                    onClick = { onAgentSelected(agent) }
                )
            }

        }
    }
}

@Composable
private fun chatPanel(
    modifier: Modifier = Modifier,
    model: SocietasHomeScreenModel,
    messages: List<SocietasChatMessageModel>,
    messageViewState: MessageViewState,
    onSendMessage: (String) -> Unit
) {
    Column(
        modifier = modifier
            .background(Color(0xFF353739))
            .padding(16.dp)
    ) {
        chatPanelHeader(model.userName)
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (messageViewState) {
                is MessageViewState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MessageViewState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { message ->
                            SocietasChatMessage(message = message)
                        }
                    }
                }
                is MessageViewState.Error -> {
                    Text(
                        text = messageViewState.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is MessageViewState.Idle -> { /* Do nothing */ }
            }
        }
        SocietasMessageInput(
            modifier = Modifier.fillMaxWidth(),
            onSendMessage = onSendMessage
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