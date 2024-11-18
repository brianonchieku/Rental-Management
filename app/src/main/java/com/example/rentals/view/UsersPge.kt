package com.example.rentals.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rentals.NetworkResponse
import com.example.rentals.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersPage(viewModel: UserViewModel){

    val users by viewModel.users.observeAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        scope.launch {
            viewModel.fetchUsers()
        }

    }
    Scaffold (topBar = {
        TopAppBar(title = { Text(text = "Users") }, navigationIcon = {
            Icon(imageVector = Icons.Default.Person, contentDescription =null )
        })
    }){ paddingValues ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ){
                    Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(15.dp)) {
                        Text(text = "Add user")

                    }
                }
                when(users){
                    is NetworkResponse.Error -> TODO()
                    NetworkResponse.Loading -> TODO()
                    is NetworkResponse.Success -> TODO()
                    null -> TODO()
                }
            }
        }
    }
}