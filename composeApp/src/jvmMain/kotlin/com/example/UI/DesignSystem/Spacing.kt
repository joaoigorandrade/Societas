package com.example.UI.DesignSystem

import androidx.compose.ui.unit.dp

object SocietasSpacing {
    // Base spacing unit (4dp)
    val base = 4.dp
    
    // Spacing scale
    val xs = base * 0.5f      // 2dp
    val sm = base * 1f        // 4dp
    val md = base * 2f        // 8dp
    val lg = base * 3f        // 12dp
    val xl = base * 4f        // 16dp
    val xxl = base * 6f       // 24dp
    val xxxl = base * 8f      // 32dp
    val huge = base * 12f     // 48dp
    val massive = base * 16f  // 64dp
    
    // Border radius
    val borderRadiusSmall = sm
    val borderRadiusMedium = md
    val borderRadiusLarge = lg
    val borderRadiusExtraLarge = xl
    
    // Elevation
    val elevationSmall = 2.dp
    val elevationMedium = 4.dp
    val elevationLarge = 8.dp
    val elevationExtraLarge = 16.dp
    
    // Icon sizes
    val iconSizeSmall = 16.dp
    val iconSizeMedium = 24.dp
    val iconSizeLarge = 32.dp
    val iconSizeExtraLarge = 48.dp
    
    // Avatar sizes
    val avatarSizeSmall = 32.dp
    val avatarSizeMedium = 48.dp
    val avatarSizeLarge = 64.dp
    val avatarSizeExtraLarge = 96.dp
    
    // Input field dimensions
    val inputHeight = 56.dp
    val inputPadding = md
    val inputBorderWidth = 1.dp
    
    // Button dimensions
    val buttonHeight = 48.dp
    val buttonMinWidth = 120.dp
    val buttonPadding = md
    
    // Card dimensions
    val cardElevation = elevationSmall
    val cardCornerRadius = borderRadiusMedium
    val cardPadding = lg
    
    // Component-specific spacing
    val screenPadding = xl
    val itemSpacing = md
    val sectionSpacing = xxl
}
