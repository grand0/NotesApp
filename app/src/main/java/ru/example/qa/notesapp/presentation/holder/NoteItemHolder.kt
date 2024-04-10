package ru.example.qa.notesapp.presentation.holder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.example.qa.notesapp.databinding.ItemNoteBinding
import ru.example.qa.notesapp.domain.model.NoteModel

class NoteItemHolder(
    private val binding: ItemNoteBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: NoteModel) {
        with (binding) {
            tvTitle.text = item.title
            if (item.fileId != null) {
                ivFile.isVisible = true
//                Glide.with(itemView)
//                    .load(item.fileId)
//                    .into(binding.ivFile)
            } else {
                ivFile.isVisible = false
                ivFile.setImageDrawable(null)
            }
        }
    }
}