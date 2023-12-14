package com.aquiles.hotel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.aquiles.hotel.models.Credential
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aquiles.hotel.models.Profile
import com.aquiles.hotel.ui.theme.Purple500
import okhttp3.HttpUrl
import java.io.IOException

@Preview(showBackground = true)
@Composable
fun Login(
    onSuccess : (Profile)-> Unit={},
    onSignUp : ()-> Unit ={},
    onForgot : ()-> Unit ={},

    ) {

    var isLoading  by remember {
        mutableStateOf(false)
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)

            ) {
                val email = remember { mutableStateOf(TextFieldValue()) }
                val password = remember { mutableStateOf(TextFieldValue()) }
                var message by remember {
                    mutableStateOf("")
                }

                Image(
                    painter = painterResource(id = R.drawable.login_image),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Text("HOTEL MANAGEMENT SYSTEM", fontWeight = FontWeight.Bold)
                Text(text = "Login")

                OutlinedTextField(
                    value = email.value,
                    onValueChange = { newValue ->
                        email.value = newValue
                        if (message.isNotEmpty()) message = ""
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { newValue ->
                        if (message.isNotEmpty()) message = ""
                        password.value = newValue
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = message)
                Spacer(modifier = Modifier.height(5.dp))



                Button(
                    onClick = {

                        isLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            val client = OkHttpClient()

                            val creds = Credential(email.value.text, password.value.text)

                            val url = HttpUrl.Builder()
                                .scheme("http")
                                .host(getServerIp(HotApplication.getAppContext())!!)
                                .port(8080)
                                .addPathSegment("login")
                                .addQueryParameter("email", creds.email)
                                .addQueryParameter("password", creds.password)
                                .build()

                            val request = Request.Builder()
                                .url(url)
                                .build()

                            try {
                                val response = client.newCall(request).execute()
                                if (response.isSuccessful) {
                                    val json = response.body?.string()
                                    val profile = Gson().fromJson(json, Profile::class.java)
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        onSuccess(profile)
                                    }
                                } else {
                                    message = "Wrong password or email ${response.message}"
                                    isLoading = false
                                }
                            } catch (e: IOException) {
                                message = "An error occurred: ${e.message}"
                                isLoading = false
                            }


                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text("Sign in")
                }

                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Forgot password ?", modifier = Modifier.clickable(onClick = onForgot))
            }

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth())
            {
                Text(
                    text = "Create an account",
                    color = Purple500,
                    modifier = Modifier.clickable(onClick = onSignUp)
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))
        }

        if (isLoading){
            LinearProgressIndicator()
        }
     
    }
}




























