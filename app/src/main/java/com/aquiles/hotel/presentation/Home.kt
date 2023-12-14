package com.aquiles.hotel

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquiles.hotel.presentation.QPushButton

@Composable
fun Home(modifier: Modifier,onNavigate : (String) -> Unit){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .then(modifier)
        ) {

            Row(Modifier.fillMaxWidth()) {
                QPushButton(onClick = { onNavigate("viewprofil") }, text = "Profil")
                Spacer(Modifier.width(50.dp))
                QPushButton(onClick = { onNavigate("rooms") }, text = "Rooms")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth()) {
                QPushButton(onClick = { onNavigate("customers") }, text = "customers")
                Spacer(Modifier.width(50.dp))
                QPushButton(onClick = { onNavigate("booking") }, text = "Booking")

            }

        }
    }
}