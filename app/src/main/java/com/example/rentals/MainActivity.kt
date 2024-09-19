package com.example.rentals

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentals.ui.theme.RentalsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Login()
        }
    }
}

@Composable
fun Login() {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Card(modifier = Modifier
                .wrapContentSize()
                .padding(30.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize(), 
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    var email  by remember { mutableStateOf("") }
                    var password  by remember { mutableStateOf("") }
                    var role  by remember { mutableStateOf("") }
                    var expanded by remember { mutableStateOf(false) }
                    val list = listOf("landlord", "caretaker")
                    val icon = if (expanded){
                        Icons.Filled.KeyboardArrowUp
                    } else{
                        Icons.Filled.KeyboardArrowDown
                    }
                    val context = LocalContext.current
                    val auth = FirebaseAuth.getInstance()
                    val db = FirebaseFirestore.getInstance()
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Loin")
                    Spacer(modifier = Modifier.size(20.dp))
                    OutlinedTextField(value = email, onValueChange = {email=it},
                        label = { Text(text = "Email")},
                        leadingIcon = { Icon(painterResource(id = R.drawable.baseline_email_24), contentDescription =null)})
                    Spacer(modifier = Modifier.size(20.dp))
                    Box (modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center){
                        OutlinedTextField(value = role, onValueChange = {role=it},
                            label = { Text(text = "Role")},
                            leadingIcon = { Icon(painterResource(id = R.drawable.baseline_person_24), contentDescription =null)},
                            trailingIcon = { Icon(icon, contentDescription = null,
                                modifier = Modifier.clickable { expanded=!expanded })},
                            readOnly = true)
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded= false }) {
                            list.forEach { label->
                                DropdownMenuItem(text = { Text(text = label) },
                                    onClick = {
                                        role=label
                                        expanded=false
                                    })
                            }

                        }

                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    OutlinedTextField(value = password, onValueChange = {password=it},
                        label = { Text(text = "password")},
                        leadingIcon = {Icon(painterResource(id = R.drawable.baseline_lock_24), contentDescription = null)},
                        visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(onClick = {
                        if(email.isNotEmpty() && role.isNotEmpty() && password.isNotEmpty()){
                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    val current = auth.currentUser
                                    if(current != null){
                                        val userId = current.uid

                                        db.collection("Users").document(userId).get()
                                            .addOnSuccessListener { document->
                                                if(document.exists()){
                                                    val userRole = document.getString("Role")
                                                    if(userRole == role){
                                                        //val intent = Intent(context, MainActivity::class.java)
                                                      //  context.startActivity(intent)
                                                        Toast.makeText(context, "login successful", Toast.LENGTH_SHORT).show()
                                                    } else{
                                                        Toast.makeText(context, "Role mismatch", Toast.LENGTH_SHORT).show()
                                                    }
                                                }

                                            }
                                    }
                                }


                            }

                        }
                    }, modifier = Modifier.width(200.dp)) {
                        Text(text = "Login", fontWeight = FontWeight.Bold)
                        
                    }

                }

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
   Login()
}