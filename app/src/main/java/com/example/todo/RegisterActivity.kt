package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_btn.setOnClickListener {
            register()
        }

        tv_login.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(){
        val username = register_username.text.toString()
        val email = register_email.text.toString()
        val password = register_password.text.toString()
        val confirmPsw = register_confirmPsw.text.toString()

        if (username.isNullOrEmpty()||email.isNullOrEmpty()||password.isNullOrEmpty()||confirmPsw.isNullOrEmpty()){
            Toast.makeText(this,"Please fill all the field required!",Toast.LENGTH_SHORT).show()
            return
        }

        if(!password.equals(confirmPsw)){
            Toast.makeText(this,"Passwords don't match!",Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).
        addOnCompleteListener{
            if(!it.isSuccessful) return@addOnCompleteListener
            Log.d("RegisterActivity","Successfully created user with uid: ${it.result?.user?.uid}")
            saveUserDataToDatabase()
        }
            .addOnFailureListener{
                Log.d("RegisterActivity","Failed to create user: ${it.message}")
                Toast.makeText(this,"Failed to create user: ${it.message}",Toast.LENGTH_SHORT).show()

            }
        }

    private fun saveUserDataToDatabase() {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, register_username.text.toString())

        ref.setValue(user).addOnCompleteListener {
            Log.d("RegisterActivity","User data saved to FirebaseDatabase!")

            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Log.d("RegisterActivity","User data saved to FirebaseDatabase!")
            Toast.makeText(this,"Failed to save user: ${it.message}",Toast.LENGTH_SHORT).show()
        }
    }
}

class User(var uid: String, var username: String) {
    constructor():this("",""){
        this.uid = uid
        this.username = username
    }


}
