package com.example.submissionjetpackpro.ui.main.film

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
import com.example.submissionjetpackpro.databinding.FragmentFilmBinding
import com.example.submissionjetpackpro.ui.detail.DetailActivity
import com.example.submissionjetpackpro.ui.main.adapter.MovieAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilmFragment : Fragment() {

    private val filmViewModel: FilmViewModel by viewModel()
    private lateinit var categoryMovie: String
    private lateinit var adapter: MovieAdapter
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        adapter = MovieAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryMovie = requireContext().resources.getStringArray(R.array.category_movie)[0]

        setupAdapter()
        setupObserver()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchViewMovie.root.apply {
            visibility = View.VISIBLE
            queryHint = requireContext().getString(R.string.hint_film)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (p0.isNullOrEmpty()) {
                        filmViewModel.getMovies(categoryMovie)
                    } else {
                        filmViewModel.searchMovie(categoryMovie, p0)
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0.isNullOrEmpty()) filmViewModel.getMovies(categoryMovie)
                    return true
                }

            })
        }

    }

    override fun onResume() {
        super.onResume()
        filmViewModel.getMovies(categoryMovie)
    }

    private fun setupAdapter() {
        with(binding) {
            adapter.onClickAction = {
                startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.GET_DATA, it)
                    putExtra(DetailActivity.GET_CATEGORY, categoryMovie)
                })
            }
            rvFilm.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFilm.adapter = adapter
        }
    }

    private fun setupObserver() {
        filmViewModel.movies.observe(requireActivity(), {
            when (it) {
                is Resource.Error -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Failed to get data movie, please check your connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> binding.loading.visibility = View.VISIBLE

                is Resource.Success -> {
                    binding.loading.visibility = View.GONE
                    lifecycleScope.launch {
                        println(it.data)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}