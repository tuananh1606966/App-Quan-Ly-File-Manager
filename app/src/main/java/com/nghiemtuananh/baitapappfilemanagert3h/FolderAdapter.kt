package com.nghiemtuananh.baitapappfilemanagert3h

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.ItemFileBinding
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IDataAndClick

class FolderAdapter: RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    var inter: IDataAndClick
    constructor(inter: IDataAndClick) {
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return FolderViewHolder(ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        var fileData = inter.getItem(holder.adapterPosition)
        holder.binding.data = fileData
        holder.itemView.setOnClickListener {
            inter.onClick(fileData)
            notifyItemChanged(holder.adapterPosition)
        }
        holder.itemView.setOnLongClickListener {
            if (inter.getItem(holder.adapterPosition).isVisibleCheckBox) {
                inter.onLongClick(fileData)
                notifyItemChanged(holder.adapterPosition)
            } else {
                inter.onLongClick(fileData)
                notifyDataSetChanged()
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return inter.getCount()
    }

    inner class FolderViewHolder(var binding: ItemFileBinding): RecyclerView.ViewHolder(binding.root)
}