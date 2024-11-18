package com.example.rentals.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.rentals.model.User
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
                when(val result = users){
                    is NetworkResponse.Error -> {
                        Text(text = result.message)
                    }
                    NetworkResponse.Loading -> {
                        CircularProgressIndicator()
                    }
                    is NetworkResponse.Success -> {
                        UsersList(users = result.data)
                    }
                    null -> {}
                }
            }
        }
    }
}

@Composable
fun UsersList(users: List<User>){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(users){
            UserDetails(user = it)

        }
    }
}

@Composable
fun UserDetails(user: User){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Name: ${user.name}")
                Text(text = "Email: ${user.email}")
                Text(text = "Phone number: ${user.number}")
                Text(text = "Role: ${user.role}")

            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription ="delete" )

            }

        }

    }

}