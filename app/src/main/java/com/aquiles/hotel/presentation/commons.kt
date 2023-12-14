package com.aquiles.hotel.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import com.aquiles.hotel.UriPathFinder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.isGranted


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChooseFile(modifier: Modifier,onFileChoosen : (String)->Unit){


    val finder  =  remember {
        UriPathFinder()
    }
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )


    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult =
        {uri->

          val path = uri?.let { finder.getPath(context, it) };
            path?.let {
                onFileChoosen(it)
            }
        })

    Card(
        elevation = 0.dp,
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                if (permissionState.status.isGranted) {
                    filePickerLauncher.launch("*/*")
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
            .border(1.dp, Color.Black, RoundedCornerShape(25.dp)),
        shape = RoundedCornerShape(25.dp),
    ){

        Text(text = "Choose a picture",modifier= Modifier.padding(10.dp))

    }
}




@Composable
fun <T> QListView(
    buttontext : String,
    items: List<T>,
    onCreateItem: () -> Unit,

    itemContent: @Composable (item: T) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {

        // Display the list of  items
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(0.9f)) {
            items(items) { item ->
                itemContent(item)
            }
        }

        // Add a new  button
        if(buttontext.isNotEmpty()) {
            Button(
                onClick = onCreateItem,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .weight(0.1f)
            ) {
                Text(buttontext)
            }
        }
    }
}


@Composable
fun DrawerSection(pair: Pair<String,ImageVector>) = Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){

    Icon(imageVector = pair.second, contentDescription =null,tint = Color.Blue )
    Spacer(modifier = Modifier.width(2.dp))
    Text(
        text = pair.first,

        modifier = Modifier.fillMaxWidth(),
        fontSize = 22.sp
    )
}


