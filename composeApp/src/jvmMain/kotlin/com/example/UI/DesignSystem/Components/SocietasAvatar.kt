package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasAvatarSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

enum class SocietasAvatarVariant {
    IMAGE,
    INITIALS,
    ICON
}

@Composable
fun SocietasAvatar(
    modifier: Modifier = Modifier,
    size: SocietasAvatarSize = SocietasAvatarSize.MEDIUM,
    variant: SocietasAvatarVariant = SocietasAvatarVariant.INITIALS,
    imageContent: Painter? = null,
    initials: String = "",
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    iconContent: @Composable (() -> Unit)? = null
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors
    
    val avatarSize: Dp = when (size) {
        SocietasAvatarSize.SMALL -> spacing.avatarSizeSmall
        SocietasAvatarSize.MEDIUM -> spacing.avatarSizeMedium
        SocietasAvatarSize.LARGE -> spacing.avatarSizeLarge
        SocietasAvatarSize.EXTRA_LARGE -> spacing.avatarSizeExtraLarge
    }
    
    val fontSize = when (size) {
        SocietasAvatarSize.SMALL -> 12.sp
        SocietasAvatarSize.MEDIUM -> 16.sp
        SocietasAvatarSize.LARGE -> 20.sp
        SocietasAvatarSize.EXTRA_LARGE -> 24.sp
    }
    
    val bgColor = backgroundColor ?: colors.Primary
    val textColor = contentColor ?: colors.OnPrimary
    
    Box(
        modifier = modifier
            .size(avatarSize)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        when (variant) {
            SocietasAvatarVariant.IMAGE -> {
                imageContent?.let { painter ->
                    Image(
                        painter = painter,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(avatarSize),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            SocietasAvatarVariant.INITIALS -> {
                Text(
                    text = initials.take(2).uppercase(),
                    color = textColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium
                )
            }
            SocietasAvatarVariant.ICON -> {
                iconContent?.invoke()
            }
        }
    }
}

@Composable
fun SocietasAvatarGroup(
    modifier: Modifier = Modifier,
    avatars: List<@Composable () -> Unit>,
    maxVisible: Int = 3,
    overlapOffset: Dp = (-8).dp
) {
    val spacing = societasDesignSystem().spacing
    
    Box(modifier = modifier) {
        avatars.take(maxVisible).forEachIndexed { index, avatar ->
            Box(
                modifier = Modifier.offset(x = (index * (spacing.avatarSizeMedium.value + overlapOffset.value)).dp)
            ) {
                avatar()
            }
        }
        
        if (avatars.size > maxVisible) {
            Box(
                modifier = Modifier.offset(
                    x = (maxVisible * (spacing.avatarSizeMedium.value + overlapOffset.value)).dp
                )
            ) {
                SocietasAvatar(
                    size = SocietasAvatarSize.MEDIUM,
                    variant = SocietasAvatarVariant.INITIALS,
                    initials = "+${avatars.size - maxVisible}",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
