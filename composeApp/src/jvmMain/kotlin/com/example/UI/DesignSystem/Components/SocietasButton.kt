package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.SocietasSpacing
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasButtonVariant {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    OUTLINE,
    GHOST
}

enum class SocietasButtonSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun SocietasButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    variant: SocietasButtonVariant = SocietasButtonVariant.PRIMARY,
    size: SocietasButtonSize = SocietasButtonSize.MEDIUM,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    fullWidth: Boolean = false
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }
    
    val buttonHeight = when (size) {
        SocietasButtonSize.SMALL -> 36.dp
        SocietasButtonSize.MEDIUM -> spacing.buttonHeight
        SocietasButtonSize.LARGE -> 56.dp
    }
    
    val buttonPadding = when (size) {
        SocietasButtonSize.SMALL -> PaddingValues(horizontal = spacing.sm, vertical = spacing.xs)
        SocietasButtonSize.MEDIUM -> PaddingValues(horizontal = spacing.md, vertical = spacing.sm)
        SocietasButtonSize.LARGE -> PaddingValues(horizontal = spacing.lg, vertical = spacing.md)
    }
    
    val buttonColors = when (variant) {
        SocietasButtonVariant.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = colors.Primary,
            contentColor = colors.OnPrimary,
            disabledContainerColor = colors.Neutral300,
            disabledContentColor = colors.Neutral600
        )
        SocietasButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = colors.Secondary,
            contentColor = colors.OnSecondary,
            disabledContainerColor = colors.Neutral300,
            disabledContentColor = colors.Neutral600
        )
        SocietasButtonVariant.TERTIARY -> ButtonDefaults.buttonColors(
            containerColor = colors.Tertiary,
            contentColor = colors.OnTertiary,
            disabledContainerColor = colors.Neutral300,
            disabledContentColor = colors.Neutral600
        )
        SocietasButtonVariant.OUTLINE -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = colors.Primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.Neutral600
        )
        SocietasButtonVariant.GHOST -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = colors.Primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.Neutral600
        )
    }
    
    val buttonShape = when (variant) {
        SocietasButtonVariant.OUTLINE -> RoundedCornerShape(spacing.borderRadiusMedium)
        else -> RoundedCornerShape(spacing.borderRadiusMedium)
    }
    
    when (variant) {
        SocietasButtonVariant.OUTLINE -> {
            androidx.compose.material3.OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier.height(buttonHeight),
                enabled = enabled && !isLoading,
                colors = buttonColors,
                shape = buttonShape,
                contentPadding = buttonPadding
            ) {
                Text(
                    text = if (isLoading) "Loading..." else text,
                    textAlign = TextAlign.Center,
                    style = when (size) {
                        SocietasButtonSize.SMALL -> designSystem.typography.typography.labelMedium
                        SocietasButtonSize.MEDIUM -> designSystem.typography.typography.labelLarge
                        SocietasButtonSize.LARGE -> designSystem.typography.typography.titleMedium
                    }
                )
            }
        }
        SocietasButtonVariant.GHOST -> {
            androidx.compose.material3.TextButton(
                onClick = onClick,
                modifier = buttonModifier.height(buttonHeight),
                enabled = enabled && !isLoading,
                colors = buttonColors,
                shape = buttonShape,
                contentPadding = buttonPadding
            ) {
                Text(
                    text = if (isLoading) "Loading..." else text,
                    textAlign = TextAlign.Center,
                    style = when (size) {
                        SocietasButtonSize.SMALL -> designSystem.typography.typography.labelMedium
                        SocietasButtonSize.MEDIUM -> designSystem.typography.typography.labelLarge
                        SocietasButtonSize.LARGE -> designSystem.typography.typography.titleMedium
                    }
                )
            }
        }
        else -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier.height(buttonHeight),
                enabled = enabled && !isLoading,
                colors = buttonColors,
                shape = buttonShape,
                contentPadding = buttonPadding
            ) {
                Text(
                    text = if (isLoading) "Loading..." else text,
                    textAlign = TextAlign.Center,
                    style = when (size) {
                        SocietasButtonSize.SMALL -> designSystem.typography.typography.labelMedium
                        SocietasButtonSize.MEDIUM -> designSystem.typography.typography.labelLarge
                        SocietasButtonSize.LARGE -> designSystem.typography.typography.titleMedium
                    }
                )
            }
        }
    }
}
