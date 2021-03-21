package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.todo_row.view.*

class ToDoAdapter(options: FirestoreRecyclerOptions<ToDoModel>):
        FirestoreRecyclerAdapter<ToDoModel, ToDoAdapter.ViewHolder>(options) {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var taskname = itemView.todo_task_name!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.todo_row,parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ToDoModel) {
        holder.taskname.text = model.taskName
    }


}

