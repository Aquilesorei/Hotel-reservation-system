package com.aquiles.hotel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.aquiles.hotel.models.Profile
import com.aquiles.hotel.presentation.ChooseFile
import java.io.File

@Composable
fun EditProfilForm(
    profile: Profile,
    onSave: (Profile) -> Unit,
    onLogin : ()-> Unit
) {
    var firstName by remember { mutableStateOf(profile.first_name) }
    var lastName by remember { mutableStateOf(profile.last_name) }
    var age by remember { mutableStateOf(profile.age.toString()) }
    var email by remember { mutableStateOf(profile.email) }

    var profilPicture by remember { mutableStateOf(profile.profil_picture) }
    var password by remember { mutableStateOf(profile.password) }
     var confirmPassword  by remember {
         mutableStateOf("")
     }

    var message by remember {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
                .weight(.9f)
                .verticalScroll(scrollState)
        ) {


            Image(
                painter = painterResource(id = R.drawable.register_page),
                contentDescription = null,
                modifier = Modifier.size(256.dp)
            )

            Text("Create An Account", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },

                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    message = if (confirmPassword != password && confirmPassword.isNotEmpty() ) "passwords mismatch" else ""

                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = PasswordVisualTransformation(),
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it

                    message = if (confirmPassword != password) "passwords mismatch" else ""
                },
                label = { Text("Confirm password") },
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = PasswordVisualTransformation(),
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(text = File(profilPicture).name, color = Color.Gray)

            ChooseFile(modifier = Modifier, onFileChoosen = {filePath->
                profilPicture = filePath
            })
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = message)
            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = {
                    onSave(
                        Profile(
                            profile.id,
                            firstName,
                            lastName,
                            age.toInt(),
                            email,
                            profilPicture,
                            password
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text("Save")
            }
            Spacer(modifier = Modifier.padding(20.dp))

            Text(
                text = "Login Instead",
                modifier = Modifier.clickable(onClick = onLogin)
            )
            Spacer(modifier = Modifier.padding(20.dp))

        }
    }
}
