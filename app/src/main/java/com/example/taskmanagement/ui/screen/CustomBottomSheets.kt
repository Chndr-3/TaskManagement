package com.example.taskmanagement.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanagement.data.Priority
import com.example.taskmanagement.data.Task
import com.example.taskmanagement.ui.theme.strongRed
import com.example.taskmanagement.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheets(
    modalSheetState: SheetState,
    viewModel: HomeViewModel,
    task: Task? = null,
    onDismissRequest: () -> Unit,
) {
    var taskName by remember {
        mutableStateOf(
            if (task?.name == null) {
                ""
            } else {
                task.name
            }
        )
    }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var isDatePickerDialogVisible by remember { mutableStateOf(false) }
    var taskDate by remember {
        mutableStateOf((datePickerState.selectedDateMillis?.let {
            if (task?.date == null) {
                convertMillisToDate(
                    it
                )
            } else {
                task.date
            }

        }.toString()))
    }
    val coroutineScope = rememberCoroutineScope()
    val options = listOf("Penting", "Sedang", "Biasa")

    var chosenDropdown by remember {
        mutableStateOf(
            if (task?.name == null) {
                "Penting"
            } else {
                when(task.priority){
                    Priority.HIGH -> "Penting"
                    Priority.MEDIUM -> "Sedang"
                    Priority.LOW -> "Tidak Penting"
                }
            }
        )
    }
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

        modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 1.5).dp),
        onDismissRequest = onDismissRequest,
        sheetState = modalSheetState,
        dragHandle = { BottomSheetDefaults.ExpandedShape }) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight(),

            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                if (task?.name == null) {
                    "Add a New Task"
                } else {
                    "Update Task"
                },
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
                    label = { Text("Prioritas") },
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

            Button(colors = ButtonDefaults.buttonColors(strongRed), onClick = {

                onDismissRequest()
                coroutineScope.launch {

                    viewModel.insertTask(
                        Task(
                            taskId = if (task?.taskId != null) task.taskId else null ,
                            name = taskName,
                            date = taskDate,
                            priority = selectedOptionText,
                        )
                    )

                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    if (task?.name == null) {
                        "Insert Task"
                    } else {
                        "Update Task"
                    }
                )
            }

        }

    }


}