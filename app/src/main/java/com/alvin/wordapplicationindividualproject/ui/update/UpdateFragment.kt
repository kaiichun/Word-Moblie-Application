package com.alvin.wordapplicationindividualproject.ui.update

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvin.wordapplicationindividualproject.databinding.FragmentUpdateBinding
import kotlinx.coroutines.launch

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val updateViewModel: UpdateViewModel by viewModels { UpdateViewModel.Factory }
    private var selectedWordId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(
            layoutInflater,
            container,
            false
        )

        updateViewModel.selectedWord.observe(viewLifecycleOwner) { word ->
            word.let {
                updateViewModel.setWord(it)
            }
        }

        //to get the id of the selected task
        arguments?.let {
            selectedWordId = UpdateFragmentArgs.fromBundle(it).id
            updateViewModel.getWordById(selectedWordId)
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editWork = updateViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            updateViewModel.finish.collect {
                findNavController().popBackStack()
            }
        }

        updateViewModel.snackbar.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}