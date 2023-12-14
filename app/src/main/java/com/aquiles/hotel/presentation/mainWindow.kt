package com.aquiles.hotel.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aquiles.hotel.*
import com.aquiles.hotel.data.Repository
import com.aquiles.hotel.data.makeDeleteRequest
import com.aquiles.hotel.data.makeRequest
import com.aquiles.hotel.models.*
import com.aquiles.hotel.presentation.edition.EditReservationForm
import com.aquiles.hotel.presentation.edition.EditRoomForm
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.myriad.quicktransfer.presentation.ScanUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun mainWindow(state : MainViewModel){

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    var currentRoom by remember {
        mutableStateOf(Room.new())
    }

    var reservation  by remember {
        mutableStateOf(Reservation.new())
    }

    var title  by remember {
        mutableStateOf(if (getServerIp(context ) !=null) "Login" else "Setup")
    }

    val  dashBoardViewModel by remember {
        mutableStateOf(DashBoardViewModel())
    }

    var logged  by remember {
        mutableStateOf(false)
    }
    Scaffold(
        backgroundColor = Color.White,
        scaffoldState =  scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = Color(63, 0, 255),
                navigationIcon = {
                    if(logged) {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.drawerState.open() }
                            }
                        ) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "",
                                tint = Color.White
                            )

                        }
                    }
                },
                title = {

                    Text(text=title, style = MaterialTheme.typography.h5,color = Color.White)
                },
                actions = {



                }

            )
        },
       drawerGesturesEnabled = logged,
        drawerElevation = 4.dp,
        drawerShape = RoundedCornerShape(15.dp),
        drawerContent = {

           // Spacer(modifier = Modifier.height(50.dp))

            val ip = getServerIp(HotApplication.getAppContext())
            ip?.let {
                if(state.profile.value.profil_picture.isNotEmpty()) {

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        coil.compose.AsyncImage(
                            model = "http://$ip:8080/image/${state.profile.value.profil_picture}",
                            contentDescription = null,
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(70.dp)
                        )
                    }
                }
            }


            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                Text(text = "${state.profile.value.first_name} ${state.profile.value.last_name}")
            }

            Spacer(Modifier.height(30.dp))

            listOf(
                Pair("Dashboard", Icons.Filled.Dashboard),
                Pair("Rooms", Icons.Filled.Room),
                Pair("Customers", Icons.Filled.Person),
                Pair("Reservations", Icons.Filled.CalendarToday),
            )
                .forEachIndexed {index,pair->
                Column(modifier = Modifier
                    .padding(vertical = 1.dp)
                    .clickable {

                        val dest: String = when (index) {
                            0 -> {
                                title = "Dashboard"
                                AppScreen.Dash.route
                            }
                            1 -> {
                                title = "Room"
                                AppScreen.Rooms.route
                            }
                            2 -> {
                                title = "Customers"
                                AppScreen.Customers.route
                            }
                            3 -> {
                                title = "Booking"
                                AppScreen.Booking.route
                            }
                            else -> ""
                        }

                        scope.launch { scaffoldState.drawerState.close() }

                        if (dest.isNotEmpty()) navController.navigate(dest)

                    }) {

                      DrawerSection(pair)
                    Box(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.Gray))
                }
            }

            Spacer(Modifier.weight(1f))

            
            
            Column() {
                
            }






        }


    ) { it ->

        val startDestination =
            if (getServerIp(context) != null) AppScreen.Login.route else AppScreen.Setup.route

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(it)
        ) {
            composable(route = AppScreen.Login.route) {
                Login(
                    onSuccess = { profile ->

                        logged = true
                        state.profile.value = profile
                        val ip = getServerIp(HotApplication.getAppContext())
                        ip?.let {
                            scope.launch {
                                                                  // Create a list of requests.
                                listOf(
                                    "http://$ip:8080/rooms",
                                    "http://$ip:8080/profiles",
                                    "http://$ip:8080/clients",
                                    "http://$ip:8080/reservations"
                                ).forEachIndexed { index, url ->
                                    makeRequest(
                                        url = url,
                                        onFailure = {
                                            println("failure index $index")
                                        },
                                        onSuccess = { res ->
                                            insertData(index, res,state)
                                        }
                                    )
                                }
                            }
                        }
                        title = "Dashboard"
                        navController.navigate(AppScreen.Dash.route)
                    },
                    onSignUp = {
                        title = "Signup"
                        navController.navigate(AppScreen.Signup.route) },
                    onForgot = {}
                )
            }

            composable(route = AppScreen.Dash.route) {
                DashBoard(
                    modifier = Modifier.fillMaxSize(),
                    mainViewModel = state,
                    onNavigate = {

                        navController.navigate(AppScreen.NewsView.route)

                    },
                    state = dashBoardViewModel
                )
            }

            composable(route= AppScreen.NewsView.route){
                NewsView(dashBoardViewModel)
            }
            composable(route = AppScreen.Setup.route) {
                FirstTime(modifier = Modifier.fillMaxSize(), navController)
            }

            composable(route = AppScreen.ScanUi.route) {
                ScanUi(onNavigate = { code ->
                    scope.launch(Dispatchers.IO) { saveInCache(context, code) }
                    title = "Login"
                    navController.navigate(AppScreen.Login.route)
                })
            }

            composable(route = AppScreen.Signup.route) {
                EditProfilForm(
                    profile = state.profile.value,
                    onSave = {
                        scope.launch(Dispatchers.IO) {
                            val path = it.profil_picture
                            val file = File(path)
                            val filename = "${it.email.split(".").first()}.${file.extension}"
                            postFile(file, filename)
                            val c = it.copy(profil_picture = filename)
                            signUp(c)
                            saveToSharedPreference(context, "profilePicture", filename)
                        }
                        state.profile.value = it
                        title = "Login"

                        navController.navigate(AppScreen.Login.route)
                    }
                ) {
                    title = "Login"

                    navController.navigate(AppScreen.Login.route)
                }
            }

            composable(route = AppScreen.Home.route) {
                Home(modifier = Modifier, onNavigate = { navController.navigate(it) })
            }

            composable(route = AppScreen.ViewProfil.route) {
                ProfilView(profile = state.profile.value)
            }


            composable(route = AppScreen.Rooms.route) {

                RoomList(
                    rooms = state.rooms,
                    onCreateRoom = {
                     currentRoom = Room.new()
                      navController.navigate(AppScreen.EditRoom.route)
                    },
                    onDeleteRoom = { room ->
                        state.rooms.remove(room)
                        scope.launch(Dispatchers.IO) {
                            Repository.removeRoom(room)

                            makeDeleteRequest(
                                url = "http://${getServerIp(HotApplication.getAppContext())}:8080/deleteroom/${room.id!!}"
                            )
                        }
                        context.showToast("room deleted successfully ")
                    },
                    onEditRoom = {room ->
                                currentRoom = room
                        title = "Edit Room"
                        navController.navigate(AppScreen.EditRoom.route)
                    },
                    onBook = { room ->

                        reservation = reservation.copy(room_id = "${room.id!!}")
                        title = "Book"
                        navController.navigate(AppScreen.Book.route)
                    }
                )
            }


            composable(route = AppScreen.Book.route) {
                EditReservationForm(
                    reservation = reservation,
                    onSave = {
                        state.updateReservations()
                        title = "Booking"

                        navController.navigate(AppScreen.Booking.route)
                        scope.launch(Dispatchers.IO){
                            val ip = getServerIp(HotApplication.getAppContext())
                            ip?.let {
                                makeRequest(
                                    url = "http://$ip:8080/clients",
                                    onFailure = {
                                    },
                                    onSuccess = { res ->
                                        insertData(2, res, state)
                                    }
                                )
                            }

                        }
                         },
                    onCancel = {

                        navController.navigateUp()
                    }
                )
            }

            composable(route = AppScreen.EditRoom.route) {

                EditRoomForm(
                    room = currentRoom,
                    onSave = {
                        state.updateRooms()
                        navController.navigateUp()

                    },
                    onCancel = {
                        navController.navigateUp()
                    }
                )
            }


            composable(route = AppScreen.Booking.route) {

                QListView(
                    buttontext = "",
                    items = state.reservations,
                    onCreateItem = { /*TODO*/ })
                {
                    BookingItem(
                        reservation = it,
                        onDeleteClick = {resTodelete->
                            state.reservations.remove(resTodelete)
                           scope.launch(Dispatchers.IO) {

                               makeDeleteRequest(
                                   url = "http://${getServerIp(HotApplication.getAppContext())}:8080/removereservation/${resTodelete.id!!}"
                               )
                               Repository.removeReservation(resTodelete)
                               context.showToast("Deleted successfully")
                           }
                        }
                    )
                }
            }
            composable(route = AppScreen.Customers.route) {
                QListView(
                    buttontext = "",
                    items = state.clients,
                    onCreateItem = {},

                    ) { client ->
                    CustomerItem(client = client,
                        onDeleteClick = {clientTodelete->
                            state.clients.remove(clientTodelete)
                            scope.launch(Dispatchers.IO) {

                                makeDeleteRequest(
                                    url = "http://${getServerIp(HotApplication.getAppContext())}:8080/removeclient/${clientTodelete.id!!}"
                                )
                                Repository.removeClient(clientTodelete)
                                context.showToast("Deleted successfully")
                            }

                        }
                    )
                }

            }


        }
    }
}



