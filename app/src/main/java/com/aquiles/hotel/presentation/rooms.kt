package com.aquiles.hotel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aquiles.hotel.models.Room








@Composable
fun RoomList(
    rooms: List<Room>,
    onCreateRoom: () -> Unit,
    onEditRoom: (room : Room) -> Unit,
    onBook: (room : Room) -> Unit,
    onDeleteRoom: (room : Room) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {

        // Display the list of room items
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(0.9f)) {
            items(rooms) { room ->
                RoomItem(
                    room = room,
                    onEditClick = { onEditRoom(room) },
                    onDeleteClick = { onDeleteRoom(room) },
                    onBook = { onBook(room)}
                )
            }
        }

        // Add a new room button
        Button(
            onClick = onCreateRoom,
            modifier = Modifier.padding(top = 8.dp).weight(0.1f)
        ) {
            Text("Add Room")
        }
    }
}



@Composable
fun RoomItem(
    room: Room,
    onBook: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
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
            SingleRow(title = "Room", value = "${room.id}")
            SingleRow(title = "Capacity", value = "${room.capacity}")
            SingleRow(title = "Description", value =room.description )
            SingleRow(title = "Price", value = "${room.price}")
            SingleRow(title = "Is available", value = if (room.is_available) "Yes" else "No")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick =onBook,
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Hotel,
                        contentDescription = "book",
                        tint = Color.Blue
                    )
                }
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit room",
                        tint = Color.Blue
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete room",
                        tint = Color.Blue
                    )
                }
            }
        }


    }
}


@Composable
 fun SingleRow(title :String, value : String) =Row{

    Text(text = "$title :", fontWeight = FontWeight.Bold)
    Text(text = value, maxLines = 1, overflow = TextOverflow.Ellipsis)

}