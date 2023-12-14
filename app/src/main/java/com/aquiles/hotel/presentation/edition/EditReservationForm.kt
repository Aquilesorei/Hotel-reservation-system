package com.aquiles.hotel.presentation.edition


import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquiles.hotel.HotApplication
import com.aquiles.hotel.SingleRow
import com.aquiles.hotel.data.Repository
import com.aquiles.hotel.data.makeRequest
import com.aquiles.hotel.getServerIp
import com.aquiles.hotel.models.Client
import com.aquiles.hotel.models.Reservation
import com.aquiles.hotel.models.Room
import com.aquiles.hotel.showToast
import com.aquiles.hotel.ui.theme.Purple500
import com.google.gson.Gson
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditReservationForm(
    reservation: Reservation,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var startDate by remember { mutableStateOf(reservation.start_date) }
    var endDate by remember { mutableStateOf(reservation.end_date) }
    var totalPrice by remember { mutableStateOf(reservation.total_price.toString()) }
    var isValidated by remember { mutableStateOf(reservation.is_validated) }
    var name  by remember{ mutableStateOf(reservation.client_name) }

    val dialogState = rememberMaterialDialogState()

    var currentDate by remember {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope()
     val context = LocalContext.current
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
                "Edit Reservation",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )


            MaterialDialog(
                dialogState = dialogState,
                buttons = {
                    positiveButton("Ok")
                    negativeButton("Cancel")
                }
            ) {
                datepicker { date ->
                    // Do stuff with java.time.LocalDate object which is passed in
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val formattedDate = date.format(formatter)

                    if (currentDate == 0) startDate = formattedDate else endDate = formattedDate
                }
            }


            SingleRow(title = "Romm ID ", value = reservation.room_id)
            // Show the Material Dialog when clicking on the date fields


            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("Start Date") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Purple500,
                        modifier = Modifier.clickable {
                            currentDate = 0
                            dialogState.show()
                        })
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)

            )

            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = { Text("End Date") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Purple500,
                        modifier = Modifier.clickable {
                            currentDate = 1
                            dialogState.show()
                        })
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            )



            Row(
                Modifier.padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {

                        isLoading = true
                        val edited = Reservation(
                            reservation.id,
                            name,
                            reservation.room_id,
                            startDate,
                            endDate,
                            totalPrice.toDouble(),
                            isValidated
                        )

                        scope.launch(Dispatchers.IO) {

                            makeRequest(
                                url = "http://${getServerIp(HotApplication.getAppContext())}:8080/book?name=$name&room_id=${edited.room_id}&start_date=${startDate}&end_date=${endDate}",
                                onSuccess = { json ->

                                    scope.launch(Dispatchers.IO) {
                                        json?.let {
                                            val res = Gson().fromJson(json, Reservation::class.java)

                                            Repository.insertReservation(res)

                                            withContext(Dispatchers.Main) {
                                                onSave()
                                              isLoading = false
                                              context.showToast("booked successfully")

                                            }

                                        }
                                    }
                                },
                                onFailure = {
                                    scope.launch(Dispatchers.Main) {
                                        isLoading = false
                                        context.showToast(" failed to  perform task please start the server !")
                                    }
                                }
                            )
                        }


                    }
                ) {
                    Text(text = "Save")
                }

                Spacer(Modifier.width(20.dp))
                Button(
                    onClick =  {
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
