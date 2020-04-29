package com.example.dailyfoodplanner.ui.notes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import kotlinx.android.synthetic.main.row_note.view.*
import javax.inject.Inject

class NotesAdapter (val context: Context, val notesList: List<Notes>)
    : RecyclerView.Adapter<NotesAdapter.ViewHolder>(){

    private var checkedChangeListener: OnCheckedChangeListener? = null

    private var onItemClickListener: OnItemClickedListener? = null

    @Inject
    lateinit var checkedNotes: CheckedNotes

    val listOfCheckedNotes = arrayListOf<CheckedNotes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        listOfCheckedNotes.add(CheckedNotes(notesList[position].notesId, false))
        val isChecked = listOfCheckedNotes.any {
            it.notesId == notesList[position].notesId
        }
        holder.bindView(notesList[position], isChecked)

//        holder.itemView.cbNote.setOnCheckedChangeListener(this)

    }

    fun setOnItemCheckedListener(checkedChangeListener: OnCheckedChangeListener){
        this.checkedChangeListener = checkedChangeListener
    }

    fun setOnItemClickListener(onItemClickedListener: OnItemClickedListener){
        this.onItemClickListener = onItemClickedListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Notes, isChecked: Boolean){
            itemView.tvNote.text = note.note

            itemView.cbNote.isChecked = isChecked


//            Log.d("Checked note id", listOfCheckedNotes[position].notesId)

//
//
//            itemView.cbNote.setOnCheckedChangeListener { buttonView, isChecked ->
//                listOfCheckedNotes[position].isChecked = isChecked
//            }
//
//            itemView.cbNote.isChecked = listOfCheckedNotes[position].isChecked

//            itemView.cbNote.isChecked = checkedNotesModel?.isChecked!!

            itemView.cbNote.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    listOfCheckedNotes.add(CheckedNotes(note.notesId, isChecked))
                } else {
                    listOfCheckedNotes.remove(CheckedNotes(note.notesId, true))
                }

                Log.d("List of checked notes", listOfCheckedNotes.toString())

                checkedChangeListener?.onCheckedChange(listOfCheckedNotes)
            }

            itemView.setOnClickListener{
                if(listOfCheckedNotes.isEmpty()){
                    onItemClickListener?.onItemClick(note)
                }

            }

//            val isChecked = listOfCheckedNotes.any {
//                it.notesId == note.notesId
//            }



        }
    }

    interface OnCheckedChangeListener{
        fun onCheckedChange(listOfCheckedNotes: List<CheckedNotes>)
    }

    interface OnItemClickedListener{
        fun onItemClick(note: Notes)
    }
//
//    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
////        checkedNotesModel?.isChecked = isChecked
//    }
}