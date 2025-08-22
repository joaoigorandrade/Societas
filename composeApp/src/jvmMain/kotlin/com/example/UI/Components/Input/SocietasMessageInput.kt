package com.example.UI.Components.Input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import com.example.UI.DesignSystem.Components.SocietasTextField
import com.example.UI.DesignSystem.Components.SocietasTextFieldSize
import com.example.UI.DesignSystem.Components.SocietasTextFieldVariant
import com.example.UI.DesignSystem.SocietasIcons
import com.example.UI.DesignSystem.societasDesignSystem

@Composable
fun SocietasMessageInput(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors
    
    var text by remember { mutableStateOf("") }
    val canSend = text.isNotBlank()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        verticalAlignment = Alignment.Bottom
    ) {
        SocietasTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier.weight(1f),
            placeholder = "Ask the CFO about revenue, costs, or projections...",
            variant = SocietasTextFieldVariant.OUTLINED,
            size = SocietasTextFieldSize.MEDIUM,
            keyboardType = KeyboardType.Text,
            maxLines = 3,
            singleLine = false
        )

        IconButton(
            onClick = {
                if (canSend) {
                    onSendMessage(text)
                    text = ""
                }
            },
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