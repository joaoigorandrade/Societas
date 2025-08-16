import com.example.Domain.UseCase.Home.SocietasHomeUseCase
import com.example.UI.Screens.SocietasViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SocietasHomeScreenViewModel(private val useCase: SocietasHomeUseCase) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<SocietasViewState>(SocietasViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        scope.launch {
            _uiState.value = useCase.execute()
        }
    }
}