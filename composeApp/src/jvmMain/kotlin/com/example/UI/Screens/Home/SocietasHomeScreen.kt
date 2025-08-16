import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.UI.Screens.SocietasViewState
import org.koin.compose.koinInject

@Composable
fun SocietasHomeScreen(modifier: Modifier = Modifier) {
    val viewModel: SocietasHomeScreenViewModel = koinInject()
    val uiState = viewModel.uiState.collectAsState()

    when(val state = uiState.value) {
        is SocietasViewState.Success -> {
            Column {
                Text(
                    state.data.toString()
                )
            }
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