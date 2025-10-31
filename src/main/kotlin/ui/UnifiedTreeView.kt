import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.*

@Composable
fun UnifiedTreeView(viewModel: HabitViewModel) {
    val totalLeaves = viewModel.getTotalLeavesAllHabits()
    val overallStreak = viewModel.getOverallStreak()
    val totalCheckIns = viewModel.settings.totalCheckIns
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Your Habit Tree 🌳",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        if (overallStreak > 0 || totalLeaves > 0) {
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (overallStreak > 0) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔥", style = MaterialTheme.typography.headlineMedium)
                            Text(
                                "$overallStreak",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Perfect Days",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🍃", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "$totalLeaves",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Total Leaves",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("✅", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "$totalCheckIns",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Check-Ins",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        MotivationalBanner(totalLeaves, overallStreak, totalCheckIns)
        
        Spacer(Modifier.height(16.dp))
        
        UnifiedTreeVisualization(viewModel)
        
        Spacer(Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Today's Habits",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            if (viewModel.habits.isNotEmpty()) {
                val completedToday = viewModel.habits.count { 
                    it.isCompletedOn(LocalDate.now()) == true 
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (completedToday == viewModel.habits.size) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 1.dp
                ) {
                    Text(
                        "$completedToday / ${viewModel.habits.size}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (completedToday == viewModel.habits.size) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(Modifier.height(12.dp))
        
        if (viewModel.habits.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🌱", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Start Your Journey",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Click the + button in the sidebar to create your first habit",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                viewModel.habits.forEach { habit ->
                    TodayHabitCard(habit, viewModel)
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            WeeklyCalendarView(viewModel)
        }
    }
}

@Composable
fun WeeklyCalendarView(viewModel: HabitViewModel) {
    val today = LocalDate.now()
    val daysOfWeek = (0..6).map { today.minusDays((6 - it).toLong()) }
    
    Column {
        Text(
            "This Week's Progress",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    daysOfWeek.forEach { date ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                date.format(DateTimeFormatter.ofPattern("EEE")),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal
                            )
                            Spacer(Modifier.height(8.dp))
                            val completed = viewModel.habits.count { 
                                it.isCompletedOn(date) == true 
                            }
                            val total = viewModel.habits.size
                            
                            Surface(
                                shape = CircleShape,
                                color = when {
                                    total == 0 -> MaterialTheme.colorScheme.surfaceVariant
                                    completed == total && total > 0 -> MaterialTheme.colorScheme.primaryContainer
                                    completed > 0 -> MaterialTheme.colorScheme.secondaryContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                                tonalElevation = if (date == today) 4.dp else 1.dp,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "${date.dayOfMonth}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (total > 0) {
                                            Text(
                                                "$completed/$total",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MotivationalBanner(totalLeaves: Int, overallStreak: Int, totalCheckIns: Int) {
    val message = when {
        totalLeaves == 0 -> "🌱 Plant your first leaf today!"
        totalLeaves < 5 -> "💪 Great start! Keep the momentum!"
        totalLeaves < 10 -> "🌿 Your tree is growing! Stay consistent!"
        totalLeaves < 25 -> "🌳 Impressive! Your habits are taking root!"
        totalLeaves < 50 -> "✨ Amazing progress! You're unstoppable!"
        overallStreak >= 7 -> "🏆 LEGENDARY! 7+ perfect days in a row!"
        else -> "🎉 WOW! ${totalLeaves} leaves! You're a habit master!"
    }
    
    val achievement = when {
        totalCheckIns >= 100 -> "🏅 Century Club"
        totalCheckIns >= 50 -> "⭐ Half Century"
        totalCheckIns >= 30 -> "💎 Monthly Master"
        totalCheckIns >= 7 -> "🔰 Week Warrior"
        else -> null
    }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("💡", style = MaterialTheme.typography.displaySmall)
                Text(
                    message,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            if (achievement != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    tonalElevation = 4.dp
                ) {
                    Text(
                        achievement,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TodayHabitCard(habit: Habit, viewModel: HabitViewModel) {
    val today = LocalDate.now()
    val todayStatus = habit.isCompletedOn(today)
    val currentStreak = habit.getCurrentStreak()
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(habit.emoji, style = MaterialTheme.typography.displayMedium)
                Column {
                    Text(
                        habit.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (currentStreak > 0) {
                            Text(
                                "🔥 $currentStreak day${if (currentStreak > 1) "s" else ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        val totalDone = habit.completions.count { it.value }
                        Text(
                            "🍃 $totalDone leaf${if (totalDone != 1) "ves" else ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            FloatingActionButton(
                onClick = { viewModel.toggleHabitToday(habit.id) },
                containerColor = if (todayStatus == true) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (todayStatus == true) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(
                    if (todayStatus == true) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = "Toggle habit",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun UnifiedTreeVisualization(viewModel: HabitViewModel) {
    val allLeaves = remember(viewModel.habits, viewModel.settings.daysToShow) {
        val today = LocalDate.now()
        val daysToShow = viewModel.settings.daysToShow
        val startDate = today.minusDays(daysToShow.toLong() - 1)
        
        val leaves = mutableListOf<LeafData>()
        
        viewModel.habits.forEach { habit ->
            var current = startDate
            while (!current.isAfter(today)) {
                when (habit.isCompletedOn(current)) {
                    true -> leaves.add(LeafData("${habit.id}-${current}", true, habit.emoji))
                    false -> leaves.add(LeafData("${habit.id}-${current}", false, habit.emoji))
                    null -> {}
                }
                current = current.plusDays(1)
            }
        }
        
        leaves
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (allLeaves.filter { it.isActive }.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🌱", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Your tree awaits",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Complete habits to watch it grow!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                UnifiedTreeCanvas(allLeaves)
            }
        }
    }
}

data class LeafData(val id: String, val isActive: Boolean, val emoji: String = "🍃")

data class Branch(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val thickness: Float,
    val angle: Float,
    val depth: Int
)

@Composable
fun UnifiedTreeCanvas(leaves: List<LeafData>) {
    val activeLeaves = leaves.filter { it.isActive }
    val fallenLeaves = leaves.filter { !it.isActive }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val bottomY = size.height - 5
        val treeHeight = size.height * 0.82f
        
        val branches = mutableListOf<Branch>()
        
        fun generateBranches(
            x: Float,
            y: Float,
            angle: Float,
            depth: Int,
            length: Float,
            thickness: Float
        ) {
            if (depth == 0 || length < 8f) return
            
            val radians = Math.toRadians(angle.toDouble())
            val endX = x + (cos(radians) * length).toFloat()
            val endY = y - (sin(radians) * length).toFloat()
            
            branches.add(Branch(x, y, endX, endY, thickness, angle, depth))
            
            val newLength = length * 0.68f
            val newThickness = max(thickness * 0.65f, 1.5f)
            val angleSpread = 28f
            
            generateBranches(endX, endY, angle - angleSpread, depth - 1, newLength, newThickness)
            generateBranches(endX, endY, angle + angleSpread, depth - 1, newLength, newThickness)
        }
        
        generateBranches(
            x = centerX,
            y = bottomY,
            angle = 90f,
            depth = 8,
            length = treeHeight * 0.38f,
            thickness = 28f
        )
        
        branches.sortedBy { -it.thickness }.forEach { branch ->
            val branchColor = when {
                branch.thickness > 18f -> Color(0xFF5D4037)
                branch.thickness > 10f -> Color(0xFF6D4C41)
                branch.thickness > 5f -> Color(0xFF795548)
                else -> Color(0xFF8D6E63)
            }
            
            drawLine(
                color = branchColor,
                start = Offset(branch.startX, branch.startY),
                end = Offset(branch.endX, branch.endY),
                strokeWidth = branch.thickness
            )
        }
        
        val leafBranches = branches
            .filter { it.thickness < 6f && it.depth <= 4 }
            .shuffled()
        
        val leavesToPlace = min(activeLeaves.size, leafBranches.size)
        
        for (i in 0 until leavesToPlace) {
            val branch = leafBranches[i]
            val offsetDistance = 18f
            val offsetAngle = Math.toRadians(branch.angle.toDouble())
            val x = branch.endX + cos(offsetAngle).toFloat() * offsetDistance
            val y = branch.endY - sin(offsetAngle).toFloat() * offsetDistance
            
            val leafRotation = branch.angle - 90f + ((i * 23) % 30 - 15)
            
            drawLeafShape(
                Offset(x, y),
                Color(0xFF2E7D32),
                1f,
                leafRotation
            )
        }
        
        val fallenSpacing = 40f
        val maxFallenPerRow = ((size.width - 100) / fallenSpacing).toInt().coerceAtLeast(1)
        fallenLeaves.forEachIndexed { index, _ ->
            val row = index / maxFallenPerRow
            val col = index % maxFallenPerRow
            val rowOffset = if (row % 2 == 1) fallenSpacing / 2 else 0f
            val x = 50f + col * fallenSpacing + rowOffset + ((index * 17) % 15 - 7)
            val y = bottomY - 25f + row * 12f
            if (y >= 10f && y < size.height - 10f) {
                drawLeafShape(
                    Offset(x, y),
                    Color(0xFF8D6E63),
                    0.7f,
                    (index * 47f) % 360
                )
            }
        }
    }
}

fun DrawScope.drawLeafShape(position: Offset, color: Color, alpha: Float, rotation: Float) {
    rotate(rotation, position) {
        val path = Path().apply {
            moveTo(position.x, position.y)
            cubicTo(
                position.x - 14, position.y - 9,
                position.x - 18, position.y - 25,
                position.x, position.y - 35
            )
            cubicTo(
                position.x + 18, position.y - 25,
                position.x + 14, position.y - 9,
                position.x, position.y
            )
        }
        drawPath(path, color.copy(alpha = alpha))
        
        drawLine(
            color = color.copy(alpha = alpha * 0.5f),
            start = Offset(position.x, position.y),
            end = Offset(position.x, position.y - 30),
            strokeWidth = 2f
        )
    }
}
