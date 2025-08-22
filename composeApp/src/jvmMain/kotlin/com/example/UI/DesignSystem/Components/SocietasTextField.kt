package com.example.UI.DesignSystem.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.societasDesignSystem

enum class SocietasTextFieldVariant {
    OUTLINED,
    FILLED,
    PLAIN
}

enum class SocietasTextFieldSize {
    SMALL,
    MEDIUM,
    LARGE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocietasTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    variant: SocietasTextFieldVariant = SocietasTextFieldVariant.OUTLINED,
    size: SocietasTextFieldSize = SocietasTextFieldSize.MEDIUM,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val designSystem = societasDesignSystem()
    val colors = designSystem.colors
    val spacing = designSystem.spacing
    
    val textFieldHeight = when (size) {
        SocietasTextFieldSize.SMALL -> 40.dp
        SocietasTextFieldSize.MEDIUM -> spacing.inputHeight
        SocietasTextFieldSize.LARGE -> 64.dp
    }
    
    val textFieldPadding = when (size) {
        SocietasTextFieldSize.SMALL -> PaddingValues(horizontal = spacing.sm, vertical = spacing.xs)
        SocietasTextFieldSize.MEDIUM -> PaddingValues(horizontal = spacing.md, vertical = spacing.sm)
        SocietasTextFieldSize.LARGE -> PaddingValues(horizontal = spacing.lg, vertical = spacing.md)
    }
    
    val textFieldShape = RoundedCornerShape(spacing.borderRadiusMedium)
    
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = when (variant) {
            SocietasTextFieldVariant.FILLED -> MaterialTheme.colorScheme.surfaceVariant
            else -> Color.Transparent
        },
        unfocusedContainerColor = when (variant) {
            SocietasTextFieldVariant.FILLED -> MaterialTheme.colorScheme.surfaceVariant
            else -> Color.Transparent
        },
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLabelColor = colors.Primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedIndicatorColor = colors.Primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
        errorIndicatorColor = colors.Error,
        errorLabelColor = colors.Error
    )
    
    when (variant) {
        SocietasTextFieldVariant.OUTLINED -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .fillMaxWidth()
                    .height(textFieldHeight),
                label = label?.let { { Text(it) } },
                placeholder = placeholder?.let { { Text(it) } },
                enabled = enabled,
                readOnly = readOnly,
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                colors = textFieldColors,
                shape = textFieldShape
            )
        }
        SocietasTextFieldVariant.FILLED -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .fillMaxWidth()
                    .height(textFieldHeight),
                label = label?.let { { Text(it) } },
                placeholder = placeholder?.let { { Text(it) } },
                enabled = enabled,
                readOnly = readOnly,
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                colors = textFieldColors,
                shape = textFieldShape
            )
        }
        SocietasTextFieldVariant.PLAIN -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(textFieldHeight)
                    .clip(textFieldShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(textFieldPadding)
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    readOnly = readOnly,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    visualTransformation = visualTransformation,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = when (size) {
                            SocietasTextFieldSize.SMALL -> designSystem.typography.typography.bodySmall.fontSize
                            SocietasTextFieldSize.MEDIUM -> designSystem.typography.typography.bodyMedium.fontSize
                            SocietasTextFieldSize.LARGE -> designSystem.typography.typography.bodyLarge.fontSize
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = when (size) {
                                            SocietasTextFieldSize.SMALL -> designSystem.typography.typography.bodySmall.fontSize
                                            SocietasTextFieldSize.MEDIUM -> designSystem.typography.typography.bodyMedium.fontSize
                                            SocietasTextFieldSize.LARGE -> designSystem.typography.typography.bodyLarge.fontSize
                                        }
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
    
    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            style = designSystem.typography.typography.bodySmall,
            color = colors.Error,
            modifier = Modifier.padding(start = spacing.sm, top = spacing.xs)
        )
    }
}
