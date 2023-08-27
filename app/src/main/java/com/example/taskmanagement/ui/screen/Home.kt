package com.example.taskmanagement.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanagement.data.Priority
import com.example.taskmanagement.data.Task
import com.example.taskmanagement.ui.theme.green
import com.example.taskmanagement.ui.theme.red
import com.example.taskmanagement.ui.theme.strongRed
import com.example.taskmanagement.ui.theme.yellow
import com.example.taskmanagement.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val tasks = viewModel.tasks.value
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val onLoading = viewModel.onLoading
    Scaffold(Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (showSheet) {


                    CustomBottomSheets(modalSheetState = modalSheetState, viewModel) {
                        showSheet = false

                }


            }
            Box (Modifier.fillMaxWidth()){

                Button(colors =ButtonDefaults.buttonColors(strongRed),onClick = {
                    coroutineScope.launch {
                        showSheet = true
                    }
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text("Add Task")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                reverseLayout = false,

                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {


                items(items = tasks, key = { it.taskId }) { task ->
                    AnimatedVisibility(
                        visible = !onLoading,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Box(Modifier.animateEnterExit(enter = scaleIn(), exit = scaleOut())) {
                            TaskCard(task) {
                                viewModel.deleteTask(task)
                            }
                        }
                    }
                }

            }
            AnimatedVisibility(visible = onLoading) {
                CircularProgressIndicator()

            }


        }


    }
}


@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    val cardColor = when (task.priority) {
        Priority.LOW -> green
        Priority.MEDIUM -> yellow
        Priority.HIGH -> red
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = task.taskId.toString())
                Text(
                    text = task.name,

                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.date,
                    color = Color.Black
                )
            }
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomBottomSheets(
    modalSheetState: SheetState,
    viewModel: HomeViewModel,
    onDissmissRequest: () -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var isDatePickerDialogVisible by remember { mutableStateOf(false) }
    var taskDate by remember {
        mutableStateOf((datePickerState.selectedDateMillis?.let {
            convertMillisToDate(
                it
            )
        }.toString()))
    }
    val coroutineScope = rememberCoroutineScope()
    val options = listOf("Penting", "Sedang", "Biasa")

    var chosenDropdown by remember { mutableStateOf("Penting") }
    val selectedOptionText = when (chosenDropdown) {
        "Penting" -> Priority.HIGH
        "Sedang" -> Priority.MEDIUM
        "Tidak Penting" -> Priority.LOW
        else -> {
            Priority.LOW
        }
    }
    var dropDownExposed by remember { mutableStateOf(false) }
    ModalBottomSheet(

        modifier= Modifier.height((LocalConfiguration.current.screenHeightDp / 1.5).dp),
        onDismissRequest = onDissmissRequest,
        sheetState = modalSheetState,
        dragHandle = { BottomSheetDefaults.ExpandedShape }) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight(),

            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                "Add A New Task",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            Column {
                Text("Task Name")
                OutlinedTextField(

                    value = taskName,
                    onValueChange = { taskName = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (isDatePickerDialogVisible) {
                DatePickerDialog(
                    confirmButton = {
                        Button(onClick = {
                            isDatePickerDialogVisible = false
                            taskDate =
                                datePickerState.selectedDateMillis?.let { convertMillisToDate(it) }
                                    .toString()
                        }) {
                            Text("Confirm")

                        }
                    },
                    content = { DatePicker(state = datePickerState) },
                    onDismissRequest = { isDatePickerDialogVisible = false })
            }
            Column {
                Text("Deadline")
                OutlinedTextField(
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black, // You can adjust the background color here
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                    ),
                    leadingIcon = { Icon(Icons.Filled.DateRange, "Date Picker") },
                    value = taskDate,
                    onValueChange = { isDatePickerDialogVisible = true },
                    readOnly = true,
                    enabled = false,

                    textStyle = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .widthIn(1.dp, Dp.Infinity)
                        .clickable { isDatePickerDialogVisible = true }
                )
            }
            ExposedDropdownMenuBox(
                expanded = dropDownExposed,
                onExpandedChange = {
                    dropDownExposed = !dropDownExposed
                }
            ) {
                OutlinedTextField(
                    value = chosenDropdown,
                    readOnly = true,
                    onValueChange = { },
                    label = { Text("Label") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dropDownExposed
                        )
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropDownExposed,
                    onDismissRequest = {
                        dropDownExposed = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                chosenDropdown = selectionOption
                                dropDownExposed = false
                            }
                        )
                    }
                }
            }
            
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.insertTask(
                        Task(
                            name = taskName,
                            date = taskDate,
                            priority = selectedOptionText,
                            visibility = true
                        )
                    )
                    onDissmissRequest
                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text("Insert Task")
            }

        }

    }


}

@SuppressLint("SimpleDateFormat")
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}