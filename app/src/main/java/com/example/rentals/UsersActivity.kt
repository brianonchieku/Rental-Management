package com.example.rentals

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rentals.ui.theme.RentalsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Users()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Users() {
    val users = remember { mutableStateOf<List<User>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            users.value = fetchUsers()
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Users")},
            navigationIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription =null )
            })
    }) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), contentAlignment = Alignment.Center){
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    Button(onClick = { }, shape = RoundedCornerShape(15.dp)) {
                        Text(text = "Add User")

                    }

                }
            }
        }

    }
}

@Composable
fun AddUserDialog(onDismiss: () -> Unit, addUser: (User)-> Unit){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("")}
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val list = listOf("landlord", "caretaker")
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val icon = if (expanded){
        Icons.Default.KeyboardArrowDown
    } else{
        Icons.Default.KeyboardArrowUp
    }
    
    Dialog(onDismissRequest = {onDismiss()}) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "Add new user", fontWeight = FontWeight.Bold)
                TextField(value = name, onValueChange = { name = it }, label = { Text(text = "Name")} )
                TextField(value = email, onValueChange = {email=it}, label = { Text(text = "Email")})
                TextField(value = phone, onValueChange = {phone=it}, label = { Text(text = "Phone Number")})
                Box (modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center){
                    TextField(value = role, onValueChange = {role=it},
                        label = { Text(text = "Role")},
                        trailingIcon = {
                            Icon(icon, contentDescription = "", modifier = Modifier.clickable { expanded=!expanded })
                        })
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded=false }) {
                        list.forEach { doc->
                            DropdownMenuItem(text = { Text(text = doc) }, onClick = {
                                role=doc
                                expanded=false
                            })
                        }

                    }
                }

                TextField(value = password, onValueChange = {password=it}, label = { Text(text = "Password")})
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "cancel")
                    }
                    TextButton(onClick = {
                        if (name.isEmpty()||email.isEmpty()||phone.isEmpty()||role.isEmpty()||password.isEmpty()){
                            Toast.makeText(context, "fill all the fields", Toast.LENGTH_SHORT).show()
                        } else{
                            val newUser = User(name,email,phone,role,password)
                            addUser(newUser)
                        }
                    }) {
                        Text(text = "Add", fontWeight = FontWeight.Bold)
                    }
                    
                }

            }
            
        }
        
    }
}

fun addUserToFirebase(user: User, context: Context){
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task->
        if (task.isSuccessful){
            val current = auth.currentUser
            if (current!=null){
                val userId = current.uid
                val details= hashMapOf(
                    "Name" to user.name,
                    "Email" to user.email,
                    "Phone Number" to user.number,
                    "Role" to user.role
                )
                db.collection("Users").document(userId).set(details).addOnSuccessListener {
                    Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
        }
    }
}

suspend fun fetchUsers(): List<User>{
    val db = FirebaseFirestore.getInstance()
    val users = mutableListOf<User>()

    try {
        val snapshot = db.collection("Users").get().await()
        users.addAll(snapshot.documents.map { doc->
            User(
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?:"",
                number = doc.getString("phone number") ?:"",
                role = doc.getString("role") ?:""
            )

        })
    } catch (_: FirebaseFirestoreException){

    }
    return users
}

data class User(
    val name: String = "",
    val email: String = "",
    val number: String = "",
    val role: String = "",
    val password: String = ""
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
   Users()
}