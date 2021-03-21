package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import  com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)


        reset_password.setOnClickListener {
            sendResetPasswordLink()
        }


        tv_register_pw.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    fun sendResetPasswordLink(){
        val emailAddress = email_reset.text.toString()

        if (emailAddress.isNullOrEmpty()){
            Toast.makeText(this, "Please enter email to continue!",Toast.LENGTH_SHORT).show()
            return
        }

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reset password link send successfully!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Log.d("UpdatePasswordActivity","Failed to send reset email: ${it.message}")
                Toast.makeText(this,"Failed to send reset password email link: ${it.message}",Toast.LENGTH_SHORT).show()
            }
    }


}