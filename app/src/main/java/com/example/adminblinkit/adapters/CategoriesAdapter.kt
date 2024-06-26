package com.example.adminblinkit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adminblinkit.databinding.ItemViewProductCategoriesBinding
import com.example.adminblinkit.models.Category

class CategoriesAdapter(
    private val categoryArrayList: ArrayList<Category>,
    val onCategoryClicked: (Category) -> Unit
): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {


    class CategoriesViewHolder(val binding: ItemViewProductCategoriesBinding):ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(ItemViewProductCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categoryArrayList[position]
        holder.binding.apply {
            ivCategoryImage.setImageResource(category.icon)
            tvCategoryTitle.text=category.category
        }

        holder.itemView.setOnClickListener{
             onCategoryClicked(category)
        }
    }
}