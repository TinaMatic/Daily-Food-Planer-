package com.example.dailyfoodplanner.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter (val context: Context, private val notesList: List<Notes>)
    : RecyclerView.Adapter<NotesAdapter.ViewHolder>(){

    private var checkedChangeListener: OnCheckedChangeListener? = null

    private var onItemClickListener: OnItemClickedListener? = null

    private var isClickable = true

    val listOfCheckedNotes = arrayListOf<CheckedNotes>()

    init {
        notesList.forEach {
            listOfCheckedNotes.add(CheckedNotes(it.notesId, false))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(notesList[position], position)
        holder.itemView.cbNote.isChecked = listOfCheckedNotes[position].isChecked
    }

    fun setOnItemCheckedListener(checkedChangeListener: OnCheckedChangeListener){
        this.checkedChangeListener = checkedChangeListener
    }

    fun setOnItemClickListener(onItemClickedListener: OnItemClickedListener){
        this.onItemClickListener = onItemClickedListener
    }

    fun setClickable(isClickable: Boolean){
        this.isClickable = isClickable
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Notes, position: Int){
            itemView.tvNote.text = note.note


            itemView.cbNote.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isClickable){
                    listOfCheckedNotes[position].isChecked = isChecked
                    checkedChangeListener?.onCheckedChange(listOfCheckedNotes)
                } else{
                    itemView.cbNote.isChecked = false
                }
            }

            itemView.setOnClickListener{
                val selected = listOfCheckedNotes.any {
                    it.isChecked.equals(true)
                }
                if(!selected){
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