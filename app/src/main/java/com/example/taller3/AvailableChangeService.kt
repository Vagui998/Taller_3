package com.example.taller3

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class AvailableChangeService(private val context: Context) {

    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private lateinit var auth: FirebaseAuth

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            // Not implemented
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val user = snapshot.getValue(User::class.java)
            if (user != null && user.available && user.uid != auth.currentUser?.uid) {
                val message = "El usuario ${user.name} ${user.lastName} ahora se encuentra disponible"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            // Not implemented
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            // Not implemented
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
        }
    }

    fun startListening() {
        auth = Firebase.auth
        usersRef.addChildEventListener(listener)
    }

    fun stopListening() {
        usersRef.removeEventListener(listener)
    }
}



