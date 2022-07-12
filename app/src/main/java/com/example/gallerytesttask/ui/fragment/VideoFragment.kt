package com.example.gallerytesttask.ui.fragment

import android.content.Intent
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
import com.example.gallerytesttask.adapter.IClickListenerItem
import com.example.gallerytesttask.adapter.VideoAdapter
import com.example.gallerytesttask.databinding.FragmentVideoBinding
import com.example.gallerytesttask.model.EnumItem
import com.example.gallerytesttask.model.GalleryItem
import com.example.gallerytesttask.ui.activity.DetailActivity
import com.example.gallerytesttask.viewmodel.VideoFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding
    val viewModel by viewModels<VideoFragmentViewModel>()
    var listItems = mutableListOf<GalleryItem>()

    @Inject
    lateinit var videoAdapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllVideo()
        viewModel.getVideoList().observe(viewLifecycleOwner) { list ->
            if (list != null) {
                listItems = list as MutableList<GalleryItem>
                if (videoAdapter.getSizeList() == 0) videoAdapter.addList(list)
            }
        }
    }

    private fun initAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (listItems.size != 0) {
                    val enumItem = videoAdapter.getItemByPosition(position).enumItem
                    return if (enumItem == EnumItem.IS_NOT_EMPTY) 1
                    else 2
                }
                return 2
            }
        }

        videoAdapter.setIClickListenerItem(object : IClickListenerItem {
            override fun click(item: String) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(VIDEO_URI,item)
                startActivity(intent)
            }

        })
        with(binding) {
            rvFolder.apply {
                adapter = videoAdapter
                layoutManager = gridLayoutManager
                setHasFixedSize(true)
            }

        }
    }
    companion object{
        val VIDEO_URI="video uri"
    }
    override fun onStart() {
        super.onStart()
        videoAdapter.clear()
        viewModel.getAllVideo()
    }

}