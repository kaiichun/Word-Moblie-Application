package com.alvin.wordapplicationindividualproject.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.wordapplicationindividualproject.data.model.Word
import com.alvin.wordapplicationindividualproject.databinding.LayoutCompletedWordItemBinding

class CompletedWordAdapter(private var words: List<Word>
) : RecyclerView.Adapter<CompletedWordAdapter.CompletedWordHolder>()  {

    // Listener interface to handle click events
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedWordHolder {
        val binding = LayoutCompletedWordItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompletedWordHolder(binding)
    }

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: CompletedWordHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    // Updated the list of words and notify the adapter data set has changed
    @SuppressLint("NotifyDataSetChanged")
    fun setCompletedWorks(words: List<Word>) {
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

    inner class CompletedWordHolder(
        private val binding: LayoutCompletedWordItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            val shortTitle = letTextBecomeDot(word.title, 20)
            val shortMeaning = letTextBecomeDot(word.meaning, 30)
            binding.tvTitle.text = shortTitle
            binding.tvMeaning.text = shortMeaning
            // Set a click listener on the card view
            binding.cvCompletedWords.setOnClickListener {
                Log.d("debugging" ,"test")
                // If a listener is set, call the onClick method with the word
                listener?.onClick(word)
            }
        }
    }

    // Listener interface for click events
    interface Listener {
        fun onClick(word: Word)
    }
}

