package com.aquiles.hotel.presentation.edition

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquiles.hotel.HotApplication
import com.aquiles.hotel.data.Repository
import com.aquiles.hotel.data.makePostRequest
import com.aquiles.hotel.data.makeRequest
import com.aquiles.hotel.getServerIp
import com.aquiles.hotel.models.Room
import com.aquiles.hotel.showToast
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditRoomForm(
    room: Room,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    var capacity by remember { mutableStateOf(room.capacity.toString()) }
    var description by remember { mutableStateOf(room.description) }
    var price by remember { mutableStateOf(room.price.toString()) }
    var isAvailable by remember { mutableStateOf(room.is_available) }

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
                text = "${if (room.id == null) "Add" else "Edit"} Room",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacity") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Checkbox(
                checked = isAvailable,
                onCheckedChange = { isAvailable = it },
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(
                Modifier.padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {

                        isLoading = true
                        scope.launch(Dispatchers.IO) {
                            val gson = Gson()
                            //if id is null => we are creating a  new room otherwise we're editing an existing one
                            var edited = Room(
                                room.id,
                                capacity.toInt(),
                                description,
                                price.toInt(),
                                isAvailable
                            )

                            if (edited.id == null) {


                                edited = edited.copy(id=0)

                                makePostRequest(
                                    endpoint = "addroom",
                                    json = gson.toJson(edited),
                                    onSuccess = { json ->
                                        json?.let {

                                            val result = gson.fromJson(json, Room::class.java)
                                            scope.launch {
                                                Repository.insertRoom(result)
                                                withContext(Dispatchers.Main) {
                                                    isLoading = false
                                                    onSave()
                                                   context.showToast("Room added successfully")

                                                }
                                            }

                                        }

                                    },
                                    onFailure = {
                                        println("failed $it")
                                         scope.launch(Dispatchers.Main) {
                                             isLoading = false
                                             context.showToast(" error !")
                                         }


                                    }
                                )
                            } else {

                                makePostRequest(
                                    endpoint = "updateroom",
                                    json = gson.toJson(edited),
                                    onSuccess = {
                                        scope.launch {
                                            Repository.updateRoom(edited)
                                            withContext(Dispatchers.Main) {
                                                isLoading = false
                                                onSave()
                                                context.showToast("room updated successfully")
                                            }
                                        }
                                    },
                                    onFailure = {

                                        scope.launch(Dispatchers.Main) {
                                            isLoading = false
                                            context.showToast(" failed to update  room")
                                        }
                                    }

                                )

                            }
                        }

                    }
                ) {
                    Text(text = "Save")
                }

                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        isLoading = false
                        onCancel() }
                ) {
                    Text(text = "Cancel")
                }
            }
        }

        if (isLoading){
            LinearProgressIndicator()
        }
    }
}
