package com.example.tanganbicara.features.materiedukasi.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tanganbicara.databinding.ItemMateriEdukasiBinding
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasi

class MateriEdukasiAdapter(private var materiList: List<MateriEdukasi>) :
    RecyclerView.Adapter<MateriEdukasiAdapter.MateriViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val binding = ItemMateriEdukasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MateriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val materi = materiList[position]
        holder.bind(materi)
    }

    override fun getItemCount(): Int = materiList.size

    // Tambahkan metode untuk memperbarui data
    fun updateData(newData: List<MateriEdukasi>) {
        materiList = newData
        notifyDataSetChanged()
    }

    inner class MateriViewHolder(private val binding: ItemMateriEdukasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(materi: MateriEdukasi) {
            binding.textJudul.text = materi.judul
            binding.textJumlah.text = "${materi.jumlahMateri} Materi"
            binding.progressMateri.progress = materi.progress
            binding.imageMateri.setImageResource(materi.gambarResId)
        }
    }
}

