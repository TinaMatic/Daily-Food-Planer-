package com.example.dailyfoodplanner.ui.notes


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.dailyfoodplanner.R
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
class NotesFragment : DaggerFragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var notesViewModel: NotesViewModel

    private var compositeDisposable = CompositeDisposable()

    lateinit var inputManager: InputMethodManager
    lateinit var notesAdapter: NotesAdapter

    private var isEditTextVisible: Boolean = false
    private var defaultColor: Int = 0

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
        defaultColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        revealView.visibility = View.INVISIBLE
        isEditTextVisible = false
    }

    private fun loadAllNotes(){
        notesViewModel.loadAllNotes()

        notesViewModel.notesLiveData.observe(this, Observer {listAllNotes->
            setUpAdapter(listAllNotes)
            notesAdapter.notifyDataSetChanged()
        })
    }

    private fun setUpAdapter(notesList: List<Notes>){
        notesAdapter = NotesAdapter(requireContext(), notesList)
        recyclerViewNotes.layoutManager = LinearLayoutManager(context)
        recyclerViewNotes.adapter = notesAdapter

    }

    private fun writeNote(note: String){
        if(note.isNotBlank()){
            compositeDisposable.add(notesViewModel.writeNote(Notes(null, note))
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


//    private fun colorize(photo: Bitmap){
//        val palette = Palette.from(photo).generate()
//        applyPalette(palette)
//    }

    private fun applyPalette(palette: Palette){
        activity?.window?.setBackgroundDrawable(ColorDrawable(palette.getDarkMutedColor(defaultColor)))
        notesNameHolder.setBackgroundColor(palette.getMutedColor(defaultColor))
        revealView.setBackgroundColor(palette.getLightVibrantColor(defaultColor))
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnAddNotes -> if(!isEditTextVisible){
                revealEditText(revealView)
                etTodoNotes.requestFocus()
                inputManager.showSoftInput(etTodoNotes, InputMethodManager.SHOW_IMPLICIT)
                btnAddNotes.setImageResource(R.drawable.icn_morph)
                val animatable = btnAddNotes.drawable as Animatable
                animatable.start()
            } else {
                writeNote(etTodoNotes.text.toString())
                notesAdapter.notifyDataSetChanged()
                etTodoNotes.setText("")
                inputManager.hideSoftInputFromWindow(etTodoNotes.windowToken, 0)
                hideEditText(revealView)
                btnAddNotes.setImageResource(R.drawable.icn_morph_reverse)
                val animatable = btnAddNotes.drawable as Animatable
                animatable.start()
            }
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
}
