package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasBadgeVariant {
    FILLED,
    OUTLINED,
    DOT
}

enum class SocietasBadgeColor {
    PRIMARY,
    SECONDARY,
    SUCCESS,
    WARNING,
    ERROR,
    INFO,
    NEUTRAL
}

@Composable
fun SocietasBadge(
    modifier: Modifier = Modifier,
    text: String = "",
    variant: SocietasBadgeVariant = SocietasBadgeVariant.FILLED,
    color: SocietasBadgeColor = SocietasBadgeColor.PRIMARY,
    showBadge: Boolean = true
) {
    if (!showBadge) return
    
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    val (backgroundColor, contentColor) = when (color) {
        SocietasBadgeColor.PRIMARY -> colors.Primary to colors.OnPrimary
        SocietasBadgeColor.SECONDARY -> colors.Secondary to colors.OnSecondary
        SocietasBadgeColor.SUCCESS -> colors.Success to colors.White
        SocietasBadgeColor.WARNING -> colors.Warning to colors.White
        SocietasBadgeColor.ERROR -> colors.Error to colors.White
        SocietasBadgeColor.INFO -> colors.Info to colors.White
        SocietasBadgeColor.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    when (variant) {
        SocietasBadgeVariant.DOT -> {
            Box(
                modifier = modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
            )
        }
        SocietasBadgeVariant.FILLED -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(spacing.borderRadiusExtraLarge))
                    .background(backgroundColor)
                    .padding(horizontal = spacing.sm, vertical = spacing.xs),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                )
            }
        }
        SocietasBadgeVariant.OUTLINED -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(spacing.borderRadiusExtraLarge))
                    .background(Color.Transparent)
                    .padding(horizontal = spacing.sm, vertical = spacing.xs),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    color = backgroundColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun SocietasBadgedBox(
    modifier: Modifier = Modifier,
    badge: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            badge()
        }
    }
}
