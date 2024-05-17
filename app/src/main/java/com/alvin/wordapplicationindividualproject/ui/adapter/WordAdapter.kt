package com.alvin.wordapplicationindividualproject.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.wordapplicationindividualproject.data.model.Word
import com.alvin.wordapplicationindividualproject.databinding.LayoutWordItemBinding

class WordAdapter(private var words: List<Word>
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>()  {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = LayoutWordItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WordViewHolder(binding)
    }

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    fun letTextBecomeDot(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength) + "..."
        } else {
            text
        }
    }

    inner class WordViewHolder(
        private val binding: LayoutWordItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            val shortTitle = letTextBecomeDot(word.title, 20)
            val shortMeaning = letTextBecomeDot(word.meaning, 30)
            binding.tvTitle.text = shortTitle
            binding.tvMeaning.text = shortMeaning
            binding.cvWords.setOnClickListener {
                listener?.onClick(word)
            }
        }
    }

    interface Listener {
        fun onClick(word: Word)
    }
}

