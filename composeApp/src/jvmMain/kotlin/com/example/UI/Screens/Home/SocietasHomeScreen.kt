package com.example.UI.Screens.Home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.example.UI.DesignSystem.Components.SocietasIndeterminateProgress
import com.example.UI.DesignSystem.Components.SocietasProgressSize
import com.example.UI.DesignSystem.Components.SocietasProgressVariant
import com.example.UI.DesignSystem.societasDesignSystem
import com.example.UI.Screens.Home.Components.SocietasHomeScreenSuccessState
import com.example.UI.Screens.SocietasViewState
import org.koin.compose.koinInject

@Composable
fun SocietasHomeScreen(modifier: Modifier = Modifier) {
    val viewModel: SocietasHomeScreenViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()
    val designSystem = societasDesignSystem()
    
    // Smooth fade-in animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_fade_in"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        when(val state = uiState) {
            is SocietasViewState.Success -> {
                SocietasHomeScreenSuccessState(
                    model = state.data as SocietasHomeScreenModel
                )
            }
            is SocietasViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SocietasIndeterminateProgress(
                        variant = SocietasProgressVariant.CIRCULAR,
                        size = SocietasProgressSize.LARGE,
                        color = designSystem.colors.Primary
                    )
                }
            }
            is SocietasViewState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = designSystem.colors.Error
                    )
                }
            }
        }
    }
}