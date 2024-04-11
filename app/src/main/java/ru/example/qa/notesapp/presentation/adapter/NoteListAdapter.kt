package ru.example.qa.notesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.example.qa.notesapp.databinding.ItemNoteBinding
import ru.example.qa.notesapp.domain.model.NoteModel
import ru.example.qa.notesapp.presentation.adapter.diffutil.NoteDiffUtilItemCallback
import ru.example.qa.notesapp.presentation.holder.NoteItemHolder

class NoteListAdapter(
    private val onItemClickListener: (NoteModel) -> Unit,
) : ListAdapter<NoteModel, NoteItemHolder>(NoteDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemHolder =
        NoteItemHolder(
            binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClickListener = onItemClickListener,
        )

    override fun onBindViewHolder(holder: NoteItemHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
}