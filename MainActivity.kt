package com.example.studentsportfolio

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.studentsportfolio.data.Student
import com.example.studentsportfolio.ui.theme.StudentsPortfolioTheme
import com.example.studentsportfolio.viewmodel.StudentViewModel
import com.example.studentsportfolio.viewmodel.StudentViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: StudentViewModel by viewModels {
        StudentViewModelFactory((application as StudentsApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentsPortfolioTheme {
                val students by viewModel.allStudents.collectAsState(initial = emptyList())
                val scope = rememberCoroutineScope()
                var showAdd by remember { mutableStateOf(false) }
                var editingStudent by remember { mutableStateOf<Student?>(null) }

                Scaffold(
                    topBar = {
                        SmallTopAppBar(title = { Text("Students Portfolio Pro") })
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showAdd = true }) {
                            Text("+")
                        }
                    }
                ) { padding ->
                    if (showAdd) {
                        AddEditStudentDialog(
                            student = editingStudent,
                            onDismiss = { showAdd = false; editingStudent = null },
                            onSave = { s ->
                                scope.launch {
                                    if (s.id == 0L) viewModel.insert(s) else viewModel.update(s)
                                    showAdd = false; editingStudent = null
                                }
                            }
                        )
                    }

                    StudentList(
                        students = students,
                        modifier = Modifier.padding(padding),
                        onEdit = { editingStudent = it; showAdd = true },
                        onDelete = { s ->
                            scope.launch { viewModel.delete(s) }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStudentDialog(student: Student?, onDismiss: () -> Unit, onSave: (Student) -> Unit) {
    var name by remember { mutableStateOf(student?.name ?: "") }
    var grade by remember { mutableStateOf(student?.grade ?: "") }
    var roll by remember { mutableStateOf(student?.rollNumber ?: "") }
    var parent by remember { mutableStateOf(student?.parentContact ?: "") }
    var notes by remember { mutableStateOf(student?.notes ?: "") }
    var photoUri by remember { mutableStateOf(student?.photoUri ?: "") }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { photoUri = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val s = Student(
                    id = student?.id ?: 0L,
                    name = name,
                    grade = grade,
                    rollNumber = roll,
                    parentContact = parent,
                    notes = notes,
                    photoUri = photoUri
                )
                onSave(s)
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text(if (student == null) "Add Student" else "Edit Student") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = grade, onValueChange = { grade = it }, label = { Text("Class / Grade") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = roll, onValueChange = { roll = it }, label = { Text("Roll Number") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = parent, onValueChange = { parent = it }, label = { Text("Parent Contact") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes / Achievements") })
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(value = photoUri, onValueChange = { photoUri = it }, label = { Text("Photo URI (optional)") }, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = { pickImageLauncher.launch("image/*") }) { Text("Pick") }
                }
                Text("Tip: Use Pick to select a photo from your gallery. The app stores the URI.", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}

@Composable
fun StudentList(students: List<Student>, modifier: Modifier = Modifier, onEdit: (Student) -> Unit, onDelete: (Student) -> Unit) {
    Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
        if (students.isEmpty()) {
            Text("No students yet. Tap + to add one.", style = MaterialTheme.typography.bodyLarge)
        } else {
            androidx.compose.foundation.lazy.LazyColumn {
                items(students.size) { idx ->
                    val s = students[idx]
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            val painter = rememberAsyncImagePainter(model = if (s.photoUri.isNullOrBlank()) null else Uri.parse(s.photoUri))
                            Image(painter = painter, contentDescription = null, modifier = Modifier.size(72.dp).clickable { onEdit(s) }, contentScale = ContentScale.Crop)
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(s.name, style = MaterialTheme.typography.titleMedium)
                                Text("${s.grade} • Roll: ${s.rollNumber}", style = MaterialTheme.typography.bodySmall)
                                Text("Parent: ${s.parentContact}", style = MaterialTheme.typography.bodySmall)
                                Spacer(Modifier.height(6.dp))
                                Text(s.notes ?: "", style = MaterialTheme.typography.bodySmall, maxLines = 2)
                            }
                            Column {
                                TextButton(onClick = { onEdit(s) }) { Text("Edit") }
                            TextButton(onClick = { onDelete(s) }) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }
    }
}
