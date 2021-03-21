package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            login()
        }

        tv_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPsw_btn.setOnClickListener {
            val intent = Intent(this,UpdatePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(){
        val email = login_email.text.toString()
        val password = login_psw.text.toString()

        if(email.isNullOrEmpty()||password.isNullOrEmpty()){
            Toast.makeText(this,"Please fill all the fields required!",Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("LoginActivity","User logged in successfully!")
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("LoginActivity","Failed to log in user: ${it.message}")
                Toast.makeText(this,"Failed to log in user: ${it.message}",Toast.LENGTH_SHORT).show()

            }

    }
}