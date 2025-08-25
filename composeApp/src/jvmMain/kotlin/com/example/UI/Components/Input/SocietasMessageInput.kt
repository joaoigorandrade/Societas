package com.example.UI.Components.Input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.Components.SocietasTextField
import com.example.UI.DesignSystem.Components.SocietasTextFieldSize
import com.example.UI.DesignSystem.Components.SocietasTextFieldVariant
import com.example.UI.DesignSystem.SocietasIcons
import com.example.UI.DesignSystem.societasDesignSystem

@Composable
fun SocietasMessageInput(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit,
    isSending: Boolean = false
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors

    var text by remember { mutableStateOf("") }
    val canSend = text.isNotBlank() && !isSending

    fun handleSendMessage() {
        if (canSend) {
            onSendMessage(text)
            text = ""
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocietasTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            modifier = Modifier
                .weight(1f)
                .onKeyEvent {
                    if (it.key == Key.Enter && it.type == KeyEventType.KeyUp) {
                        text.removeSuffix("\n")
                        handleSendMessage()
                    }
                    false
                },
            placeholder = "Ask the CFO about revenue, costs, or projections...",
            variant = SocietasTextFieldVariant.OUTLINED,
            size = SocietasTextFieldSize.MEDIUM,
            keyboardType = KeyboardType.Text,
            maxLines = 3,
            singleLine = false,
            enabled = !isSending
        )

        if (isSending) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = colors.Primary
            )
        } else {
            IconButton(
                onClick = ::handleSendMessage,
                enabled = canSend
            ) {
                Icon(
                    imageVector = SocietasIcons.SEND.filled,
                    contentDescription = "Send Message",
                    tint = if (canSend) {
                        colors.Primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    }
                )
            }
        }
    }
}
