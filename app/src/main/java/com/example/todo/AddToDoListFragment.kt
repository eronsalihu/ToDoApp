package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_to_do_list.*
import kotlinx.android.synthetic.main.todo_row.*
import java.util.*
import kotlin.collections.HashMap


class AddToDoListFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_do_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseFirestore = FirebaseFirestore.getInstance()


        close_btn.setOnClickListener {

            val manager = requireActivity().supportFragmentManager
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_out, R.anim.slide_in).remove(this).commit()
            val fab: FloatingActionButton = activity?.fab_btn as FloatingActionButton
            fab.show()
        }

        activity?.create_todo_list?.setOnClickListener {
            val taskName = task_name.text.toString()
            val description = descriptionTask.text.toString()
            val dateToDo = set_date.toString()

            if(taskName.isEmpty().or(description.isEmpty()).or(dateToDo.isEmpty())){
                Toast.makeText(activity, "Please fill the fields to create ToDo list!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val documentReference: DocumentReference = firebaseFirestore.collection("todos").document()
            val todo = HashMap<String, Any?>()

            todo["taskName"] = taskName
            todo["description"] = description

            documentReference.set(todo).addOnSuccessListener{
                Toast.makeText(activity,"ToDo '$taskName' created successfully! ",Toast.LENGTH_SHORT).show()
                val manager = requireActivity().supportFragmentManager
                manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_out, R.anim.slide_in).remove(this).commit()

                val fab: FloatingActionButton = activity?.fab_btn as FloatingActionButton
                fab.show()

            }.addOnFailureListener {
                Toast.makeText(activity,"ccc",Toast.LENGTH_SHORT).show()
            }
        }

    }

}
class Todo(var taskName:String, var description: String){
    constructor():this("",""){
        this.taskName = taskName
        this.description = description
    }
}

