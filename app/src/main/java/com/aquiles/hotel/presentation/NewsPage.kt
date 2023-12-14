package com.aquiles.hotel.presentation



import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import coil.compose.AsyncImage
import com.aquiles.hotel.DashBoardViewModel
import com.aquiles.hotel.data.New
import com.aquiles.hotel.formatDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsPage(state : DashBoardViewModel,itemClicked :(Int) -> Unit ,modifier: Modifier)
{

    val  size = state.news.size
    Log.d("viewa",size.toString())

    Box(modifier =modifier, contentAlignment = Alignment.Center )
    {

        LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp))
        {
            itemsIndexed(state.news)
            {
                index,info ->
                NewsItem(modifier = Modifier, onClick ={
                    state.currentUrl = info.url ?: ""
                    itemClicked(it)
                } , info =info)

                if(index != (state.news.size-1))
                {
                    Divider()
                }


            }
        }
        if(state.isLoading)
        {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.5f).then(modifier))
        }
    }




}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsItem(modifier :Modifier ,onClick : (Int) -> Unit,info : New)
{



        Row( modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(info.Nid!!) }
            )
        {
            AsyncImage(
                model = info.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp, 70.dp)
                    .clickable { onClick(info.Nid!!) }

            )

            NewDetails(modifier = Modifier.clickable { onClick(info.Nid!!)  }, title = info.title ?: "", author =info.author ?: "" , date = info.publishedAt ?: "")

            NewsIcon(icon = Icons.Filled.Favorite, modifier =Modifier.weight(0.15f), onClick =  { onClick(info.Nid!!) } )

        }


}


@Composable
fun NewsIcon(
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit = {}
)
{
    Image(
        imageVector = icon,
        contentDescription = "icon",
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewDetails(modifier :Modifier,title : String,author : String ,date : String,horizontalAlignment: Alignment.Horizontal = Alignment.Start)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {



        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium,

            ) {
            Text(
                text =title,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                //fontSize = 20.sp,
                modifier = modifier
            )
        }


        Row(modifier = modifier) {

            Text(text = author, color = Color.Gray, modifier = modifier)
            Text(text = " at ", color = Color.Gray, modifier = modifier)
            Text(text = formatDate(date), color = Color.Gray,modifier = modifier)
        }


    }
}