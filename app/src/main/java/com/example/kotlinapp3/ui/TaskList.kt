package com.example.kotlinapp3.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp3.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList() {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application
    val viewModel: TaskViewModel = viewModel { TaskViewModel(application) }

    val tasks by viewModel.tasks.collectAsState()
    val taskCount by viewModel.taskCount.collectAsState()
    val completedTaskCount by viewModel.completedTaskCount.collectAsState()
    val pendingTaskCount = taskCount - completedTaskCount

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Task Manager",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        // Task Counter
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$taskCount",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Total Tasks",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$completedTaskCount",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$pendingTaskCount",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Pending",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Task List
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tasks yet!\nTap the + button to add one.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleCompletion = viewModel::toggleTaskCompletion,
                        onDelete = viewModel::deleteTask
                    )
                }
            }
        }
        // button add
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add task"
            )
        }
    }

    // add task dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                viewModel.newTaskTitle = ""
                viewModel.newTaskDescription = ""
            },
            title = { Text("Add New Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = viewModel.newTaskTitle,
                        onValueChange = viewModel::updateNewTaskTitle,
                        label = { Text("Task Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.newTaskDescription,
                        onValueChange = viewModel::updateNewTaskDescription,
                        label = { Text("Description (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addNewTask()
                        showAddDialog = false
                    },
                    enabled = viewModel.newTaskTitle.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        viewModel.newTaskTitle = ""
                        viewModel.newTaskDescription = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
