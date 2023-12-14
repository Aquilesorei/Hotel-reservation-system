package com.aquiles.hotel.presentation.edition

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquiles.hotel.models.Client


@Composable
fun EditClientForm(
    client: Client,
    onSave: (Client) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(client.name) }

    var isLoading by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Edit Client",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )


            Row(
                Modifier.padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        onSave(
                            Client(
                                client.id,
                                name
                            )
                        )
                    }
                ) {
                    Text(text = "Save")
                }

                Button(
                    onClick ={
                        isLoading = false
                        onCancel() }
                ) {
                    Text(text = "Cancel")
                }
            }

            if (isLoading){
                LinearProgressIndicator()
            }
        }
    }
}
