package com.example.UI.Components.ChatMessage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SocietasChatMessage(
    message: SocietasChatMessageModel,
    modifier: Modifier = Modifier,
) {

    val isUserMessage = message.author == "6qDU3re3ejbpIdman0WL"

    val arrangement = if (isUserMessage) Arrangement.End else Arrangement.Start

    val bubbleColor = if (isUserMessage) Color(0xFF4752b2) else Color(0xFF4B4D4F)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = bubbleColor
        ) {
            Text(
                text = message.text,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}