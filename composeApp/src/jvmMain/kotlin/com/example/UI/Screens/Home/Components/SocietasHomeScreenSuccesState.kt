package com.example.UI.Screens.Home.Components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.Components.*
import com.example.UI.DesignSystem.SocietasIcons
import com.example.UI.DesignSystem.societasDesignSystem
import com.example.UI.Screens.Home.Components.ChatPanel.ChatPanel
import com.example.UI.Screens.Home.SocietasHomeScreenModel

@Composable
fun SocietasHomeScreenSuccessState(
    model: SocietasHomeScreenModel,
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors
    
    var selectedAgent by remember { mutableStateOf(model.cBoard.firstOrNull()) }
    
    // Animation for the entire layout
    val layoutAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500),
        label = "layout_alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(layoutAlpha)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Modern Sidebar
        ModernSidebar(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            agents = model.cBoard,
            enterprise = model.enterprise,
            selectedAgent = selectedAgent,
            onAgentSelected = { agent ->
                selectedAgent = agent
            }
        )
        
        // Divider
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
        )

        // Chat Area with Animation
        AnimatedContent(
            targetState = selectedAgent,
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f),
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it / 3 },
                    animationSpec = tween(300)
                ) + fadeIn(tween(300)) togetherWith 
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(300)
                ) + fadeOut(tween(300))
            },
            label = "chat_panel_transition"
        ) { agent ->
            ChatPanel(
                modifier = Modifier.fillMaxSize(),
                model = model,
                selectedAgent = agent
            )
        }
    }
}

@Composable
private fun ModernSidebar(
    modifier: Modifier = Modifier,
    agents: Array<SocietasHomeScreenModel.Agent>,
    enterprise: String,
    selectedAgent: SocietasHomeScreenModel.Agent?,
    onAgentSelected: (SocietasHomeScreenModel.Agent) -> Unit
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    val colors = designSystem.colors
    
    // Animated background gradient
    val gradientColors = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    )

    SocietasCard(
        variant = SocietasCardVariant.ELEVATED,
        size = SocietasCardSize.LARGE,
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(
                    topEnd = spacing.borderRadiusLarge,
                    bottomEnd = spacing.borderRadiusLarge
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.xxl)
        ) {
            // Header Section
            SidebarHeader(enterprise = enterprise)
            
            // Agents Section
            AgentsSection(
                agents = agents,
                selectedAgent = selectedAgent,
                onAgentSelected = onAgentSelected
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Footer Section
            SidebarFooter()
        }
    }
}

@Composable
private fun SidebarHeader(enterprise: String) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.sm)
    ) {
        SocietasAvatar(
            size = SocietasAvatarSize.LARGE,
            variant = SocietasAvatarVariant.INITIALS,
            initials = enterprise.take(2),
            backgroundColor = designSystem.colors.Primary,
            contentColor = designSystem.colors.OnPrimary
        )
        
        Text(
            text = enterprise,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        
        SocietasChip(
            text = "Enterprise",
            variant = SocietasChipVariant.FILLED,
            size = SocietasChipSize.SMALL,
            selected = false,
            leadingIcon = SocietasIcons.BUSINESS.filled
        )
    }
}

@Composable
private fun AgentsSection(
    agents: Array<SocietasHomeScreenModel.Agent>,
    selectedAgent: SocietasHomeScreenModel.Agent?,
    onAgentSelected: (SocietasHomeScreenModel.Agent) -> Unit
) {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "C-SUITE AGENTS",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            
            SocietasBadge(
                text = agents.size.toString(),
                variant = SocietasBadgeVariant.FILLED,
                color = SocietasBadgeColor.PRIMARY
            )
        }
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing.xs)
        ) {
            items(agents.toList()) { agent ->
                AgentListItem(
                    agent = agent,
                    isSelected = selectedAgent?.name == agent.name,
                    onClick = { onAgentSelected(agent) }
                )
            }
        }
    }
}

@Composable
private fun AgentListItem(
    agent: SocietasHomeScreenModel.Agent,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val designSystem = societasDesignSystem()
    
    SocietasIconListItem(
        headlineText = agent.name,
        supportingText = "AI Assistant",
        variant = SocietasListItemVariant.TWO_LINE,
        leadingIcon = when (agent.name.lowercase()) {
            "cfo" -> SocietasIcons.WORK.filled
            "ceo" -> SocietasIcons.BUSINESS.filled
            "cto" -> SocietasIcons.BUSINESS.filled
            else -> SocietasIcons.PERSON.filled
        },
        trailingIcon = if (isSelected) SocietasIcons.CHECK.filled else null,
        selected = isSelected,
        onClick = onClick
    )
}

@Composable
private fun SidebarFooter() {
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        SocietasButton(
            onClick = { /* Settings action */ },
            text = "Settings",
            variant = SocietasButtonVariant.GHOST,
            size = SocietasButtonSize.SMALL,
            fullWidth = true
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocietasAvatar(
                size = SocietasAvatarSize.SMALL,
                variant = SocietasAvatarVariant.INITIALS,
                initials = "US",
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "User Account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}