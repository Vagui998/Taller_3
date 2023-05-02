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
    private var previousSnapshot: DataSnapshot? = null

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            auth = Firebase.auth
            for (userSnapshot in snapshot.children) {
                val user = userSnapshot.getValue(MyUser::class.java)
                if (user != null && user.available && userSnapshot.key != auth.currentUser?.uid) {
                    val previousUserSnapshot = userSnapshot.key?.let { previousSnapshot?.child(it) }
                    val previousUser = previousUserSnapshot?.getValue(MyUser::class.java)
                    if (previousUser == null || !previousUser.available) {
                        val message = "El usuario ${user.name} ${user.lastName} ahora se encuentra disponible"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        break // stop looping after showing the first toast
                    }
                }
            }
            previousSnapshot = snapshot
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
        }
    }

    fun startListening() {
        usersRef.addValueEventListener(listener)
    }

    fun stopListening() {
        usersRef.removeEventListener(listener)
    }
}

