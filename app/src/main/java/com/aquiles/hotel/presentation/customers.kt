package com.aquiles.hotel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aquiles.hotel.models.Client
import com.aquiles.hotel.models.Room
import com.aquiles.hotel.presentation.QListView




@Composable
fun CustomerItem(
     client: Client,
    onDeleteClick: (Client) -> Unit,
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
            SingleRow(title = "Client ID", value = "${client.id}")

            SingleRow(title = "Name", value = client.name)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = { onDeleteClick(client) },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete client",
                        tint = Color.Blue
                    )
                }
            }
        }


    }
}
