package com.alvin.wordapplicationindividualproject.ui.compeleted

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isInvisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.wordapplicationindividualproject.data.model.Word
import com.alvin.wordapplicationindividualproject.databinding.AlertSortViewBinding
import com.alvin.wordapplicationindividualproject.databinding.FragmentCompletedBinding
import com.alvin.wordapplicationindividualproject.ui.adapter.CompletedWordAdapter
import com.alvin.wordapplicationindividualproject.ui.container.ContainerFragmentDirections
import com.alvin.wordapplicationindividualproject.ui.container.ContainerViewModel
import kotlinx.coroutines.launch

class CompletedFragment : Fragment() {
    private lateinit var binding: FragmentCompletedBinding
    private lateinit var adapter: CompletedWordAdapter
    private lateinit var originalWord: List<Word>
    private val parentViewModel: ContainerViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val viewModel: CompletedViewModel by viewModels { CompletedViewModel.Factory }
    private var selectedSortBy = "title"
    private var selectedSortOrder = "asc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeCompletedViewModel() // Observe changes in ViewModel

        // Coroutine for refreshing data
        lifecycleScope.launch {
            parentViewModel.refreshHome.collect {
                viewModel.getAllCompletedWords() // Get all completed words
            }
        }

        // Search functionality
        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterWords(newText) // Filter words based on search query
                return true
            }
        })

        binding.ivSort.setOnClickListener {
            val alertView = AlertSortViewBinding.inflate(layoutInflater)
            val sortDialog = AlertDialog.Builder(requireContext())
            sortDialog.setView(alertView.root)
            val dialog = sortDialog.create() // Create the sort dialog

            // Setting up listeners for radio buttons
            alertView.rbTitle.setOnClickListener {
                // Clear selection of the date radio button
                alertView.rbDate.isChecked = false
                selectedSortBy = "title"
            }
            alertView.rbDate.setOnClickListener {
                alertView.rbTitle.isChecked = false
                selectedSortBy = "date"
            }
            alertView.rbAscending.setOnClickListener {
                alertView.rbDescending.isChecked = false
                selectedSortOrder = "asc"
            }
            alertView.rbDescending.setOnClickListener {
                alertView.rbAscending.isChecked = false
                selectedSortOrder = "desc"
            }

            alertView.btnDone.setOnClickListener {
                // Sort the originalWord list based on selected options
                val sortedList = originalWord.sortedWith(compareBy<Word> {
                    if (selectedSortBy == "date") it.date else it.title
                }.run {
                    if (selectedSortOrder == "asc") this else reversed()
                })

                adapter.setCompletedWorks(sortedList)
                dialog.dismiss() // Dismiss the sort dialog
            }

            dialog.show() // Show the sort dialog
        }
    }

    // Set up RecyclerView adapter
    private fun setupAdapter() {
        adapter = CompletedWordAdapter(emptyList())
        adapter.listener = object : CompletedWordAdapter.Listener{
            override fun onClick(word: Word) {
                // Navigate to ViewDoneDeleteFragment when a word is clicked
                findNavController().navigate(
                    ContainerFragmentDirections.actionContainerFragmentToViewDoneDeleteFragment(word.id!!)
                )
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompletedWords.adapter = adapter
        binding.rvCompletedWords.layoutManager = layoutManager
    }

    // Observe changes in ViewModel
    private fun observeCompletedViewModel() {
        viewModel.getAllCompletedWords()  // Get all completed words

        // Observe changes in the list of completed words
        viewModel.words.observe(viewLifecycleOwner) { words ->
            // Store the fetched words in the originalWord list
            originalWord = words
            // Update the adapter with the fetched words to display them in the RecyclerView
            adapter.setCompletedWorks(words)
            // If the adapter has items, hide the "no completed words" message; otherwise, show it
            binding.tvNoCompletedWords.isInvisible = adapter.itemCount != 0
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show the progress bar if data is loading, otherwise hide it
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Coroutine for refreshing data
        lifecycleScope.launch {
            // Collect data from the refreshHome flow in the parentViewModel
            parentViewModel.refreshHome.collect {
                viewModel.getAllCompletedWords()
            }
        }
    }

    private fun filterWords(query: String?) {
        if (query.isNullOrBlank()) {
            // Show all works if the query is null or blank
            adapter.setCompletedWorks(originalWord)
        } else {
            val filteredWords = originalWord.filter {
                it.title.contains(query, ignoreCase = true)
            }
            // Show filtered works
            adapter.setCompletedWorks(filteredWords)
        }
    }
}