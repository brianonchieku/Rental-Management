package com.example.rentals.model

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db= FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun fetchUsers(): List<User>{

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

    fun addUser(user: User, context: Context, onSuccess: () -> Unit, onFailure: () -> Unit){

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
                        onSuccess()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show()
                        onFailure()
                    }
                }
            }else{
                Toast.makeText(context, "Filed. Try again", Toast.LENGTH_SHORT).show()
                onFailure()
            }
        }
    }

   /* fun userLogin(user: User, role: String){
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        auth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val current = auth.currentUser
                if (current!=null){
                    val userId = current.uid
                    db.collection("Users").document(userId).get().addOnSuccessListener { doc ->
                        if (doc.exists()){
                            val userRole = doc.getString("Role")
                            if (userRole==role){

                            }

                        }
                    }
                }

            }
        }
    }*/
}