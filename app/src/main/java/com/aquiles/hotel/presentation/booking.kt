package com.aquiles.hotel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aquiles.hotel.models.Profile
import com.aquiles.hotel.models.Reservation


@Composable
fun BookingItem(
    reservation: Reservation,
    onDeleteClick: (Reservation) -> Unit,
)
 {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {



            SingleRow(title = "Room id ", value = reservation.room_id)
            SingleRow(title = "Client Name ", value = reservation.client_name)
            SingleRow(title = "Start date " , value = reservation.start_date)
            SingleRow(title = "End Date ", value =  reservation.end_date)


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.weight(1f))
      /*          IconButton(
                    onClick = { onEditClick(reservation.id!!) },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit reservation",
                        tint = Color.Blue
                    )
                }*/
                IconButton(
                    onClick = { onDeleteClick(reservation) },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete reservation",
                        tint = Color.Blue
                    )
                }
            }
        }


    }
}