# Societas Design System

A comprehensive design system for the Societas application built with Jetpack Compose.

## Overview

The Societas Design System provides a consistent, scalable foundation for building user interfaces. It includes:

- **Color Palette**: A carefully selected color scheme with semantic meanings
- **Typography**: A comprehensive type scale following Material Design principles
- **Spacing**: A consistent spacing system based on 4dp grid
- **Icons**: Material Design icon system with multiple variants
- **Components**: Reusable UI components that follow the design system

## Color Palette

### Primary Colors
- **White** (`#FFFFFF`): Pure white for backgrounds and text
- **Dark Green** (`#3B413C`): Primary brand color, used for text and primary elements
- **Sage Green** (`#9DB5B2`): Secondary color, used for secondary elements
- **Light Mint** (`#DAF0EE`): Surface color, used for cards and containers
- **Mint Green** (`#94D1BE`): Tertiary color, used for accents and highlights

### Semantic Colors
- **Success**: Green for positive actions and states
- **Warning**: Orange for caution and attention
- **Error**: Red for errors and destructive actions
- **Info**: Blue for informational content

### Usage
```kotlin
import com.example.UI.DesignSystem.societasDesignSystem

val designSystem = societasDesignSystem()
val colors = designSystem.colors

// Use semantic colors
Text(
    text = "Success message",
    color = colors.Success
)
```

## Typography

The typography system follows Material Design 3 principles with a comprehensive scale:

### Display Styles
- `displayLarge`: 57sp - Hero headlines
- `displayMedium`: 45sp - Large headlines
- `displaySmall`: 36sp - Medium headlines

### Headline Styles
- `headlineLarge`: 32sp - Page titles
- `headlineMedium`: 28sp - Section titles
- `headlineSmall`: 24sp - Subsection titles

### Title Styles
- `titleLarge`: 22sp - Card titles
- `titleMedium`: 16sp - List item titles
- `titleSmall`: 14sp - Small titles

### Body Styles
- `bodyLarge`: 16sp - Main content
- `bodyMedium`: 14sp - Secondary content
- `bodySmall`: 12sp - Captions and metadata

### Label Styles
- `labelLarge`: 14sp - Button text
- `labelMedium`: 12sp - Form labels
- `labelSmall`: 11sp - Small labels

### Usage
```kotlin
val designSystem = societasDesignSystem()
val typography = designSystem.typography

Text(
    text = "Page Title",
    style = typography.typography.headlineLarge
)
```

## Spacing

The spacing system is based on a 4dp grid unit:

### Base Scale
- `xs`: 2dp - Minimal spacing
- `sm`: 4dp - Small spacing
- `md`: 8dp - Medium spacing
- `lg`: 12dp - Large spacing
- `xl`: 16dp - Extra large spacing
- `xxl`: 24dp - Section spacing
- `xxxl`: 32dp - Large section spacing

### Component Spacing
- `buttonPadding`: 8dp - Button internal padding
- `cardPadding`: 12dp - Card internal padding
- `screenPadding`: 16dp - Screen edge padding
- `itemSpacing`: 8dp - Between list items
- `sectionSpacing`: 24dp - Between sections

### Usage
```kotlin
val designSystem = societasDesignSystem()
val spacing = designSystem.spacing

Box(
    modifier = Modifier.padding(spacing.screenPadding)
) {
    // Content
}
```

## Icons

The icon system provides Material Design icons in three variants:

### Icon Variants
- **Filled**: Solid icons for primary actions
- **Outlined**: Line icons for secondary actions
- **Rounded**: Soft-edged icons for a friendly feel

### Icon Categories
- **Navigation**: Home, Search, Settings, Arrows
- **Communication**: Send, Chat, Email, Phone
- **Actions**: Add, Edit, Delete, Save
- **Status**: Check, Error, Warning, Info
- **Content**: Attach, Image, Video, Audio
- **Social**: Person, Group, Account
- **Utility**: Menu, More, Refresh, Download
- **Business**: Business, Work, School
- **Time**: Schedule, Event, Today
- **Location**: Location, Map, Place

### Usage
```kotlin
import com.example.UI.DesignSystem.SocietasIcons

Icon(
    imageVector = SocietasIcons.HOME.filled,
    contentDescription = "Home",
    tint = MaterialTheme.colorScheme.primary
)
```

## Components

### SocietasButton

A customizable button component with multiple variants and sizes.

```kotlin
SocietasButton(
    onClick = { /* action */ },
    text = "Click me",
    variant = SocietasButtonVariant.PRIMARY,
    size = SocietasButtonSize.MEDIUM,
    fullWidth = true
)
```

**Variants:**
- `PRIMARY`: Main action buttons
- `SECONDARY`: Secondary actions
- `TERTIARY`: Tertiary actions
- `OUTLINE`: Outlined buttons
- `GHOST`: Text-only buttons

**Sizes:**
- `SMALL`: 36dp height
- `MEDIUM`: 48dp height
- `LARGE`: 56dp height

### SocietasCard

A flexible card component for content containers.

```kotlin
SocietasCard(
    variant = SocietasCardVariant.ELEVATED,
    size = SocietasCardSize.MEDIUM
) {
    SocietasCardHeader(
        title = "Card Title",
        subtitle = "Card subtitle"
    )
    SocietasCardContent {
        // Card content
    }
    SocietasCardActions {
        // Action buttons
    }
}
```

**Variants:**
- `ELEVATED`: Raised card with shadow
- `OUTLINED`: Card with border
- `FILLED`: Card with background color

**Sizes:**
- `SMALL`: Minimal padding
- `MEDIUM`: Standard padding
- `LARGE`: Generous padding

### SocietasTextField

A comprehensive text input component.

```kotlin
SocietasTextField(
    value = text,
    onValueChange = { text = it },
    label = "Input Label",
    placeholder = "Enter text here",
    variant = SocietasTextFieldVariant.OUTLINED,
    size = SocietasTextFieldSize.MEDIUM
)
```

**Variants:**
- `OUTLINED`: Standard outlined input
- `FILLED`: Input with background
- `PLAIN`: Minimal styling

**Sizes:**
- `SMALL`: 40dp height
- `MEDIUM`: 56dp height
- `LARGE`: 64dp height

## Theme Integration

The design system integrates seamlessly with Material 3:

```kotlin
@Composable
fun App() {
    SocietasTheme {
        // Your app content
    }
}
```

The theme automatically provides:
- Light and dark color schemes
- Typography scale
- Component styling
- Consistent spacing

## Best Practices

1. **Use Semantic Colors**: Always use semantic colors instead of hardcoded values
2. **Follow Typography Scale**: Use the predefined typography styles for consistency
3. **Respect Spacing**: Use the spacing system for consistent layouts
4. **Choose Appropriate Icons**: Use filled icons for primary actions, outlined for secondary
5. **Component Composition**: Compose complex UIs using the provided components
6. **Accessibility**: Ensure proper contrast ratios and touch targets

## Extending the Design System

To add new components or modify existing ones:

1. Create the component in the `Components` package
2. Follow the existing naming conventions
3. Use the design system tokens (colors, typography, spacing)
4. Add proper documentation
5. Update this README

## Examples

See the main `App.kt` file for a complete showcase of the design system components and their usage.
