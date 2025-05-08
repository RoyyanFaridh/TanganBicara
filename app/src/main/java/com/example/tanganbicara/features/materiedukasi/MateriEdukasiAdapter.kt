package com.example.tanganbicara.features.materiedukasi.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tanganbicara.databinding.ItemMateriEdukasiBinding
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiEntity

class MateriEdukasiAdapter(private var materiList: List<MateriEdukasiEntity>) :
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

    // Update data tanpa membuat instance baru adapter
    fun updateData(newData: List<MateriEdukasiEntity>) {
        materiList = newData
        notifyDataSetChanged() // Memanggil perubahan data pada RecyclerView
    }

    inner class MateriViewHolder(private val binding: ItemMateriEdukasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(materi: MateriEdukasiEntity) {
            binding.textJudul.text = materi.judul
            binding.textJumlah.text = "${materi.jumlahMateri} Materi"
            binding.progressMateri.progress = materi.progress
            binding.imageMateri.setImageResource(materi.gambarResId)
        }
    }
}
