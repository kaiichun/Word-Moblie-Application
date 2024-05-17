package com.alvin.wordapplicationindividualproject.ui.container

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alvin.wordapplicationindividualproject.databinding.FragmentContainerBinding
import com.alvin.wordapplicationindividualproject.ui.adapter.ContainerAdapter
import com.alvin.wordapplicationindividualproject.ui.compeleted.CompletedFragment
import com.alvin.wordapplicationindividualproject.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class ContainerFragment : Fragment() {
    private lateinit var binding: FragmentContainerBinding
    private val viewModel: ContainerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpTabs.adapter = ContainerAdapter(this, listOf(HomeFragment(), CompletedFragment()))

        TabLayoutMediator(binding.tlTabs, binding.vpTabs) { tab, position ->
            when (position) {
                0 -> tab.text = "New Word"
                else -> tab.text = "Completed Word"
            }
        }.attach()
    }
}