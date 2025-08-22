package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasCardVariant {
    ELEVATED,
    OUTLINED,
    FILLED
}

enum class SocietasCardSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun SocietasCard(
    modifier: Modifier = Modifier,
    variant: SocietasCardVariant = SocietasCardVariant.ELEVATED,
    size: SocietasCardSize = SocietasCardSize.MEDIUM,
    contentPadding: PaddingValues? = null,
    content: @Composable () -> Unit
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    val cardPadding = contentPadding ?: when (size) {
        SocietasCardSize.SMALL -> PaddingValues(spacing.sm)
        SocietasCardSize.MEDIUM -> PaddingValues(spacing.md)
        SocietasCardSize.LARGE -> PaddingValues(spacing.lg)
    }
    
    val cardElevation = when (variant) {
        SocietasCardVariant.ELEVATED -> CardDefaults.cardElevation(
            defaultElevation = spacing.cardElevation
        )
        else -> CardDefaults.cardElevation(defaultElevation = 0.dp)
    }
    
    val cardColors = when (variant) {
        SocietasCardVariant.ELEVATED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
        SocietasCardVariant.OUTLINED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
        SocietasCardVariant.FILLED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
    
    val cardShape = RoundedCornerShape(spacing.cardCornerRadius)
    
    when (variant) {
        SocietasCardVariant.ELEVATED -> {
            Card(
                modifier = modifier,
                elevation = cardElevation,
                colors = cardColors,
                shape = cardShape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding)
                ) {
                    content()
                }
            }
        }
        SocietasCardVariant.OUTLINED -> {
            Card(
                modifier = modifier,
                elevation = cardElevation,
                colors = cardColors,
                shape = cardShape,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding)
                ) {
                    content()
                }
            }
        }
        SocietasCardVariant.FILLED -> {
            Box(
                modifier = modifier
                    .clip(cardShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(cardPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SocietasCardHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    titleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium,
    subtitleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(modifier = modifier) {
        androidx.compose.material3.Text(
            text = title,
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (subtitle != null) {
            androidx.compose.material3.Text(
                text = subtitle,
                style = subtitleStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SocietasCardContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
    }
}

@Composable
fun SocietasCardActions(
    actions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = androidx.compose.ui.Alignment.CenterEnd
    ) {
        actions()
    }
}
