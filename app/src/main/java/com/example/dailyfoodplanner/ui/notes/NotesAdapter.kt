package com.example.dailyfoodplanner.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import kotlinx.android.synthetic.main.row_note.view.*

class NotesAdapter (val context: Context, val notesList: List<Notes>): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var checkedChangeListener: OnCheckedChangeListener? = null

    private var onItemClickListener: OnItemClickedListener? = null

    val listOfCheckedNotes = arrayListOf<CheckedNotes>()

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

    fun setOnItemCheckedListener(checkedChangeListener: OnCheckedChangeListener){
        this.checkedChangeListener = checkedChangeListener
    }

    fun setOnItemClickListener(onItemClickedListener: OnItemClickedListener){
        this.onItemClickListener = onItemClickedListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Notes){
            itemView.tvNote.text = note.note

            itemView.cbNote.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    listOfCheckedNotes.add(CheckedNotes(note.notesId, isChecked))
                } else {
                    val listOfDeleteNotes = arrayListOf<CheckedNotes>()
                    listOfCheckedNotes.forEach {
                        if(it.notesId.equals(note.notesId)){
                            listOfDeleteNotes.add(it)
                        }
                    }
                    listOfCheckedNotes.removeAll(listOfDeleteNotes)
                }

                checkedChangeListener?.onCheckedChange(listOfCheckedNotes)
            }

            itemView.setOnClickListener{
                if(listOfCheckedNotes.isEmpty()){
                    onItemClickListener?.onItemClick(note)
                }

            }

        }
    }

    interface OnCheckedChangeListener{
        fun onCheckedChange(listOfCheckedNotes: List<CheckedNotes>)
    }

    interface OnItemClickedListener{
        fun onItemClick(note: Notes)
    }
}