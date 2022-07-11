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
class VideoAdapter @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : RecyclerView.Adapter<VideoAdapter.ItemVideoHolder>() {
    private var list = mutableListOf<GalleryItem>()

    private lateinit var clickListener: IClickListenerItem

    fun setIClickListenerItem(clickListenerItem: IClickListenerItem) {
        this.clickListener = clickListenerItem
    }


    inner class ItemVideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            if (icon != null) {
                Glide.with(context)
                    .load(item)
                    .into(icon)
            }
            itemView.setOnClickListener {
                clickListener.click(item)
            }
        }

        private val icon = itemView.findViewById<ImageView>(R.id.grid_image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addList(list: MutableList<GalleryItem>) {
        for (item in list) {
            this.list.add(item)
            notifyItemInserted(itemCount)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list.get(position).enumItem.ordinal
    }

    fun getSizeList() = list.size

    fun getItemByPosition(position: Int): GalleryItem {
        return list[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVideoHolder {
        var view: View
        if (viewType == EnumItem.IS_NOT_EMPTY.ordinal) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_item, parent, false)
        } else if (viewType == EnumItem.IS_EMPTY_GREEN.ordinal) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_green, parent, false)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_blue, parent, false)
        }
        return ItemVideoHolder(view)
    }

    override fun onBindViewHolder(holder: ItemVideoHolder, position: Int) {
        holder.bind(list[position].uri)
    }
}