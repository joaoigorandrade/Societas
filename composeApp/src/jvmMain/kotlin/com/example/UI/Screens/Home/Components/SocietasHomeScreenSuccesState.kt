package com.example.UI.Screens.Home.Components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.UI.Components.ChatMessage.SocietasChatMessageModel
import com.example.UI.Components.Item.SocietasItem
import com.example.UI.Screens.Home.Components.ChatPanel.ChatPanel
import com.example.UI.Screens.Home.Components.ChatPanel.ChatPanelViewState
import com.example.UI.Screens.Home.SocietasHomeScreenModel

@Composable
fun SocietasHomeScreenSuccessState(
    model: SocietasHomeScreenModel,
    messages: List<SocietasChatMessageModel>,
    messageViewState: ChatPanelViewState,
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

        ChatPanel(
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