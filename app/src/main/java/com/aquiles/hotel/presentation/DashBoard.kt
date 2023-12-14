package com.aquiles.hotel.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquiles.hotel.DashBoardViewModel
import com.aquiles.hotel.MainViewModel
import com.aquiles.hotel.data.Repository
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoard(modifier: Modifier, mainViewModel : MainViewModel, state :DashBoardViewModel,onNavigate : () ->Unit ){



    val available  =  mainViewModel.rooms.filter { it.is_available }.toMutableStateList()
    val progress =   if(mainViewModel.rooms.isNotEmpty())  (available.size.toFloat() *100/mainViewModel.rooms.size.toFloat()) else 0f
    Column(
        modifier
            .background(Color(63, 0, 255))
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {

            Row(

            ) {
                Spacer(Modifier.width(20.dp))
                Text(text="Overview", style = MaterialTheme.typography.h5,color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier= Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){

                Spacer(Modifier.width(20.dp))
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                    CircularProgressIndicator(
                        color = Color.White,
                        progress = progress,
                        modifier = Modifier.size(80.dp),
                        strokeWidth = 15.dp
                    )
                    Text(text = " ${progress.toInt()}%",  color = Color.White, modifier = Modifier.padding(10.dp))
                }

                Spacer(Modifier.weight(1f))

                Column {

                    Text(text = "${available.size} rooms available",  color = Color.White,)
                }
            }
        }


        val last = remember {
            runBlocking{
                Repository.getLastReservation()
            }
        }

        BottomBackground(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f),
            backgroundColor = Color.White
        ) {
            Column {

                Spacer(modifier = Modifier.height(50.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text(text = "Last reserved ")

                    Spacer(modifier = Modifier.weight(1f))

                    if (last == null) {
                        Text(text = "-")
                    } else {
                        Text("${last.start_date}-${last.end_date}")
                    }
                }


                Text(text = "News", style = MaterialTheme.typography.h5)


                NewsPage(
                    state =state,
                    itemClicked ={
                    onNavigate()
                } ,
                 modifier = Modifier)
            }


        }


    }
}




@Composable
private  fun BottomBackground(modifier: Modifier, backgroundColor: Color,content: @Composable ()-> Unit){

    val roundedTopShape = RoundedCornerShape(
        topStart = 50.dp,
        topEnd = 50.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )


    Card(
        elevation = 4.dp,
        modifier = Modifier
            //   .background(backgroundColor)
            //.border(1.dp, Color.Black, roundedTopShape)
            .then(modifier)
        ,
        shape = roundedTopShape,
    ){

        Column(
            Modifier
                .background(backgroundColor)
                /// .border(1.dp, Color.Black, roundedTopShape)

        )
        {

       content()

        }

    }
}




@Composable
private  fun ItemView(modifier: Modifier, title : String,value : String, backgroundColor: Color, icon : @Composable ()-> Unit){
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(16.dp)
            //   .background(backgroundColor)
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .then(modifier)
            ,
        shape = RoundedCornerShape(10.dp),
    ){

        Column(
            Modifier
                .background(backgroundColor)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))) {
          Row() {
              Spacer(Modifier.width(10.dp))
              icon.invoke()
               Spacer(Modifier.weight(1f))
              Text(text = value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 25.sp)
              Spacer(Modifier.width(20.dp))

          }
            Spacer(modifier = Modifier.weight(0.8f))

            Row() {
                Spacer(Modifier.width(20.dp))
                 Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)

            }

            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}