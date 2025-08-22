package com.example.UI.DesignSystem

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

object SocietasDesignSystem {
    val colors = SocietasColors
    val typography = SocietasTypography
    val spacing = SocietasSpacing
    
    // Light color scheme
    val lightColorScheme = lightColorScheme(
        primary = colors.Primary,
        onPrimary = colors.OnPrimary,
        primaryContainer = colors.LightMint,
        onPrimaryContainer = colors.OnSurface,
        secondary = colors.Secondary,
        onSecondary = colors.OnSecondary,
        secondaryContainer = colors.MintGreen,
        onSecondaryContainer = colors.OnSurface,
        tertiary = colors.Tertiary,
        onTertiary = colors.OnTertiary,
        tertiaryContainer = colors.SageGreen,
        onTertiaryContainer = colors.OnSurface,
        background = colors.Background,
        onBackground = colors.OnBackground,
        surface = colors.Surface,
        onSurface = colors.OnSurface,
        surfaceVariant = colors.Neutral100,
        onSurfaceVariant = colors.Neutral700,
        outline = colors.Neutral300,
        outlineVariant = colors.Neutral200,
        error = colors.Error,
        onError = colors.White,
        errorContainer = Color(0xFFFFEBEE),
        onErrorContainer = colors.Error
    )
    
    // Dark color scheme
    val darkColorScheme = darkColorScheme(
        primary = colors.MintGreen,
        onPrimary = colors.DarkGreen,
        primaryContainer = colors.DarkGreen,
        onPrimaryContainer = colors.MintGreen,
        secondary = colors.SageGreen,
        onSecondary = colors.DarkGreen,
        secondaryContainer = colors.DarkGreen,
        onSecondaryContainer = colors.SageGreen,
        tertiary = colors.LightMint,
        onTertiary = colors.DarkGreen,
        tertiaryContainer = colors.DarkGreen,
        onTertiaryContainer = colors.LightMint,
        background = colors.DarkGreen,
        onBackground = colors.LightMint,
        surface = colors.Neutral800,
        onSurface = colors.LightMint,
        surfaceVariant = colors.Neutral700,
        onSurfaceVariant = colors.Neutral200,
        outline = colors.Neutral600,
        outlineVariant = colors.Neutral700,
        error = colors.Error,
        onError = colors.White,
        errorContainer = Color(0xFF3E2723),
        onErrorContainer = Color(0xFFFFCDD2)
    )
}

// CompositionLocal for design system
val LocalSocietasDesignSystem = staticCompositionLocalOf { SocietasDesignSystem }

// Composable to provide design system
@Composable
fun SocietasTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        SocietasDesignSystem.darkColorScheme
    } else {
        SocietasDesignSystem.lightColorScheme
    }
    
    CompositionLocalProvider(LocalSocietasDesignSystem provides SocietasDesignSystem) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SocietasDesignSystem.typography.typography,
            content = content
        )
    }
}

// Extension function to access design system
@Composable
fun societasDesignSystem(): SocietasDesignSystem {
    return LocalSocietasDesignSystem.current
}
