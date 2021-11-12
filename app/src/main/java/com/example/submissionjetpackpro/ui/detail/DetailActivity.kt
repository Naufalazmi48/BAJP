package com.example.submissionjetpackpro.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.data.Resource
import com.example.core.domain.model.Movie
import com.example.submissionjetpackpro.R
import com.example.submissionjetpackpro.databinding.ActivityDetailBinding
import com.example.submissionjetpackpro.ui.main.adapter.CasterAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var categoryMovie: String
    private lateinit var adapter: CasterAdapter
    private var showSnackBar = false
    private val viewModel: DetailViewModel by viewModel()
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryMovie = intent.getStringExtra(GET_CATEGORY) ?: ""
        movie = intent.getParcelableExtra(GET_DATA)
        adapter = CasterAdapter(this)

        setUI(movie)
        setupAdapter()
        setupListener()
        setupObserver()

        viewModel.isFavoriteMovie(movie?.id ?: -1)
        viewModel.getCasters(categoryMovie, movie?.id ?: -1)
    }

    private fun setupListener() {
        with(binding) {
            watchNow.setOnClickListener {
                val uri = Uri.parse("https://www.themoviedb.org/search?query=${movie?.title}")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

            favButton.setOnClickListener {
                val movie = this@DetailActivity.movie
                if (movie != null) {
                    showSnackBar = true
                    viewModel.setFavoriteMovie(
                        movie,
                        categoryMovie,
                        adapter.snapshot().toList().filterNotNull()
                    )
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.casters.observe(this, {
            when (it) {
                is Resource.Error -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Failed to get data caster, please check your connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> binding.loading.visibility = View.VISIBLE

                is Resource.Success -> {
                    binding.loading.visibility = View.GONE
                    lifecycleScope.launch {
                        adapter.submitData(it.data ?: PagingData.empty())
                    }
                }
            }
        })

        viewModel.favorite.observe(this, {
            when (it) {
                is Resource.Error -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(this, "Failed to set favorite movie", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> binding.loading.visibility = View.VISIBLE

                is Resource.Success -> {
                    binding.loading.visibility = View.GONE
                    val data = it.data
                    if (data != null) {
                        binding.favButton.isSelected = data

                        if (showSnackBar) {
                            val message =
                                if (data) getString(R.string.added_to_favorite_movie, movie?.title)
                                else getString(R.string.removed_to_favorite_movie, movie?.title)
                            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        })
    }

    private fun setupAdapter() {
        with(binding.rvCasts) {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@DetailActivity.adapter
        }
    }

    private fun setUI(movie: Movie?) {
        with(binding) {
            textSinopsis.text = movie?.synopsis
            titleFilm.text = movie?.title
            detailFilm.text = movie?.releaseDate

            Glide.with(poster)
                .load(getString(R.string.img_path) + movie?.poster)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_error)
                        .error(R.drawable.ic_error)
                )
                .into(poster)


        }
    }

    companion object {
        const val GET_DATA = "GET_DATA"
        const val GET_CATEGORY = "GET_CATEGORY"
    }
}