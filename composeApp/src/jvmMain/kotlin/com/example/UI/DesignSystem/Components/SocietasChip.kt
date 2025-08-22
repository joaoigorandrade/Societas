package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasChipVariant {
    FILLED,
    OUTLINED,
    ELEVATED
}

enum class SocietasChipSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun SocietasChip(
    text: String,
    modifier: Modifier = Modifier,
    variant: SocietasChipVariant = SocietasChipVariant.FILLED,
    size: SocietasChipSize = SocietasChipSize.MEDIUM,
    selected: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    val chipPadding = when (size) {
        SocietasChipSize.SMALL -> PaddingValues(horizontal = spacing.sm, vertical = spacing.xs)
        SocietasChipSize.MEDIUM -> PaddingValues(horizontal = spacing.md, vertical = spacing.sm)
        SocietasChipSize.LARGE -> PaddingValues(horizontal = spacing.lg, vertical = spacing.md)
    }
    
    val textStyle = when (size) {
        SocietasChipSize.SMALL -> MaterialTheme.typography.labelSmall
        SocietasChipSize.MEDIUM -> MaterialTheme.typography.labelMedium
        SocietasChipSize.LARGE -> MaterialTheme.typography.labelLarge
    }
    
    val iconSize = when (size) {
        SocietasChipSize.SMALL -> spacing.iconSizeSmall
        SocietasChipSize.MEDIUM -> spacing.iconSizeMedium
        SocietasChipSize.LARGE -> spacing.iconSizeLarge
    }
    
    val (backgroundColor, contentColor, borderColor) = when (variant) {
        SocietasChipVariant.FILLED -> {
            if (selected) {
                Triple(colors.Primary, colors.OnPrimary, Color.Transparent)
            } else {
                Triple(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    Color.Transparent
                )
            }
        }
        SocietasChipVariant.OUTLINED -> {
            if (selected) {
                Triple(colors.Primary, colors.OnPrimary, colors.Primary)
            } else {
                Triple(
                    Color.Transparent,
                    MaterialTheme.colorScheme.onSurface,
                    MaterialTheme.colorScheme.outline
                )
            }
        }
        SocietasChipVariant.ELEVATED -> {
            if (selected) {
                Triple(colors.Primary, colors.OnPrimary, Color.Transparent)
            } else {
                Triple(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.colorScheme.onSurface,
                    Color.Transparent
                )
            }
        }
    }
    
    val elevation = if (variant == SocietasChipVariant.ELEVATED) spacing.elevationSmall else 0.dp
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(spacing.borderRadiusExtraLarge))
            .then(
                if (onClick != null && enabled) {
                    Modifier.clickable { onClick() }
                } else Modifier
            ),
        color = backgroundColor,
        border = if (borderColor != Color.Transparent) {
            BorderStroke(1.dp, borderColor)
        } else null,
        shadowElevation = elevation,
        shape = RoundedCornerShape(spacing.borderRadiusExtraLarge)
    ) {
        Row(
            modifier = Modifier.padding(chipPadding),
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = contentColor
                )
            }
            
            Text(
                text = text,
                style = textStyle,
                color = contentColor,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
            
            trailingIcon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = contentColor
                )
            }
        }
    }
}

@Composable
fun SocietasChipGroup(
    modifier: Modifier = Modifier,
    chips: List<@Composable () -> Unit>,
    arrangement: Arrangement.Horizontal = Arrangement.spacedBy(societasDesignSystem().spacing.sm)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        chips.forEach { chip ->
            chip()
        }
    }
}
