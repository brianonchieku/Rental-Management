package com.example.rentals

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentals.ui.theme.RentalsTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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

@Composable
fun Users() {

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
    val name: String,
    val email: String,
    val number: String,
    val role: String
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
   Users()
}