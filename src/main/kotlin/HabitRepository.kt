import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate

class HabitRepository {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    private val dataDir = File(System.getProperty("user.home"), ".microhabittree")
    private val dataFile = File(dataDir, "habits.json")
    
    init {
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }
    }
    
    fun loadData(): AppData {
        return try {
            if (dataFile.exists()) {
                val jsonText = dataFile.readText()
                json.decodeFromString<AppData>(jsonText)
            } else {
                AppData()
            }
        } catch (e: Exception) {
            println("Error loading data: ${e.message}")
            AppData()
        }
    }
    
    fun saveData(data: AppData) {
        try {
            val jsonText = json.encodeToString(data)
            dataFile.writeText(jsonText)
        } catch (e: Exception) {
            println("Error saving data: ${e.message}")
        }
    }
    
    fun exportData(): String {
        return json.encodeToString(loadData())
    }
    
    fun importData(jsonText: String): Boolean {
        return try {
            val data = json.decodeFromString<AppData>(jsonText)
            saveData(data)
            true
        } catch (e: Exception) {
            println("Error importing data: ${e.message}")
            false
        }
    }
    
    fun getDataFilePath(): String = dataFile.absolutePath
}
