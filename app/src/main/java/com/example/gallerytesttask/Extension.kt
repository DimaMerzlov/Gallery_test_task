package com.example.gallerytesttask

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.gallerytesttask.databinding.DialogLayoutBinding
import com.example.gallerytesttask.model.EnumItem
import com.example.gallerytesttask.model.GalleryItem

fun Context?.showGalleryDialog(
    sureClickListener: (bool:Boolean) -> Unit
) {
    this?.let { context ->

        val binding = DataBindingUtil.inflate<DialogLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_layout,
            null,
            false
        )
        MaterialDialog(context)
            .customView(R.layout.dialog_layout, binding.root)
            .cornerRadius(literalDp = 38f)
            .show {
                binding.apply {
                    tvClose.setOnClickListener {
                        sureClickListener.invoke(false)
                        dismiss()
                    }
                    tvStart.setOnClickListener {
                        sureClickListener.invoke(true)
                        dismiss()
                    }
                }
            }
    }
}

fun ArrayList<String>.toListGallery():List<GalleryItem>{
    var count=0
    var isGreen=true
    var listItems= mutableListOf<GalleryItem>()
    for (item in this) {
        count++
        if (count<3){
            listItems.add(GalleryItem(item, EnumItem.IS_NOT_EMPTY))
        }else{
            count=0
            if (isGreen){
                listItems.add(GalleryItem(item, EnumItem.IS_EMPTY_GREEN))
                isGreen=false
            }else{
                listItems.add(GalleryItem(item, EnumItem.IS_EMPTY_BLUE))
                isGreen=true
            }

        }
    }
    return listItems
}