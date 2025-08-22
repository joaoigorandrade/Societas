package com.example.UI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.societas.GreetingService
import com.example.UI.DesignSystem.SocietasTheme
import com.example.UI.DesignSystem.Components.*
import com.example.UI.DesignSystem.societasDesignSystem
import com.example.UI.DesignSystem.SocietasIcons
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

import societas.composeapp.generated.resources.Res
import societas.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    SocietasTheme {
        val greetingService: GreetingService = koinInject()
        
        var showContent by remember { mutableStateOf(false) }
        var showDesignSystem by remember { mutableStateOf(false) }
        
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header
            Text(
                text = "Societas App",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Main content toggle
            SocietasButton(
                onClick = { showContent = !showContent },
                text = if (showContent) "Hide Content" else "Show Content",
                variant = SocietasButtonVariant.PRIMARY,
                size = SocietasButtonSize.MEDIUM,
                fullWidth = true,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Design system showcase toggle
            SocietasButton(
                onClick = { showDesignSystem = !showDesignSystem },
                text = if (showDesignSystem) "Hide Design System" else "Show Design System",
                variant = SocietasButtonVariant.SECONDARY,
                size = SocietasButtonSize.MEDIUM,
                fullWidth = true,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Main content
            AnimatedVisibility(showContent) {
                val greeting = remember { greetingService.greet() }
                SocietasCard(
                    variant = SocietasCardVariant.ELEVATED,
                    size = SocietasCardSize.MEDIUM,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = null,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Compose: $greeting",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            // Design System Showcase
            AnimatedVisibility(showDesignSystem) {
                DesignSystemShowcase()
            }
        }
    }
}

@Composable
fun DesignSystemShowcase() {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.lg)
    ) {
        // Colors Section
        SocietasCard(
            variant = SocietasCardVariant.ELEVATED,
            size = SocietasCardSize.MEDIUM
        ) {
            SocietasCardHeader(
                title = "Color Palette",
                subtitle = "Primary colors used in the design system"
            )
            SocietasCardContent {
                ColorPaletteShowcase()
            }
        }
        
        // Typography Section
        SocietasCard(
            variant = SocietasCardVariant.ELEVATED,
            size = SocietasCardSize.MEDIUM
        ) {
            SocietasCardHeader(
                title = "Typography",
                subtitle = "Text styles and hierarchy"
            )
            SocietasCardContent {
                TypographyShowcase()
            }
        }
        
        // Components Section
        SocietasCard(
            variant = SocietasCardVariant.ELEVATED,
            size = SocietasCardSize.MEDIUM
        ) {
            SocietasCardHeader(
                title = "Components",
                subtitle = "Reusable UI components"
            )
            SocietasCardContent {
                ComponentsShowcase()
            }
        }
        
        // Icons Section
        SocietasCard(
            variant = SocietasCardVariant.ELEVATED,
            size = SocietasCardSize.MEDIUM
        ) {
            SocietasCardHeader(
                title = "Icons",
                subtitle = "Material Design icon system"
            )
            SocietasCardContent {
                IconsShowcase()
            }
        }
    }
}

@Composable
fun ColorPaletteShowcase() {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.sm)
    ) {
        ColorSwatch("Primary", colors.Primary, colors.OnPrimary)
        ColorSwatch("Secondary", colors.Secondary, colors.OnSecondary)
        ColorSwatch("Tertiary", colors.Tertiary, colors.OnTertiary)
        ColorSwatch("Background", colors.Background, colors.OnBackground)
        ColorSwatch("Surface", colors.Surface, colors.OnSurface)
    }
}

@Composable
fun ColorSwatch(
    name: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color
) {
    val spacing = societasDesignSystem().spacing
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = backgroundColor.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
fun TypographyShowcase() {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.sm)
    ) {
        Text(
            text = "Display Large",
            style = designSystem.typography.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Headline Medium",
            style = designSystem.typography.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Title Large",
            style = designSystem.typography.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Body Large",
            style = designSystem.typography.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Label Medium",
            style = designSystem.typography.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ComponentsShowcase() {
    val spacing = societasDesignSystem().spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        // Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            SocietasButton(
                onClick = { },
                text = "Primary",
                variant = SocietasButtonVariant.PRIMARY,
                size = SocietasButtonSize.SMALL
            )
            SocietasButton(
                onClick = { },
                text = "Secondary",
                variant = SocietasButtonVariant.SECONDARY,
                size = SocietasButtonSize.SMALL
            )
            SocietasButton(
                onClick = { },
                text = "Tertiary",
                variant = SocietasButtonVariant.TERTIARY,
                size = SocietasButtonSize.SMALL
            )
        }
        
        // Text Fields
        SocietasTextField(
            value = "",
            onValueChange = { },
            label = "Sample Input",
            placeholder = "Enter text here",
            variant = SocietasTextFieldVariant.OUTLINED,
            size = SocietasTextFieldSize.MEDIUM
        )
        
        // Cards
        SocietasCard(
            variant = SocietasCardVariant.OUTLINED,
            size = SocietasCardSize.SMALL
        ) {
            Text(
                text = "Sample Card",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun IconsShowcase() {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        Text(
            text = "Common Icons",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Icon(
                imageVector = SocietasIcons.HOME.filled,
                contentDescription = "Home",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = SocietasIcons.SEARCH.filled,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = SocietasIcons.SETTINGS.filled,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = SocietasIcons.SEND.filled,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = SocietasIcons.CHAT.filled,
                contentDescription = "Chat",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}