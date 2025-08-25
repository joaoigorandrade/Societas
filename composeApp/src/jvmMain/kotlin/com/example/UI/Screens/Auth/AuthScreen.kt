package com.example.UI.Screens.Auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.UI.DesignSystem.Components.*
import com.example.UI.DesignSystem.societasDesignSystem
import org.koin.compose.koinInject

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    val viewModel: AuthViewModel = koinInject()
    val authState by viewModel.authState.collectAsState()
    val designSystem = societasDesignSystem()
    val spacing = designSystem.spacing

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SocietasCard(
            variant = SocietasCardVariant.ELEVATED,
            modifier = Modifier.width(400.dp)
        ) {
            Column(
                modifier = Modifier.padding(spacing.xxl),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.lg)
            ) {
                Text("Welcome to Societas", style = MaterialTheme.typography.headlineMedium)

                val tabs = listOf("Sign In", "Sign Up")
                TabRow(selectedTabIndex = viewModel.authMode.ordinal) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = viewModel.authMode.ordinal == index,
                            onClick = { viewModel.setAuthType(if (index == 0) AuthMode.SIGN_IN else AuthMode.SIGN_UP) },
                            text = { Text(title) }
                        )
                    }
                }

                SocietasTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    size = SocietasTextFieldSize.MEDIUM,
                    modifier = Modifier.fillMaxWidth()
                )

                SocietasTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = "Password",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    size = SocietasTextFieldSize.MEDIUM,
                    modifier = Modifier.fillMaxWidth()
                )

                if (viewModel.authMode == AuthMode.SIGN_UP) {
                    SocietasTextField(
                        value = viewModel.confirmPassword,
                        onValueChange = { viewModel.confirmPassword = it },
                        label = "Confirm Password",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation(),
                        size = SocietasTextFieldSize.MEDIUM,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (authState is AuthViewState.Error) {
                    Text((authState as AuthViewState.Error).message, color = MaterialTheme.colorScheme.error)
                }

                SocietasButton(
                    onClick = {
                        if (viewModel.authMode == AuthMode.SIGN_IN) {
                            viewModel.signIn()
                        } else {
                            viewModel.signUp()
                        }
                    },
                    text = if (viewModel.authMode == AuthMode.SIGN_IN) "Sign In" else "Sign Up",
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = authState is AuthViewState.Loading,
                    variant = SocietasButtonVariant.PRIMARY,
                    enabled = authState !is AuthViewState.Loading
                )
            }
        }
    }
}
