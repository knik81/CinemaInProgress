package com.best.nikflix.ui.profile.profile_page.compose

import androidx.compose.foundation.border
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.best.nikflix.R

@Composable
fun DialogCompose(
    isOpenAlertDialog: Boolean,
    returnCollectionName: (String) -> Unit
) {
    //var openAlertDialog = isOpenAlertDialog//remember { mutableStateOf(fa) }
    when {
        isOpenAlertDialog ->
            AlertDialogExample(
                onDismissRequest = { returnCollectionName("") },
                onConfirmation = { collectionName ->
                    //Log.d("Nik", " $collectionName")
                    returnCollectionName(collectionName)
                    //openAlertDialog = false
                },
                dialogTitle = "Создание новой коллекции ",
            )
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    dialogTitle: String,
) {
    val inputText = remember {
        mutableStateOf("")
    }
    AlertDialog(
        modifier = Modifier
            .border(3.dp, colorResource(R.color.blackС)),
        title = {
            Text(text = dialogTitle)
        },
        text = {
            TextField(colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
                value = inputText.value,
                onValueChange = { inputText.value = it },
                placeholder = {
                    Text(
                        text = "введите название ...",
                        fontSize = 12.sp
                    )
                })
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation(inputText.value) }
            ) { Text("Создать") }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Отмена")
            }
        }
    )
}