package com.example.tanganbicara

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MateriAdapter(
    private val materiList: List<Materi>,
    private val onItemClick: (Materi) -> Unit // menerima callback saat card diklik
) : RecyclerView.Adapter<MateriAdapter.MateriViewHolder>() {

    class MateriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textJudul: TextView = itemView.findViewById(R.id.textJudul)
        val textJumlah: TextView = itemView.findViewById(R.id.textJumlah)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressMateri)
//        val imageMateri: ImageView = itemView.findViewById(R.id.imageMateri)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edukasi_materi, parent, false)
        return MateriViewHolder(view)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val materi = materiList[position]
        holder.textJudul.text = materi.judul
        holder.textJumlah.text = "${materi.subbab.size} Materi"
        holder.progressBar.progress = materi.progress
//        holder.imageMateri.setImageResource(R.drawable.ic_launcher_foreground) // ganti jika pakai gambar dinamis

        // Event klik
        holder.itemView.setOnClickListener {
            onItemClick(materi)
        }
    }

    override fun getItemCount(): Int = materiList.size
}
