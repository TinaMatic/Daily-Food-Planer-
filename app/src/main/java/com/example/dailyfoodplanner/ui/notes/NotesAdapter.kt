package com.example.dailyfoodplanner.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.Notes
import kotlinx.android.synthetic.main.row_note.view.*

class NotesAdapter (val context: Context, val notesList: List<Notes>): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(notesList[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Notes){
            itemView.tvNote.text = note.note
        }
    }
}