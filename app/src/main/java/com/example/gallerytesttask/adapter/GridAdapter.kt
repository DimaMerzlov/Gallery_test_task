package com.example.gallerytesttask.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.gallerytesttask.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class GridAdapter(private val context: Context,
                  private val list: List<String>
): BaseAdapter() {
    //private var listUri= mutableListOf<Uri>()
    private var inflater:LayoutInflater?=null



    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        if (inflater==null){
            inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        var convertView:View = view ?: inflater!!.inflate(R.layout.grid_item,null)
        val imageView=convertView.findViewById<ImageView>(R.id.grid_image)
        Glide.with(context)
            .load(list[position])
            /*.er(R.drawable.placeholder)*/
            .into(imageView)

        return convertView

    }

}