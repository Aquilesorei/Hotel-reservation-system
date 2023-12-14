package com.aquiles.hotel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import com.aquiles.hotel.models.Client
import com.aquiles.hotel.models.Profile

@Composable
fun ProfilView(profile: Profile) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AsyncImage(
            model= profile.profil_picture,
            contentDescription = "Profil picture",
            modifier = Modifier
                .size(120.dp)
                .padding(vertical = 16.dp)
                .clip(shape = CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Text("Name: ${profile.first_name} ${profile.last_name}")
        Text("Age: ${profile.age}")
        Text("Email: ${profile.email}")
     //   Text("Password: ${profil.password}")

    }
}




@Composable
fun ProfileItem(
    profile: Profile,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
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


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("http://${getServerIp(LocalContext.current)}:8080/download/${profile.profil_picture}")
                    .crossfade(true)
                    .build(),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )

            SingleRow(title = "First Name", value = profile.first_name)
            SingleRow(title ="Last Name " , value = profile.last_name)
            SingleRow(title = "Age", value =  "${profile.age}")
            SingleRow(title = "Email", value = profile.email)


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { onEditClick(profile.id!!) },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit profile",
                        tint = Color.Blue
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(profile.id!!) },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete profile",
                        tint = Color.Blue
                    )
                }
            }
        }


    }
}