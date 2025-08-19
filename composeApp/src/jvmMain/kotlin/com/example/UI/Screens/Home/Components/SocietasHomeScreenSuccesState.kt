package com.example.UI.Screens.Home.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

@Composable
fun SocietasHomeScreenSuccessState() {
    Row(modifier = Modifier.fillMaxSize()) {
        sideBar(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )

        chatPanel(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
        )
    }
}

@Composable
private fun sideBar(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf("Financial") }

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
                text = "Command Center",
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

            val items = listOf(
                "Chief Financial Officer",
                "Chief Marketing Officer",
                "Head of Engineering"
            )

            items.forEach { itemText ->
                SocietasItem(
                    text = itemText,
                    isSelected = (selectedItem == itemText),
                    onClick = {
                        selectedItem = itemText
                    }
                )
            }

        }
    }
}

@Composable
private fun chatPanel(modifier: Modifier = Modifier) {
    val sampleMessages = listOf(
        SocietasChatMessageModel(
            "What was our operating margin for Q2 2025?",
            "User"
        ),
        SocietasChatMessageModel(
            "Our operating margin for Q2 2025 was 18.7%. This is an increase from 16.2% in Q1 2025, primarily driven by reduced operational expenditure.",
            "CFO"
        ),
        SocietasChatMessageModel(
            "Show me the breakdown of that OpEx reduction.",
            "User"
        )
    )

    Column(
        modifier = modifier
            .background(Color(0xFF353739))
            .padding(16.dp)
    ) {
        chatPanelHeader()
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adds space between bubbles
        ) {
            items(sampleMessages) { message ->
                SocietasChatMessage(message = message)
            }
        }
        SocietasMessageInput(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun chatPanelHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Header Area", color = Color.White)
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