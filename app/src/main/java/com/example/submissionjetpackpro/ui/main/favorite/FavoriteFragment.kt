package com.example.submissionjetpackpro.ui.main.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.submissionjetpackpro.R
import com.example.submissionjetpackpro.databinding.FragmentFavoriteBinding
import com.example.submissionjetpackpro.ui.detail.DetailActivity
import com.example.submissionjetpackpro.ui.main.adapter.MovieAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var categoryMovie: String
    private lateinit var adapter: MovieAdapter
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapter = MovieAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryMovie = requireContext().resources.getStringArray(R.array.category_movie)[0]
        getView()?.findViewById<SearchView>(R.id.search_view_movie)?.apply {
            visibility = View.GONE
        }

        setupListener()
        setupAdapter()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMovies(categoryMovie)
    }

    private fun setupObserver() {
        viewModel.movies.observe(requireActivity(), {
            when(it) {
                is Resource.Error -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to get data favorite movie", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> binding.loading.visibility = View.VISIBLE

                is Resource.Success -> {
                    binding.loading.visibility = View.GONE
                    lifecycleScope.launch {
                        val data = it.data
                        if (data != null) {
                            adapter.submitData(data)

                            binding.notFoundContent.isVisible = adapter.itemCount < 1
                        }
                    }
                }
            }
        })
    }

    private fun setupAdapter() {
        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun setupListener() {
        adapter.onClickAction = {
            startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.GET_DATA, it)
                putExtra(DetailActivity.GET_CATEGORY, categoryMovie)
            })
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> categoryMovie = requireContext().resources.getStringArray(R.array.category_movie)[0]
                    1 -> categoryMovie = requireContext().resources.getStringArray(R.array.category_movie)[1]
                }

                viewModel.getMovies(categoryMovie)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}