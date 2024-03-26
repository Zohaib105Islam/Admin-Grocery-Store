package com.example.adminblinkit.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adminblinkit.databinding.ItemViewImageSelectionBinding

class AdapterSelectedImage(val imageUris : ArrayList<Uri>) :
    RecyclerView.Adapter<AdapterSelectedImage.SelectedImageViewHolder>() {

    class SelectedImageViewHolder(val binding :ItemViewImageSelectionBinding) :ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        val image  = imageUris[position]
        holder.binding.apply {
            selectImage.setImageURI(image)
        }

        holder.binding.removeBtn.setOnClickListener{
            if (position < imageUris.size){
                imageUris.removeAt(position)
               // notifyItemChanged(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, imageUris.size)
            }
        }
    }


}