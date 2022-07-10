package com.example.gallerytesttask.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallerytesttask.R
import com.example.gallerytesttask.adapter.ImageAdapter
import com.example.gallerytesttask.databinding.FragmentImageBinding
import com.example.gallerytesttask.model.EnumItem
import com.example.gallerytesttask.model.GalleryItem
import com.example.gallerytesttask.viewmodel.ImageFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageFragment : Fragment() {
    private lateinit var binding: FragmentImageBinding
    val viewModel by viewModels<ImageFragmentViewModel>()
    var listItems = mutableListOf<GalleryItem>()

    @Inject
    lateinit var imageAdapter: ImageAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observe()
        viewModel.getAllImages()
    }

    private fun observe() {
        viewModel.getImageList().observe(viewLifecycleOwner) { list ->
            if (list != null) {
                listItems = list as MutableList<GalleryItem>
                imageAdapter.addList(list)
            }
        }
    }

    private fun initAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (listItems.get(position).enumItem == EnumItem.IS_NOT_EMPTY) return 1
                else return 2
            }

        }
        with(binding) {
            rvFolder.apply {
                adapter = imageAdapter
                layoutManager = gridLayoutManager
            }

        }
    }

}