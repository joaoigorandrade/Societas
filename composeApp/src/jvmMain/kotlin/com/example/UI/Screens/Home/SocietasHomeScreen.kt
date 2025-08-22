import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.UI.Screens.Home.Components.ChatPanel.ChatPanelViewModel
import com.example.UI.Screens.Home.Components.SocietasHomeScreenSuccessState
import com.example.UI.Screens.Home.SocietasHomeScreenModel
import com.example.UI.Screens.SocietasViewState
import org.koin.compose.koinInject

@Composable
fun SocietasHomeScreen(modifier: Modifier = Modifier) {
    val viewModel: SocietasHomeScreenViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()

    when(val state = uiState) {
        is SocietasViewState.Success -> {
            SocietasHomeScreenSuccessState(
                model = state.data as SocietasHomeScreenModel
            )
        }
        is SocietasViewState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is SocietasViewState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = state.message)
            }
        }
    }
}