package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasListItemVariant {
    ONE_LINE,
    TWO_LINE,
    THREE_LINE
}

@Composable
fun SocietasListItem(
    modifier: Modifier = Modifier,
    variant: SocietasListItemVariant = SocietasListItemVariant.ONE_LINE,
    headlineText: String,
    supportingText: String? = null,
    overlineText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors
    
    val backgroundColor = when {
        selected -> colors.LightMint
        else -> Color.Transparent
    }
    
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        selected -> colors.Primary
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    val listItemPadding = PaddingValues(
        horizontal = spacing.md,
        vertical = when (variant) {
            SocietasListItemVariant.ONE_LINE -> spacing.sm
            SocietasListItemVariant.TWO_LINE -> spacing.md
            SocietasListItemVariant.THREE_LINE -> spacing.lg
        }
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(spacing.borderRadiusMedium))
            .background(backgroundColor)
            .then(
                if (onClick != null && enabled) {
                    Modifier.clickable { onClick() }
                } else Modifier
            )
            .padding(listItemPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading content
            leadingContent?.let { content ->
                Box {
                    content()
                }
            }
            
            // Text content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                overlineText?.let { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Text(
                    text = headlineText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                    maxLines = when (variant) {
                        SocietasListItemVariant.ONE_LINE -> 1
                        SocietasListItemVariant.TWO_LINE -> if (supportingText != null) 1 else 2
                        SocietasListItemVariant.THREE_LINE -> 2
                    },
                    overflow = TextOverflow.Ellipsis
                )
                
                supportingText?.let { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.7f),
                        maxLines = when (variant) {
                            SocietasListItemVariant.ONE_LINE -> 0
                            SocietasListItemVariant.TWO_LINE -> 1
                            SocietasListItemVariant.THREE_LINE -> 2
                        },
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Trailing content
            trailingContent?.let { content ->
                Box {
                    content()
                }
            }
        }
    }
}

@Composable
fun SocietasIconListItem(
    modifier: Modifier = Modifier,
    variant: SocietasListItemVariant = SocietasListItemVariant.ONE_LINE,
    headlineText: String,
    supportingText: String? = null,
    overlineText: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    SocietasListItem(
        modifier = modifier,
        variant = variant,
        headlineText = headlineText,
        supportingText = supportingText,
        overlineText = overlineText,
        leadingContent = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(spacing.iconSizeMedium),
                    tint = if (selected) {
                        designSystem.colors.Primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        },
        trailingContent = trailingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(spacing.iconSizeMedium),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        selected = selected,
        enabled = enabled,
        onClick = onClick
    )
}
