import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val repository = HabitRepository()
    val viewModel = HabitViewModel(repository)
    
    Window(
        onCloseRequest = {
            viewModel.saveAndExit()
            exitApplication()
        },
        title = "🌳 Micro-Habit Tree",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        HabitTreeApp(viewModel)
    }
}
