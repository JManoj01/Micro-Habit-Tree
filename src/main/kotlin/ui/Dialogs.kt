import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddEditHabitDialog(
    initialName: String = "",
    initialEmoji: String = "🌱",
    isEdit: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedEmoji by remember { mutableStateOf(initialEmoji) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    
    val emojiCategories = mapOf(
        "Health" to listOf("🏃", "💪", "🧘", "🚴", "��️", "🤸", "🧗", "⛹️"),
        "Food" to listOf("🥗", "🥑", "🍎", "🥤", "💧", "🍵", "🥛", "🍌"),
        "Study" to listOf("📚", "📖", "✍️", "🎓", "��", "💻", "🖊️", "📔"),
        "Self-Care" to listOf("😴", "🛀", "💆", "🧴", "😌", "🕯️", "🎧", "🎵"),
        "Work" to listOf("💼", "📊", "📈", "⚡", "🎯", "✅", "📅", "🔔"),
        "Creative" to listOf("🎨", "🎭", "📷", "🎬", "✨", "🎪", "🎸", "🖌️"),
        "Social" to listOf("👥", "📞", "💬", "🤝", "❤️", "👨‍👩‍👧", "🎉", "🌟"),
        "Other" to listOf("🌱", "🌿", "🌳", "⭐", "🔥", "💎", "🏆", "🎁")
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                if (isEdit) "Edit Habit" else "Create New Habit",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    placeholder = { Text("e.g., Morning Exercise") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    "Choose an Icon",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { showEmojiPicker = !showEmojiPicker },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(selectedEmoji, style = MaterialTheme.typography.displaySmall)
                            Text(
                                "Tap to change",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text("▼", style = MaterialTheme.typography.bodySmall)
                    }
                }
                
                if (showEmojiPicker) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            emojiCategories.forEach { (category, emojis) ->
                                Text(
                                    category,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(8),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.height(50.dp)
                                ) {
                                    items(emojis) { emoji ->
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (selectedEmoji == emoji) 
                                                        MaterialTheme.colorScheme.primaryContainer 
                                                    else 
                                                        MaterialTheme.colorScheme.surfaceVariant
                                                )
                                                .clickable { selectedEmoji = emoji },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(emoji, style = MaterialTheme.typography.titleLarge)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, selectedEmoji) },
                enabled = name.isNotBlank()
            ) {
                Text(if (isEdit) "Save Changes" else "Create Habit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SettingsDialog(
    viewModel: HabitViewModel,
    onDismiss: () -> Unit
) {
    var daysToShow by remember { mutableStateOf(viewModel.settings.daysToShow) }
    var isDarkTheme by remember { mutableStateOf(viewModel.settings.isDarkTheme) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Display Period", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = daysToShow == 7,
                        onClick = { daysToShow = 7 },
                        label = { Text("7 days") }
                    )
                    FilterChip(
                        selected = daysToShow == 14,
                        onClick = { daysToShow = 14 },
                        label = { Text("14 days") }
                    )
                    FilterChip(
                        selected = daysToShow == 30,
                        onClick = { daysToShow = 30 },
                        label = { Text("30 days") }
                    )
                }
                
                HorizontalDivider()
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark Theme", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it }
                    )
                }
                
                HorizontalDivider()
                
                Text("Data Management", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Storage: ${viewModel.getDataPath()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Button(
                    onClick = { showExportDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Export / Import Data")
                }
                
                Button(
                    onClick = { showClearAllDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Clear All Data")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.updateSettings(daysToShow, isDarkTheme)
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    if (showExportDialog) {
        ExportImportDialog(
            viewModel = viewModel,
            onDismiss = { showExportDialog = false }
        )
    }
    
    if (showClearAllDialog) {
        ClearAllDataDialog(
            viewModel = viewModel,
            onDismiss = { showClearAllDialog = false },
            onConfirm = {
                viewModel.clearAllData()
                showClearAllDialog = false
                onDismiss()
            }
        )
    }
}

@Composable
fun ClearAllDataDialog(
    viewModel: HabitViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val totalHabits = viewModel.habits.size
    val totalLeaves = viewModel.getTotalLeavesAllHabits()
    val totalCheckIns = viewModel.settings.totalCheckIns
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { 
            Icon(
                Icons.Default.Warning, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            ) 
        },
        title = { 
            Text(
                "Clear All Data?",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "This will permanently delete EVERYTHING:",
                    fontWeight = FontWeight.Bold
                )
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    tonalElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🗑️", style = MaterialTheme.typography.bodyMedium)
                            Text("$totalHabits habit${if (totalHabits != 1) "s" else ""}")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🍃", style = MaterialTheme.typography.bodyMedium)
                            Text("$totalLeaves leaves")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("✅", style = MaterialTheme.typography.bodyMedium)
                            Text("$totalCheckIns check-ins")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("📊", style = MaterialTheme.typography.bodyMedium)
                            Text("All progress & statistics")
                        }
                    }
                }
                
                Text(
                    "⚠️ This action CANNOT be undone!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "Consider exporting your data first as a backup.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Yes, Delete Everything")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ExportImportDialog(
    viewModel: HabitViewModel,
    onDismiss: () -> Unit
) {
    var jsonText by remember { mutableStateOf("") }
    var showExport by remember { mutableStateOf(true) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (showExport) "Export Data" else "Import Data") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = showExport,
                        onClick = { 
                            showExport = true
                            jsonText = viewModel.exportToJson()
                        },
                        label = { Text("Export") }
                    )
                    FilterChip(
                        selected = !showExport,
                        onClick = { 
                            showExport = false
                            jsonText = ""
                        },
                        label = { Text("Import") }
                    )
                }
                
                if (showExport) {
                    LaunchedEffect(Unit) {
                        jsonText = viewModel.exportToJson()
                    }
                }
                
                OutlinedTextField(
                    value = jsonText,
                    onValueChange = { jsonText = it },
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    readOnly = showExport,
                    label = { Text(if (showExport) "Copy this JSON" else "Paste JSON here") }
                )
            }
        },
        confirmButton = {
            if (!showExport) {
                Button(
                    onClick = {
                        if (viewModel.importFromJson(jsonText)) {
                            onDismiss()
                        }
                    },
                    enabled = jsonText.isNotBlank()
                ) {
                    Text("Import")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
