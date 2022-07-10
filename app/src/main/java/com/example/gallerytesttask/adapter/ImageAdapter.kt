package com.example.gallerytesttask.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallerytesttask.R
import com.example.gallerytesttask.model.EnumItem
import com.example.gallerytesttask.model.GalleryItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
) : RecyclerView.Adapter<ImageAdapter.ItemHolder>() {
    private var list = mutableListOf<GalleryItem>()


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            if (icon!=null){
                Log.d("bind ", item.toString())
                Glide.with(context)
                    .load(item)
                    //.override(600,300)
                    .into(icon)
            }
        }

        private val icon = itemView.findViewById<ImageView>(R.id.grid_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view: View
        if (viewType == EnumItem.IS_NOT_EMPTY.ordinal) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_item, parent, false)
        } else if (viewType == EnumItem.IS_EMPTY_GREEN.ordinal){
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_green, parent, false)
        }else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_blue, parent, false)
        }
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(list[position].uri)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addList(list: MutableList<GalleryItem>) {
        Log.d("list size", list.size.toString())
        for (item in list) {
            Log.d("item add", "")
            this.list.add(item)
            notifyItemInserted(itemCount)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list.get(position).enumItem.ordinal
    }
}