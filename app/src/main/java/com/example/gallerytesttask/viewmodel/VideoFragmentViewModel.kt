package com.example.gallerytesttask.viewmodel

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gallerytesttask.model.GalleryItem
import com.example.gallerytesttask.toListGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class VideoFragmentViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context
) : ViewModel(), CoroutineScope{
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    private var videoLiveData: MutableLiveData<List<GalleryItem>?> = MutableLiveData()
    fun getVideoList(): MutableLiveData<List<GalleryItem>?> {
        return videoLiveData
    }

    private fun getAllVideoPath(): ArrayList<String> {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val pathArrList = ArrayList<String>()
        //int vidsCount = 0;
        if (cursor != null) {
            //vidsCount = cursor.getCount();
            //Log.d(TAG, "Total count of videos: " + vidsCount);
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0))
            }
            cursor.close()
        }
        Log.d("getAllVideoPath", pathArrList.size.toString());

        return pathArrList
    }

    fun getAllVideo() {
        launch(Dispatchers.Main) {
            videoLiveData.value = withContext(Dispatchers.IO) {
                Log.d("getAllVideoPath",getAllVideoPath().toString())
                getAllVideoPath().toListGallery()
            }
        }
    }
}