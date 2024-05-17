package com.alvin.wordapplicationindividualproject.ui.viewDoneDelete

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvin.wordapplicationindividualproject.R
import com.alvin.wordapplicationindividualproject.databinding.AlertCompletedViewBinding
import com.alvin.wordapplicationindividualproject.databinding.AlertDeleteWordViewBinding
import com.alvin.wordapplicationindividualproject.databinding.FragmentViewDoneDeleteBinding
import kotlinx.coroutines.launch

class ViewDoneDeleteFragment : Fragment() {
    private lateinit var binding: FragmentViewDoneDeleteBinding
    private val wordItemViewModel: ViewDoneDeleteViewModel by viewModels { ViewDoneDeleteViewModel.Factory }
    private var selectedWordId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewDoneDeleteBinding.inflate(
            layoutInflater,
            container,
            false
        )
        arguments?.let {
            selectedWordId = ViewDoneDeleteFragmentArgs.fromBundle(it).id
            wordItemViewModel.getWordById(selectedWordId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedWord() // Observe change the selected word data
        observeSelectedWordLoadingState() // Observe loading state
        setupButtons() // Set up button click listeners
    }

    private fun observeSelectedWord() {
        lifecycleScope.launch {
            wordItemViewModel.selectedWord.observe(viewLifecycleOwner) { word ->
                word?.let {
                    binding.run {
                        tvTitle.text = word.title
                        tvMeaning.text = word.meaning
                        tvSynonym.text = word.synonym
                        tvDetails.text = word.details
                    }
                    updateButtonTextAndBackground(word.status)
                }
            }
        }
    }

    private fun observeSelectedWordLoadingState() {
        // Observe the loading state from the ViewModel
        wordItemViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Update the visibility of the progress bar based on the loading state
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Update the visibility of the main content based on the loading state
            binding.clViewContent.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupButtons() {
        // Set up the update button click listener
        binding.btnUpdate.setOnClickListener {
            findNavController().navigate(
                ViewDoneDeleteFragmentDirections.actionViewDoneDeleteFragmentToUpdateFragment(selectedWordId)
            )
        }

        // Set up the delete button click listener
        binding.btnDelete.setOnClickListener {
            val alertView = AlertDeleteWordViewBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            alertView.tvTitle.text = "Are you sure?"
            alertView.tvBody.text = "You want to delete this word? \n Action can not be undone."

            val temporaryDeleteDialog = deleteDialog.create()

            alertView.btnDelete.setOnClickListener {
                wordItemViewModel.delete()
                findNavController().navigate(
                    ViewDoneDeleteFragmentDirections.actionViewDoneDeleteFragmentToContainerFragment()
                )
                Toast.makeText(
                    requireContext(),
                    "Deleted Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                temporaryDeleteDialog.dismiss()
            }

            alertView.btnCancel.setOnClickListener {
                temporaryDeleteDialog.dismiss() // Dismiss the dialog when cancel button is clicked
            }
            // Show the delete dialog
            temporaryDeleteDialog.show()
        }

        // Set up the done/undo button click listener
        binding.btnDone.setOnClickListener {
            wordItemViewModel.selectedWord.value?.let { word ->
            val alertView = AlertCompletedViewBinding.inflate(layoutInflater)
            val doneDialog = AlertDialog.Builder(requireContext())
                doneDialog.setView(alertView.root)
            alertView.tvTitle.text = "Are you sure?"
                if (word.status == true) {
                    alertView.tvBody.text = "Want to move this back to new word?"
                } else {
                    alertView.tvBody.text = "Want to move this word to completed?"
                }

            val temporaryDeleteDialog = doneDialog.create()

            alertView.btnYes.setOnClickListener {
                wordItemViewModel.moveWordToAnywhere() // Move the word status
                findNavController().navigate(
                    ViewDoneDeleteFragmentDirections.actionViewDoneDeleteFragmentToContainerFragment())
                temporaryDeleteDialog.dismiss()
            }

            alertView.btnNo.setOnClickListener {
                temporaryDeleteDialog.dismiss() // Dismiss the dialog when cancel button is clicked
            }
            temporaryDeleteDialog.show()
            }
        }
    }

    // Update the button text and background color based on the word status
    private fun updateButtonTextAndBackground(status: Boolean?) {
        status?.let {
            binding.btnDone.apply {
                text = if (it) "Undo" else "Done"
                backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it) R.color.green else R.color.blue
                    )
                )
            }
        }
    }
}
