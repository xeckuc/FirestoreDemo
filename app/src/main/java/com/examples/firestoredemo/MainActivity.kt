package com.examples.firestoredemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindUI()
    }

    private fun bindUI() {

        btnSend.setOnClickListener {

            if (editText.text.toString().isNotEmpty())
            {
                sendTextToFirestore(editText.text.toString())
            }
            else Toast.makeText(this, "No Text", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendTextToFirestore(text: String) {

        val dtf = SimpleDateFormat("dd.MM hh.mm.ss")
        val dateTime = dtf.format(Date())

        val message = hashMapOf(
            "date" to dateTime,
            "text" to text
        )

        db.collection("Messages")
            .add(message)
            .addOnSuccessListener { documentReference ->

                fetchMessages()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed To add message", Toast.LENGTH_SHORT).show()
            }

    }

    private fun fetchMessages() {

        svLayout.removeAllViews()

        db.collection("Messages")
            .get()
            .addOnSuccessListener { result ->

                for(document in result) {

                    val message = document.data.toString()

                    val tv = TextView(this)
                    tv.text = message

                    svLayout.addView(tv)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch messages", Toast.LENGTH_SHORT).show()
            }


    }
}
