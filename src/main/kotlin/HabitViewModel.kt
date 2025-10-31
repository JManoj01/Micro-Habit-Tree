import androidx.compose.runtime.*
import java.time.LocalDate
import java.util.*

class HabitViewModel(private val repository: HabitRepository) {
    private var appData by mutableStateOf(repository.loadData())
    
    val habits: List<Habit> get() = appData.habits
    val settings: AppSettings get() = appData.settings
    var selectedHabitId by mutableStateOf<String?>(null)
    
    fun addHabit(name: String, emoji: String) {
        val newHabit = Habit(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            emoji = emoji.ifEmpty { "🌱" }
        )
        appData = appData.copy(habits = habits + newHabit)
        selectedHabitId = newHabit.id
        save()
    }
    
    fun updateHabit(id: String, name: String, emoji: String) {
        appData = appData.copy(
            habits = habits.map { habit ->
                if (habit.id == id) {
                    habit.copy(name = name.trim(), emoji = emoji)
                } else {
                    habit
                }
            }
        )
        save()
    }
    
    fun deleteHabit(id: String) {
        appData = appData.copy(habits = habits.filter { it.id != id })
        if (selectedHabitId == id) {
            selectedHabitId = habits.firstOrNull()?.id
        }
        save()
    }
    
    fun toggleHabitToday(id: String) {
        val today = LocalDate.now()
        appData = appData.copy(
            habits = habits.map { habit ->
                if (habit.id == id) {
                    habit.withToggled(today)
                } else {
                    habit
                }
            }
        )
        
        val wasCheckedIn = settings.lastCheckInDate == today.toString()
        if (!wasCheckedIn) {
            appData = appData.copy(
                settings = settings.copy(
                    totalCheckIns = settings.totalCheckIns + 1,
                    lastCheckInDate = today.toString()
                )
            )
        }
        
        save()
    }
    
    fun updateSettings(daysToShow: Int? = null, isDarkTheme: Boolean? = null) {
        appData = appData.copy(
            settings = settings.copy(
                daysToShow = daysToShow ?: settings.daysToShow,
                isDarkTheme = isDarkTheme ?: settings.isDarkTheme
            )
        )
        save()
    }
    
    fun clearAllData() {
        appData = AppData(
            habits = emptyList(),
            settings = AppSettings()
        )
        selectedHabitId = null
        save()
    }
    
    fun exportToJson(): String = repository.exportData()
    
    fun importFromJson(json: String): Boolean {
        val success = repository.importData(json)
        if (success) {
            appData = repository.loadData()
            selectedHabitId = habits.firstOrNull()?.id
        }
        return success
    }
    
    fun getDataPath(): String = repository.getDataFilePath()
    
    fun getTotalLeavesAllHabits(): Int {
        return habits.sumOf { it.completions.count { entry -> entry.value } }
    }
    
    fun getOverallStreak(): Int {
        val today = LocalDate.now()
        var streak = 0
        var current = today
        
        while (true) {
            val allCompleted = habits.isNotEmpty() && habits.all { 
                it.isCompletedOn(current) == true 
            }
            if (allCompleted) {
                streak++
                current = current.minusDays(1)
            } else {
                break
            }
        }
        
        return streak
    }
    
    private fun save() {
        repository.saveData(appData)
    }
    
    fun saveAndExit() {
        save()
    }
}
