package ru.example.qa.notesapp.presentation.holder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.example.qa.notesapp.databinding.ItemNoteBinding
import ru.example.qa.notesapp.domain.model.NoteModel

class NoteItemHolder(
    private val binding: ItemNoteBinding,
    private val onItemClickListener: (NoteModel) -> Unit,
    private val onItemMenuClickListener: (NoteModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: NoteModel) {
        with (binding) {
            tvTitle.isVisible = !item.title.isNullOrEmpty() || item.content.isNullOrEmpty()
            tvTitle.text = item.title
            tvContents.isVisible = !item.content.isNullOrEmpty()
            tvContents.text = item.content
            if (item.fileUri != null) {
                ivFile.isVisible = true
                Glide.with(itemView)
                    .load(item.fileUri)
                    .into(binding.ivFile)
            } else {
                ivFile.isVisible = false
                ivFile.setImageDrawable(null)
            }

            root.setOnClickListener {
                onItemClickListener(item)
            }
            root.setOnLongClickListener {
                onItemMenuClickListener(item)
                true
            }
            ivMoreBtn.setOnClickListener {
                onItemMenuClickListener(item)
            }
        }
    }
}