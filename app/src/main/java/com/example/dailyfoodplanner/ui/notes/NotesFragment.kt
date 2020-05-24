package com.example.dailyfoodplanner.ui.notes


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_notes.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class NotesFragment : DaggerFragment(), View.OnClickListener, NotesAdapter.OnItemClickedListener, NotesAdapter.OnCheckedChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var notesViewModel: NotesViewModel

    private var compositeDisposable = CompositeDisposable()

    private lateinit var inputManager: InputMethodManager
    private var notesAdapter: NotesAdapter? = null

    private var isEditTextVisible: Boolean = false
    private var defaultColor: Int = 0
    private var listCheckedNotes = arrayListOf<CheckedNotes>()
    private var shouldEdit: Boolean = false
    private var clickedNote: Notes? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesViewModel = ViewModelProvider(this, viewModelFactory).get(NotesViewModel::class.java)

        setInitials()
        loadAllNotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notesViewModel.clear()
        compositeDisposable.clear()
    }

    private fun setInitials(){
        btnAddNotes.setOnClickListener(this)
        btnDeleteNotes.setOnClickListener(this)
        defaultColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        revealView.visibility = View.INVISIBLE
        isEditTextVisible = false
    }

    private fun loadAllNotes(){
        notesViewModel.getAllNotes()

        notesViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {listAllNotes->
            setUpAdapter(listAllNotes)
            notesAdapter?.notifyDataSetChanged()
        })

        notesViewModel.notesLoading.observe(viewLifecycleOwner, Observer {
            showProgressBarNotes(it)
        })
    }


    private fun setUpAdapter(notesList: List<Notes>){
        notesAdapter = NotesAdapter(requireContext(), notesList)

        notesAdapter?.setOnItemCheckedListener(this)
        notesAdapter?.setOnItemClickListener(this)

        recyclerViewNotes.layoutManager = LinearLayoutManager(context)
        recyclerViewNotes.adapter = notesAdapter

    }

    private fun showProgressBarNotes(show: Boolean){
        if(show){
            progressBarNotes.visibility = View.VISIBLE
            recyclerViewNotes.visibility = View.INVISIBLE
        } else{
            progressBarNotes.visibility = View.INVISIBLE
            recyclerViewNotes.visibility = View.VISIBLE
        }
    }

    private fun writeNote(note: String){
        if(note.isNotBlank()){
            compositeDisposable.add(notesViewModel.addNote(Notes(null, note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it){
                        Toast.makeText(context, "Note successfully added", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }

    private fun deleteNotes(listOfCheckedNotes: List<CheckedNotes>){
        compositeDisposable.add(
            notesViewModel.deleteNote(listOfCheckedNotes).subscribe {
                if(it){
                    btnDeleteNotes.visibility = View.INVISIBLE
                    btnAddNotes.visibility = View.VISIBLE
                    Toast.makeText(context, "Notes successfully deleted", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(context, "Something went wrong when deleting", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnAddNotes -> {
                showEditText("")
                if(shouldEdit){
                    //edit note
                    notesViewModel.editNote(Notes(clickedNote?.notesId, etTodoNotes.text.toString()))
                    etTodoNotes.setText("")
                    shouldEdit = false
                } else{
                    writeNote(etTodoNotes.text.toString())
                    etTodoNotes.setText("")
                }
            }
            R.id.btnDeleteNotes -> deleteNotes(listCheckedNotes)
        }
    }

    private fun revealEditText(view: LinearLayout){
        val cx = view.right -30
        val cy = view.bottom -60
        val finalRadius = Math.max(view.width, view.height)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
        view.visibility = View.VISIBLE
        isEditTextVisible = true
        anim.start()
    }

    private fun hideEditText(view: LinearLayout){
        val cx = view.right -30
        val cy = view.bottom -60
        val initialRadius = view.width
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)
        anim.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.visibility = View.INVISIBLE
            }
        })
        isEditTextVisible = false
        anim.start()
    }

    private fun showEditText(note: String){
        if(!isEditTextVisible) {
            notesAdapter?.setClickable(false)
            notesAdapter?.notifyDataSetChanged()
            revealEditText(revealView)
            etTodoNotes.requestFocus()
            etTodoNotes.setText(note)
            inputManager.showSoftInput(etTodoNotes, InputMethodManager.SHOW_IMPLICIT)
            btnAddNotes.setImageResource(R.drawable.icn_morph)
            val animatable = btnAddNotes.drawable as Animatable
            animatable.start()
        } else {
            notesAdapter?.setClickable(true)
            notesAdapter?.notifyDataSetChanged()
            inputManager.hideSoftInputFromWindow(etTodoNotes.windowToken, 0)
            hideEditText(revealView)
            btnAddNotes.setImageResource(R.drawable.icn_morph_reverse)
            val animatable = btnAddNotes.drawable as Animatable
            animatable.start()
        }
    }

    override fun onCheckedChange(listOfCheckedNotes: List<CheckedNotes>) {
        listCheckedNotes.clear()

       listOfCheckedNotes.forEach {
           if(it.isChecked){
               listCheckedNotes.add(it)
           }
       }

        if(listCheckedNotes.isNotEmpty()){
            btnDeleteNotes.visibility = View.VISIBLE
            btnAddNotes.visibility = View.INVISIBLE
        } else{
            btnAddNotes.visibility = View.VISIBLE
            btnDeleteNotes.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(note: Notes) {
        shouldEdit = true
        clickedNote = note
        showEditText(note.note)

    }
}
