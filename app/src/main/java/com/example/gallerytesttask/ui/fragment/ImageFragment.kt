package com.example.gallerytesttask.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallerytesttask.R
import com.example.gallerytesttask.adapter.IClickListenerItem
import com.example.gallerytesttask.adapter.ImageAdapter
import com.example.gallerytesttask.databinding.FragmentImageBinding
import com.example.gallerytesttask.model.EnumItem
import com.example.gallerytesttask.model.GalleryItem
import com.example.gallerytesttask.ui.activity.DetailActivity
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
        initAdapter()
        Log.d("Gragment","onCreateView")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        imageAdapter.clear()
        viewModel.getAllImages()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()

        viewModel.getAllImages()
    }



    private fun observe() {
        viewModel.getImageList().observe(viewLifecycleOwner) { list ->
            if (list != null) {
                listItems = list as MutableList<GalleryItem>
                Log.d("imageAdapter.getSizeList()", imageAdapter.getSizeList().toString())
                if (imageAdapter.getSizeList() == 0) imageAdapter.addList(list)
            }
        }
    }

    private fun initAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (listItems.size != 0) {
                    val enumItem = imageAdapter.getItemByPosition(position).enumItem
                    return if (enumItem == EnumItem.IS_NOT_EMPTY) 1
                    else 2
                }
                return 2

            }
        }

        imageAdapter.setIClickListenerItem(object :IClickListenerItem{
            override fun click(item: String) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(IMAGE_URI,item)
                startActivity(intent)
            }

        })


        with(binding) {
            rvFolder.apply {
                adapter = imageAdapter
                layoutManager = gridLayoutManager
                setHasFixedSize(true)
            }

        }
    }

    companion object{
        val IMAGE_URI="image uri"
    }

}