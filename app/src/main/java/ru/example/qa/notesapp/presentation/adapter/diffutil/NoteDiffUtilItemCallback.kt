package ru.example.qa.notesapp.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.example.qa.notesapp.domain.model.NoteModel

class NoteDiffUtilItemCallback : DiffUtil.ItemCallback<NoteModel>() {

    override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean =
        oldItem == newItem
}