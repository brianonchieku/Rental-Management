package com.example.rentals.model

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
                    email = doc.getString("Emai") ?:"",
                    number = doc.getString("Phone Number") ?:"",
                    role = doc.getString("Role") ?:""
                )
            })
        } catch (_: FirebaseFirestoreException){

        }

        return users

    }
}