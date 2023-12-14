package com.aquiles.hotel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun  FirstTime(
    modifier: Modifier,
    navController: NavController
){

   Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

       Text(text = "It sounds that its your first time ", fontWeight = FontWeight.Bold)

       Text(text = "please  start the  server  and then  click on the link\n " +
               "your'll be redirected to the browser \n  Then  click on  the button to scan  the qr code " +
               " ")

            Spacer(modifier = Modifier.height(50.dp))

       ScanQRCodeButton {
          navController.navigate("scanui")
       }


   }

}


@Composable
fun ScanQRCodeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.QrCode,
                contentDescription = "Scan QR code",
            )
            Spacer(Modifier.width(8.dp))
            Text(text = "Scan QR code")
        }
    }
}