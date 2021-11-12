package com.example.submissionjetpackpro.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.model.Caster
import com.example.submissionjetpackpro.R
import com.example.submissionjetpackpro.databinding.ItemCastBinding

class CasterAdapter(private val context: Context) :
    PagingDataAdapter<Caster, CasterAdapter.CasterViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class CasterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCastBinding.bind(itemView)
        fun bind(caster: Caster) {
            with(binding) {
                castName.text = caster.name
                castJob.text = caster.job

                Glide.with(castPicture)
                    .load(context.getString(R.string.img_path) + caster.avatar)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_error)
                            .error(R.drawable.ic_error)
                    )
                    .into(castPicture)
            }
        }

    }

    override fun onBindViewHolder(holder: CasterViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasterViewHolder =
        CasterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        )

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Caster>() {
            override fun areItemsTheSame(oldItem: Caster, newItem: Caster): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Caster, newItem: Caster): Boolean {
                return oldItem == newItem
            }
        }
    }
}