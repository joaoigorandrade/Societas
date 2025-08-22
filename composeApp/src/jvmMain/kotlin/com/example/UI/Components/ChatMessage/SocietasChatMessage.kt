package com.example.UI.Components.ChatMessage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.Components.SocietasAvatar
import com.example.UI.DesignSystem.Components.SocietasAvatarSize
import com.example.UI.DesignSystem.Components.SocietasAvatarVariant
import com.example.UI.DesignSystem.Components.SocietasCard
import com.example.UI.DesignSystem.Components.SocietasCardSize
import com.example.UI.DesignSystem.Components.SocietasCardVariant
import com.example.UI.DesignSystem.societasDesignSystem
import kotlinx.coroutines.delay


@Composable
fun SocietasChatMessage(
    message: SocietasChatModel.Message,
    modifier: Modifier = Modifier,
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors
    
    val isUserMessage = message.author == "6qDU3re3ejbpIdman0WL"
    
    // Animation state
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "message_fade_in"
    )
    
    LaunchedEffect(Unit) {
        delay(100) // Small delay for staggered animation
        isVisible = true
    }
    
    val arrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    val avatarAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(horizontal = spacing.md, vertical = spacing.xs),
        horizontalAlignment = avatarAlignment
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = arrangement,
            verticalAlignment = Alignment.Bottom
        ) {
            // Avatar for AI messages (left side)
            if (!isUserMessage) {
                SocietasAvatar(
                    size = SocietasAvatarSize.SMALL,
                    variant = SocietasAvatarVariant.INITIALS,
                    initials = "AI",
                    backgroundColor = colors.Secondary,
                    contentColor = colors.OnSecondary,
                    modifier = Modifier.padding(end = spacing.sm)
                )
            }
            
            // Message bubble
            SocietasCard(
                variant = if (isUserMessage) SocietasCardVariant.FILLED else SocietasCardVariant.ELEVATED,
                size = SocietasCardSize.SMALL,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Column(
                    modifier = Modifier.padding(spacing.md)
                ) {
                    if (!isUserMessage) {
                        Text(
                            text = "AI Assistant",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = spacing.xs)
                        )
                    }
                    
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isUserMessage) {
                            colors.OnPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
            
            // Avatar for user messages (right side)
            if (isUserMessage) {
                SocietasAvatar(
                    size = SocietasAvatarSize.SMALL,
                    variant = SocietasAvatarVariant.INITIALS,
                    initials = "U",
                    backgroundColor = colors.Primary,
                    contentColor = colors.OnPrimary,
                    modifier = Modifier.padding(start = spacing.sm)
                )
            }
        }
    }
}