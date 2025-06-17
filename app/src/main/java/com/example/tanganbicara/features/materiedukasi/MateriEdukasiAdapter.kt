package com.example.tanganbicara.features.materiedukasi.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tanganbicara.databinding.ItemMateriEdukasiBinding
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiEntity

class MateriEdukasiAdapter(
    private val onItemClick: (MateriEdukasiEntity) -> Unit
) : ListAdapter<MateriEdukasiEntity, MateriEdukasiAdapter.MateriViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val binding = ItemMateriEdukasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MateriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val materi = getItem(position)
        holder.bind(materi)
    }

    inner class MateriViewHolder(private val binding: ItemMateriEdukasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(materi: MateriEdukasiEntity) {
            binding.textJudul.text = materi.judul
            binding.textJumlah.text = "${materi.jumlahMateri} Materi"
            binding.progressMateri.progress = materi.progress
            binding.imageMateri.setImageResource(materi.gambarResId)

            binding.root.setOnClickListener {
                onItemClick(materi)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MateriEdukasiEntity>() {
        override fun areItemsTheSame(oldItem: MateriEdukasiEntity, newItem: MateriEdukasiEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MateriEdukasiEntity, newItem: MateriEdukasiEntity): Boolean {
            return oldItem == newItem
        }
    }
}
