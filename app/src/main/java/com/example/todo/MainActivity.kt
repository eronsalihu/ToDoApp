package com.example.todo

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_add_to_do_list.*
import kotlinx.android.synthetic.main.fragment_to_do.*
import kotlinx.android.synthetic.main.todo_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("todos")
    var toDoAdapter: ToDoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VerifyIfUserIsLoggedIn()

        val bar:Toolbar = toolbar
        bar.inflateMenu(R.menu.nav_menu)
        bar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            run {
                when (item.itemId) {
                    R.id.signOut -> {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            true

        })

        val addToDoListFragment =  AddToDoListFragment()

        fab_btn.setOnClickListener {

            fragmentController(addToDoListFragment)
        }

        fetchUsername()
        greeting()

        setUpRecyclerview()
    }

    private fun fetchUsername() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val userName: String = i.child("username").value.toString()
                    username.text = userName
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                        this@MainActivity,
                        "FirebaseError ${error.message}",
                        Toast.LENGTH_SHORT
                ).show()

            }

        })
    }

    private fun greeting(){

        val calendar = Calendar.getInstance()
        val hourOfDay = calendar[Calendar.HOUR_OF_DAY]

        if(hourOfDay>11||hourOfDay<4){
            greet.text = "Hello"
        }
        else{
            greet.text = "Good Morning"
        }
    }

    private fun fragmentController(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_out, R.anim.slide_in)
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
        fab_btn.hide()

    }

    private fun VerifyIfUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if(uid==null){
            val  intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private fun setUpRecyclerview(){
        val query: Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ToDoModel> = FirestoreRecyclerOptions.Builder<ToDoModel>()
                .setQuery(query,ToDoModel::class.java).build()

        toDoAdapter = ToDoAdapter(firestoreRecyclerOptions)

        recyclerView.adapter = toDoAdapter
        recyclerView.layoutManager = GridLayoutManager(this,2)
    }

    override fun onStart() {
        super.onStart()
        toDoAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        toDoAdapter?.stopListening()
    }
}
