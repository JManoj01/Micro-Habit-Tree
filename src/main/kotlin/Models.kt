import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Habit(
    val id: String,
    val name: String,
    val emoji: String = "🌱",
    val completions: Map<String, Boolean> = emptyMap(),
    val createdDate: String = LocalDate.now().toString()
) {
    fun isCompletedOn(date: LocalDate): Boolean? {
        return completions[date.toString()]
    }
    
    fun withToggled(date: LocalDate): Habit {
        val dateStr = date.toString()
        val newValue = when (completions[dateStr]) {
            true -> null
            false -> true
            null -> true
        }
        val newCompletions = if (newValue == null) {
            completions - dateStr
        } else {
            completions + (dateStr to newValue)
        }
        return copy(completions = newCompletions)
    }
    
    fun getConsistencyPercentage(days: Int): Int {
        val endDate = LocalDate.now()
        val createdDate = LocalDate.parse(this.createdDate)
        val startDate = if (createdDate.isAfter(endDate.minusDays(days.toLong() - 1))) {
            createdDate
        } else {
            endDate.minusDays(days.toLong() - 1)
        }
        
        var completed = 0
        var total = 0
        
        var current = startDate
        while (!current.isAfter(endDate)) {
            val status = completions[current.toString()]
            if (status != null) {
                total++
                if (status) completed++
            }
            current = current.plusDays(1)
        }
        
        return if (total > 0) (completed * 100) / total else 0
    }
    
    fun getCurrentStreak(): Int {
        var streak = 0
        var current = LocalDate.now()
        
        while (completions[current.toString()] == true) {
            streak++
            current = current.minusDays(1)
        }
        
        return streak
    }
}

@Serializable
data class AppSettings(
    val daysToShow: Int = 14,
    val isDarkTheme: Boolean = false,
    val totalCheckIns: Int = 0,
    val lastCheckInDate: String? = null
)

@Serializable
data class AppData(
    val habits: List<Habit> = emptyList(),
    val settings: AppSettings = AppSettings()
)
