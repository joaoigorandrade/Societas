package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasSwitchSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun SocietasSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    size: SocietasSwitchSize = SocietasSwitchSize.MEDIUM,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    
    val switchModifier = when (size) {
        SocietasSwitchSize.SMALL -> modifier.size(width = 40.dp, height = 20.dp)
        SocietasSwitchSize.MEDIUM -> modifier.size(width = 52.dp, height = 32.dp)
        SocietasSwitchSize.LARGE -> modifier.size(width = 64.dp, height = 40.dp)
    }
    
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = switchModifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.OnPrimary,
            checkedTrackColor = colors.Primary,
            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            checkedBorderColor = Color.Transparent,
            uncheckedBorderColor = Color.Transparent,
            disabledCheckedThumbColor = colors.Neutral400,
            disabledCheckedTrackColor = colors.Neutral200,
            disabledUncheckedThumbColor = colors.Neutral300,
            disabledUncheckedTrackColor = colors.Neutral100
        ),
        interactionSource = interactionSource
    )
}