@Composable
fun QPushButton(
    onClick : () ->Unit,
    text : String,
    modifier: Modifier =Modifier
)
{
    Button(onClick = {
        onClick()
    }) {
        Column(

            modifier = Modifier

                .padding(5.dp)
                .then(modifier),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text,
                style = TextStyle(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

        }
    }
}




sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Dash : AppScreen("dash")
    object Setup : AppScreen("setup")
    object ScanUi : AppScreen("scanui")
    object Signup : AppScreen("signup")
    object Home : AppScreen("Home")
    object ViewProfil : AppScreen("viewprofil")
    object Rooms : AppScreen("rooms")
    object Customers : AppScreen("customers")
    object Booking  : AppScreen("booking")
    object EditRoom : AppScreen("editroom")
    object Book  : AppScreen("book")
    object NewsView : AppScreen("newsview")
}


private fun insertData(index: Int, res: String?,state: MainViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        res?.let {
            when (index) {
                0 -> {

                    val gson =
                        GsonBuilder().registerTypeAdapter(
                            List::class.java,
                            RoomDeserializer()
                        ).create()
                    val rooms =
                        gson.fromJson<List<Room>>(
                            res,
                            object :
                                TypeToken<List<Room>>() {}.type
                        )


                    rooms.forEach {
                        Repository.insertRoom(it)
                    }

                    state.updateRooms()


                }
                1 -> {
                    val gson =
                        GsonBuilder().registerTypeAdapter(
                            List::class.java,
                            ProfileDeserializer()
                        ).create()
                    val profiles =
                        gson.fromJson<List<Profile>>(
                            res,
                            object :
                                TypeToken<List<Profile>>() {}.type
                        )


                    profiles.forEach {
                        Repository.insertProfile(it)
                    }

                }
                2 -> {
                    val gson =
                        GsonBuilder().registerTypeAdapter(
                            List::class.java,
                            ClientDeserializer()
                        ).create()
                    val clients =
                        gson.fromJson<List<Client>>(
                            res,
                            object :
                                TypeToken<List<Client>>() {}.type
                        )

                    clients.forEach {
                        Repository.insertClient(it)
                    }

                    state.updateClients()
                }
                3 -> {
                    val gson =
                        GsonBuilder().registerTypeAdapter(
                            List::class.java,
                            ReservationDeserializer()
                        ).create()
                    val reservations =
                        gson.fromJson<List<Reservation>>(
                            res,
                            object :
                                TypeToken<List<Reservation>>() {}.type
                        )



                    reservations.forEach {
                        Repository.insertReservation(it)
                    }
                    state.updateReservations()
                }
            }

        }
    }
}

