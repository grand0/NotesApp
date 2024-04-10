package ru.example.qa.notesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.example.qa.notesapp.databinding.ItemNoteBinding
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.presentation.adapter.diffutil.NoteDiffUtilItemCallback
import ru.example.qa.notesapp.presentation.holder.NoteItemHolder

class NoteListAdapter : ListAdapter<NoteModel, NoteItemHolder>(NoteDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemHolder =
        NoteItemHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NoteItemHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
}