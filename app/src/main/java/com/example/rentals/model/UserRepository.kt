package com.example.rentals.model

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class UserRepository {

    suspend fun fetchUsers(): List<User>{
        val db = FirebaseFirestore.getInstance()
        val users = mutableListOf<User>()

        try {
            val snapshot = db.collection("Users").get().await()
            users.addAll(snapshot.documents.map { doc ->
                User(
                    name = doc.getString("Name") ?:"",
                    email = doc.getString("Email") ?:"",
                    number = doc.getString("Phone Number") ?:"",
                    role = doc.getString("Role") ?:""
                )
            })
        } catch (_: FirebaseFirestoreException){

        }
        return users
    }

    fun addUser(user: User, context: Context){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val current = auth.currentUser
                if (current != null){
                    val userId = current.uid
                    val details = hashMapOf(
                        "Name" to user.name,
                        "Email" to user.email,
                        "Phone Number" to user.number,
                        "Role" to user.role
                    )

                    db.collection("Users").document(userId).set(details).addOnSuccessListener {
                        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}