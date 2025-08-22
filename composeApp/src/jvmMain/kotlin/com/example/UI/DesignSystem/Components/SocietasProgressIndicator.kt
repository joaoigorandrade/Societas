package com.example.UI.DesignSystem.Components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.UI.DesignSystem.societasDesignSystem
import kotlin.math.cos
import kotlin.math.sin

enum class SocietasProgressVariant {
    LINEAR,
    CIRCULAR,
    STEP
}

enum class SocietasProgressSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun SocietasProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    variant: SocietasProgressVariant = SocietasProgressVariant.LINEAR,
    size: SocietasProgressSize = SocietasProgressSize.MEDIUM,
    color: Color? = null,
    backgroundColor: Color? = null,
    showLabel: Boolean = false,
    label: String = "${(progress * 100).toInt()}%"
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    val progressColor = color ?: colors.Primary
    val trackColor = backgroundColor ?: MaterialTheme.colorScheme.surfaceVariant
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )
    
    when (variant) {
        SocietasProgressVariant.LINEAR -> {
            val height = when (size) {
                SocietasProgressSize.SMALL -> 4.dp
                SocietasProgressSize.MEDIUM -> 6.dp
                SocietasProgressSize.LARGE -> 8.dp
            }
            
            Column(modifier = modifier) {
                if (showLabel) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .clip(RoundedCornerShape(height / 2)),
                    color = progressColor,
                    trackColor = trackColor,
                    strokeCap = StrokeCap.Round
                )
            }
        }
        
        SocietasProgressVariant.CIRCULAR -> {
            val indicatorSize = when (size) {
                SocietasProgressSize.SMALL -> 32.dp
                SocietasProgressSize.MEDIUM -> 48.dp
                SocietasProgressSize.LARGE -> 64.dp
            }
            
            val strokeWidth = when (size) {
                SocietasProgressSize.SMALL -> 3.dp
                SocietasProgressSize.MEDIUM -> 4.dp
                SocietasProgressSize.LARGE -> 6.dp
            }
            
            Box(
                modifier = modifier.size(indicatorSize),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(indicatorSize),
                    color = progressColor,
                    strokeWidth = strokeWidth,
                    trackColor = trackColor,
                    strokeCap = StrokeCap.Round
                )
                
                if (showLabel) {
                    Text(
                        text = label,
                        style = when (size) {
                            SocietasProgressSize.SMALL -> MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                            SocietasProgressSize.MEDIUM -> MaterialTheme.typography.labelSmall
                            SocietasProgressSize.LARGE -> MaterialTheme.typography.labelMedium
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        SocietasProgressVariant.STEP -> {
            // Custom step progress indicator
            SocietasStepProgressIndicator(
                progress = animatedProgress,
                modifier = modifier,
                size = size,
                color = progressColor,
                backgroundColor = trackColor,
                showLabel = showLabel,
                label = label
            )
        }
    }
}

@Composable
private fun SocietasStepProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    size: SocietasProgressSize,
    color: Color,
    backgroundColor: Color,
    showLabel: Boolean,
    label: String
) {
    val spacing = societasDesignSystem().spacing
    
    val stepSize = when (size) {
        SocietasProgressSize.SMALL -> 8.dp
        SocietasProgressSize.MEDIUM -> 12.dp
        SocietasProgressSize.LARGE -> 16.dp
    }
    
    val stepCount = 5 // Number of steps
    val completedSteps = (progress * stepCount).toInt()
    
    Column(modifier = modifier) {
        if (showLabel) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
        
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(stepSize)
        ) {
            val stepWidth = this.size.width / stepCount
            val stepSpacing = stepWidth * 0.1f
            val actualStepWidth = stepWidth - stepSpacing
            
            repeat(stepCount) { index ->
                val isCompleted = index < completedSteps
                val stepColor = if (isCompleted) color else backgroundColor
                
                drawRoundRect(
                    color = stepColor,
                    topLeft = Offset(index * stepWidth, 0f),
                    size = Size(actualStepWidth, this.size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = this.size.height / 2,
                        y = this.size.height / 2
                    )
                )
            }
        }
    }
}

@Composable
fun SocietasIndeterminateProgress(
    modifier: Modifier = Modifier,
    variant: SocietasProgressVariant = SocietasProgressVariant.CIRCULAR,
    size: SocietasProgressSize = SocietasProgressSize.MEDIUM,
    color: Color? = null
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val progressColor = color ?: colors.Primary
    
    when (variant) {
        SocietasProgressVariant.CIRCULAR -> {
            val indicatorSize = when (size) {
                SocietasProgressSize.SMALL -> 32.dp
                SocietasProgressSize.MEDIUM -> 48.dp
                SocietasProgressSize.LARGE -> 64.dp
            }
            
            val strokeWidth = when (size) {
                SocietasProgressSize.SMALL -> 3.dp
                SocietasProgressSize.MEDIUM -> 4.dp
                SocietasProgressSize.LARGE -> 6.dp
            }
            
            CircularProgressIndicator(
                modifier = modifier.size(indicatorSize),
                color = progressColor,
                strokeWidth = strokeWidth,
                strokeCap = StrokeCap.Round
            )
        }
        
        else -> {
            val height = when (size) {
                SocietasProgressSize.SMALL -> 4.dp
                SocietasProgressSize.MEDIUM -> 6.dp
                SocietasProgressSize.LARGE -> 8.dp
            }
            
            LinearProgressIndicator(
                modifier = modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(height / 2)),
                color = progressColor,
                strokeCap = StrokeCap.Round
            )
        }
    }
}
